package br.inpe.cap.asniffer.output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ElementMetricValues {
	
	@XmlAttribute(name = "name")
	public String element;
	
	@XmlValue
	public int value;

}
