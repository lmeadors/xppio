package com.elmsw;

import com.elmsw.core.State;
import com.elmsw.core.converters.IntegerConverter;
import com.elmsw.core.converters.ListConverter;
import com.elmsw.core.converters.ReflectionConverter;
import com.elmsw.core.converters.StringConverter;
import com.elmsw.core.exceptionhandlers.SilentFailure;
import com.elmsw.core.namingstrategies.PropertyNameStrategy;
import com.elmsw.core.statefactory.SimpleStateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.*;

public class XppIO {

	private static final Logger log = LoggerFactory.getLogger(XppIO.class);

	final private XmlPullParserFactory xmlPullParserFactory;
	final private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	final private XPathFactory xPathFactory;
	private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

	final private Map<String, Class> aliasMap = new HashMap<String, Class>();
	private static final ThreadLocal<Map<String, Class>> localAliasMap = new ThreadLocal<Map<String, Class>>();

	final private Converter reflectionConverter = new ReflectionConverter();

	final private Map<Class, Converter> converterMap = new HashMap<Class, Converter>();
	final private ExceptionHandler exceptionHandler;
	final private NamingStrategy namingStrategy;
	final private StateFactory stateFactory;


	public XppIO(
			XmlPullParserFactory xmlPullParserFactory,
			ExceptionHandler exceptionHandler,
			NamingStrategy namingStrategy,
			StateFactory stateFactory
	) {

		this.xmlPullParserFactory = xmlPullParserFactory;
		this.exceptionHandler = exceptionHandler;
		this.namingStrategy = namingStrategy;
		this.stateFactory = stateFactory;
		this.xPathFactory = XPathFactory.newInstance();

		converterMap.put(String.class, new StringConverter());
		converterMap.put(Integer.class, new IntegerConverter());
		converterMap.put(List.class, new ListConverter());

		localAliasMap.set(new HashMap<String, Class>());

	}

	public XppIO() throws XmlPullParserException {
		this(
				XmlPullParserFactory.newInstance(),
				new SilentFailure(),
				new PropertyNameStrategy(),
				new SimpleStateFactory()
		);
	}

	public <T> T toObject(String input) {

		final XmlPullParser xpp;
		try {
			xpp = xmlPullParserFactory.newPullParser();
		} catch (XmlPullParserException e) {
			// we don't send this to the exception handler because it's a show stopper
			throw new RuntimeException(e.toString(), e);
		}

		final State state = stateFactory.get(exceptionHandler);

		try {

			xpp.setInput(new StringReader(input));
			int eventType = xpp.getEventType();

			do {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					state.begin();
				} else if (eventType == XmlPullParser.START_TAG) {
					state.processStartElement(xpp, this);
				} else if (eventType == XmlPullParser.END_TAG) {
					state.processEndElement();
				} else if (eventType == XmlPullParser.TEXT) {
					state.processText(xpp, this);
				}
				eventType = xpp.next();
			} while (eventType != XmlPullParser.END_DOCUMENT);

			state.end();

		} catch (Exception e) {
			exceptionHandler.handle(e);
		}

