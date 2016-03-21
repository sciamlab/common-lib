package com.sciamlab.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLLoader {

	private static final Logger logger = Logger.getLogger(XMLLoader.class);

	public static List<Element> load(String xml, String element_type) {
		List<Element> elements = new ArrayList<Element>();

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(xml));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList listOfNodes = doc.getElementsByTagName(element_type);

			for (int i = 0; i < listOfNodes.getLength(); i++) {
				Element e = (Element) listOfNodes.item(i);
				elements.add(e);
			}
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return elements;
	}

}