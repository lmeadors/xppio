package com.elmsw;

import com.elmsw.core.IntegerConverter;
import com.elmsw.core.ListConverter;
import com.elmsw.core.ReflectionConverter;
import com.elmsw.core.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XppIO {

	private static final Logger log = LoggerFactory.getLogger(XppIO.class);

	final private XmlPullParserFactory factory;
	final private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	final private Map<String, Class> aliasMap = new HashMap<String, Class>();

	final private Converter reflectionConverter = new ReflectionConverter();

	final private Map<Class, Converter> converterMap = new HashMap<Class, Converter>();
	final private ExceptionHandler exceptionHandler;
	final private NamingStrategy namingStrategy;

	public XppIO(XmlPullParserFactory factory, ExceptionHandler exceptionHandler, NamingStrategy namingStrategy) {
		this.factory = factory;
		this.exceptionHandler = exceptionHandler;
		this.namingStrategy = namingStrategy;
		converterMap.put(String.class, new StringConverter());
		converterMap.put(Integer.class, new IntegerConverter());
		converterMap.put(List.class, new ListConverter());
	}

	public <T> T toObject(String input) {

		final XmlPullParser xpp;
		try {
			xpp = factory.newPullParser();
		} catch (XmlPullParserException e1) {
			// we don't send this to the exception handler because it's a show stopper
			throw new RuntimeException(e1.toString(), e1);
		}

		final State state = new State(exceptionHandler);

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

	private Element buildElement(Object object, String name, Document document) throws IllegalAccessException, IOException, SAXException, ParserConfigurationException {

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
				if (Converter.NOT_XML.equals(xml)) {
					// odd, we return nothing from the converter? OK.
				} else {
					log.debug("adding xml from converter to {}", element.getTagName());
					appendXmlChildrenTo(element, xml);
				}
			} else {
				element.setTextContent(text);
			}
		}

		return element;

	}

	private void appendXmlChildrenTo(Element element, String xml) throws IOException, SAXException, ParserConfigurationException {
		final Node node = stringAsNode(xml);
		final NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			final Node child = childNodes.item(i);
//			element.getOwnerDocument().importNode(child, true);
			element.appendChild(element.getOwnerDocument().importNode(child, true));
		}
	}

	private Node stringAsNode(String xml) throws ParserConfigurationException, IOException, SAXException {
		// we wrap this, because we can get a list of items from the converter
		// in that case, we'll add all of the child nodes to the parent
		String wellFormedXml = "<x>" + xml + "</x>";
		log.debug("Making {} into an xml node", wellFormedXml);
		return documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(wellFormedXml.getBytes())).getDocumentElement();
	}

	private String getElementName(Object object) {
		return namingStrategy.getElementName(object);
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

	public void addAlias(String alias, Class<?> type) {
		aliasMap.put(alias, type);
	}

	public Class typeForElement(String nextPropertyName) {

		final Class nextType;

		if (aliasMap.containsKey(nextPropertyName)) {
			nextType = aliasMap.get(nextPropertyName);
		} else {
			nextType = String.class;
		}

		return nextType;

	}

}
