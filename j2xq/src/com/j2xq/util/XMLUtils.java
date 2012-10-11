package com.j2xq.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLUtils {
	/**
	 * Converts a W3C DOM Document to String
	 * @param xml The W3C DOM Document object
	 * @return The serialized form of the DOM object 
	 * @throws TransformerException
	 */
	public static String toString(Document xml) throws TransformerException{
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(xml),new StreamResult(buffer));
		
		return buffer.toString();
	}
	
	/**
	 * Converts a serialized XML from String to W3C DOM Document
	 * @param xml The serialized form of XML
	 * @return The W3C DOM Document object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document fromString(String xml) throws ParserConfigurationException, SAXException, IOException {		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    return builder.parse(new ByteArrayInputStream(xml.getBytes()));		
	}
}
