package br.inpe.cap.asniffer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadConfigFile extends DefaultHandler {
	
	private List<String> metricsNames;
	File xmlFile;
	SAXParserFactory factory;
	SAXParser saxParser;
	boolean hasMetricTag = false;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase("metric"))
			hasMetricTag = true;
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (hasMetricTag) {
			metricsNames.add(new String(ch, start, length));
			hasMetricTag = false;
		}
	}
	
	public ReadConfigFile(String xmlFile) {
		this.xmlFile = new File(xmlFile);
        factory = SAXParserFactory.newInstance();
        metricsNames = new ArrayList<>();
        try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getMetrics(){
		try {
			saxParser.parse(xmlFile, this);
			return this.metricsNames;
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}