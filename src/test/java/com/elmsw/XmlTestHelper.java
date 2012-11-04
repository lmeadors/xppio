package com.elmsw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class XmlTestHelper {

	private static final Logger log = LoggerFactory.getLogger(XmlTestHelper.class);

	private final XPathFactory xPathFactory;
	private final DocumentBuilder documentBuilder;
	private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

	public XmlTestHelper() {
		xPathFactory = XPathFactory.newInstance();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e.toString(), e);
		}
	}

	public String nodeToString(Node node) throws TransformerException {
		final Transformer transformer = transformerFactory.newTransformer();
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(new DOMSource(node), result);
		return writer.toString();
	}

	public Document stringToDocument(String source) throws Exception {
//		log.debug("creating document from : \n{}", source);
		return documentBuilder.parse(new ByteArrayInputStream(source.getBytes()));
	}

	public Node xPathQueryForNode(Node document, String query) throws Exception {
		return (Node) getStringAsXPathExpression(query).evaluate(document, XPathConstants.NODE);
	}

	public String xPathQuery(Node document, String query) throws Exception {
		return getStringAsXPathExpression(query).evaluate(document);
	}

	public NodeList xPathQueryForNodeList(Document document, String query) throws Exception {
		return (NodeList) getStringAsXPathExpression(query).evaluate(document, XPathConstants.NODESET);
	}

	private XPathExpression getStringAsXPathExpression(String query) throws XPathExpressionException {
		XPath xpath = xPathFactory.newXPath();
		return xpath.compile(query);
	}

	public String transformStringToString(Transformer transformer, String xml) throws TransformerException {
		final StreamSource source = new StreamSource(new StringReader(xml));
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		return writer.toString();
	}

	public Document transformSourceToDocument(Transformer transformer, Source source) throws Exception {
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		return stringToDocument(writer.toString());
	}

	public void assertEquality(Document actualDocument, Document expectedDocument, String query) throws Exception {
		assertEquals(
				xPathQuery(expectedDocument, query),
				xPathQuery(actualDocument, query)
		);
	}

}