		return (T) state.getObject();

	}

	public String toXml(Object object) {
		String xml = null;
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			final String elementName = getElementName(object);
			document.appendChild(buildElement(object, elementName, document));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

			//create string from xml tree
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			xml = writer.toString().trim();
		} catch (Exception e) {
			exceptionHandler.handle(e);
		}
		log.debug("xml: {}", xml);
		return xml;
	}

	public Object populate(Object target, String xml) {
		return populate(target, xml, null);
	}

	public Object populate(Object target, String xml, String start) {

		Object returnValue = null;

		try {
			returnValue = populate(target, xmlAsNode(xml), start);
		} catch (Exception e) {
			exceptionHandler.handle(e);
		}

		return returnValue;

	}

	public Object populate(Object target, Node xml) {
		return populate(target, xml, null);
	}

	public Object populate(Object target, Node xml, String start) {
		try {

			final Class targetType = target.getClass();

			final Node root;
			if (start != null) {
				root = (Node) xPathFactory.newXPath().compile(start).evaluate(xml, XPathConstants.NODE);
			} else {
				root = xml;
			}

			// if the target type is a collection, we need to get it's children, populate objects from them, and add
			// them to the collection
			if (Collection.class.isAssignableFrom(targetType)) {
				log.debug("Type {} is a collection.", targetType);
				NodeList list = root.getChildNodes();
				final int nodeCount = list.getLength();
				Collection<Object> targetCollection = (Collection<Object>) target;

				for (int i = 0; i < nodeCount; i++) {
					final String input = nodeToString(list.item(i)).trim();
					// we can't map an empty string, don't be dumb here...
					if (!input.isEmpty()) {
						log.debug("adding {} to list", input);
						targetCollection.add(toObject(input));
					}
				}

			} else {
				// todo: this can be optimized a LOT! it's O(n*m) now. Suck. It could be O(n+m) at least, maybe better.
				// but it works for now...
				final Field[] fields = getFields(targetType);
				final NodeList nodeList = root.getChildNodes();
				final int nodeCount = nodeList.getLength();

				for (Field field : fields) {
					final String fieldName = field.getName();
					log.debug("looking for a match for {}", fieldName);
					for (int i = 0; i < nodeCount; i++) {
						final Node node = nodeList.item(i);
						final String nodeName = node.getNodeName();
						log.debug("node name: {}", nodeName);
						if (nodeName.equals(fieldName)) {
							final Class fieldType = field.getType();
							final int length = node.getChildNodes().getLength();
							log.debug("node has {} children", length);
							if (length < 2) {
								final Converter converter = getConverterForClass(fieldType);
								final String content = node.getTextContent();
								log.debug("found a match, value is: {}", content);
								log.debug("trying to use converter {} to set value", converter);
								field.setAccessible(true);
								field.set(target, converter.fromString(content));
							} else {
								log.debug("Node '{}' is a nested object of {}, we need to map it out if we can...",
										new Object[]{
												nodeToString(node),
												fieldType
										}
								);
								Object value = populate(
										newInstance(fieldType, nodeName),
										nodeToString(node),
										"/" + nodeName
								);
								log.debug("value is {}", value);
								field.setAccessible(true);
								field.set(target, value);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			exceptionHandler.handle(e);
		}
		return target;
	}

	private Field[] getFields(Class targetType) {
		if (Object.class.equals(targetType)) {
			// root of the object hierarchy, done.
			return targetType.getDeclaredFields();
		} else {
			// this class has a super-class - get it's fields and add it to the list of fields this class defines
			ArrayList<Field> fields = new ArrayList<Field>();
			fields.addAll(Arrays.asList(getFields(targetType.getSuperclass())));
			fields.addAll(Arrays.asList(targetType.getDeclaredFields()));
			return fields.toArray(new Field[fields.size()]);
		}
	}

	private Object newInstance(final Class fieldType, String nodeName) throws Exception {

		// try to find an alias - these take precedence
		return typeForElement(nodeName, fieldType).newInstance();

	}

	private String nodeToString(Node node) throws TransformerException {
		final Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(new DOMSource(node), result);
		return writer.toString();
	}

	private Element buildElement(Object object, String name, Document document) throws Exception {

		final Element element = document.createElement(name);

		final Class objectClass = object.getClass();
		final Converter converter = getConverterForClass(objectClass);

		final Field[] fields = converter.getFields(object);

		if (fields != Converter.NO_FIELDS) {
			for (Field field : fields) {
				log.debug("handling field {} of {}", field.getName(), objectClass);
				field.setAccessible(true);
				element.appendChild(buildElement(field.get(object), field.getName(), document));
			}
		} else {
			final String text = converter.asText(object, this);
			if (Converter.NOT_TEXT.equals(text)) {
				final String xml = converter.asXml(object, this);
				log.debug("adding xml from converter to {}", element.getTagName());
				appendXmlChildrenTo(element, xml);
			} else {
				element.setTextContent(text);
			}
		}

		return element;

	}

	private void appendXmlChildrenTo(Element element, String xml) throws Exception {
		final Node node = fragmentAsNode(xml);
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node child = childNodes.item(i);
			element.appendChild(element.getOwnerDocument().importNode(child, true));
		}
	}

	private Node fragmentAsNode(String xml) throws ParserConfigurationException, IOException, SAXException {
		// we wrap this, because we can get a list of items from the converter
		// in that case, we'll add all of the child nodes to the parent
		final String wellFormedXml = "<x>" + xml + "</x>";
		log.debug("Making {} into an xml node", wellFormedXml);
		return documentBuilderFactory.newDocumentBuilder().parse(getStringInputSource(wellFormedXml)).getDocumentElement();
	}

	private Node xmlAsNode(String xml) throws ParserConfigurationException, IOException, SAXException {
		// we wrap this, because we can get a list of items from the converter
		// in that case, we'll add all of the child nodes to the parent
		return documentBuilderFactory.newDocumentBuilder().parse(getStringInputSource(xml)).getDocumentElement();
	}

	private InputSource getStringInputSource(String xml) {
		return new InputSource(new StringReader(xml));
	}

	private String getElementName(Object object) {
		final Class objectClass = object.getClass();
		if (aliasMap.containsValue(objectClass)) {
			// crap, now i have to look this up
			return findAlias(objectClass, aliasMap);
		}else if(localAliasMap.get().containsValue(objectClass)){
			return findAlias(objectClass, localAliasMap.get());
		}
		return namingStrategy.getElementName(object);
	}

	private String findAlias(Class type, Map<String, Class> map){
		for (String alias : map.keySet()) {
			if (map.get(alias).equals(type)) {
				return alias;
			}
		}
		throw new RuntimeException("Unable to find alias in map - something changed the map while searching. Bad.");
	}

	public Converter getConverterForClass(Class fieldClass) {

		if (converterMap.containsKey(fieldClass)) {
			log.debug("Found exact match for converting {}", fieldClass);
			return converterMap.get(fieldClass);
		}

		log.debug("Couldn't find exact converter match for {}, checking interfaces", fieldClass);
		for (Class type : fieldClass.getInterfaces()) {
			log.debug("Is there a converter for {}?", type);
			if (converterMap.containsKey(type)) {
				log.debug("Found adequate match for converting {} as {}", new Object[]{fieldClass, type});
				return converterMap.get(type);
			}
		}

		log.debug("We'll just use reflection to deal with {}", fieldClass);
		converterMap.put(fieldClass, reflectionConverter);
		return reflectionConverter;

	}

	public void addLocalAlias(String alias, Class<?> type) {
		localAliasMap.get().put(alias, type);
	}

	public void addAlias(String alias, Class<?> type) {
		aliasMap.put(alias, type);
	}

	public Class typeForElement(String elementName, Class defaultType) {

		final Class nextType;

		if (aliasMap.containsKey(elementName)) {
			nextType = aliasMap.get(elementName);
		} else if (localAliasMap.get().containsKey(elementName)) {
			nextType = localAliasMap.get().get(elementName);
		} else {
			nextType = defaultType;
		}

		return nextType;

	}

	public Class typeForElement(String elementName) {
		return typeForElement(elementName, String.class);
	}

}
