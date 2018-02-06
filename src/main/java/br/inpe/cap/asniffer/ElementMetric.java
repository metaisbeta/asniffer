package br.inpe.cap.asniffer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (propOrder={"elementName", "type", "sourceCodeLine"})
public class ElementMetric {
	
	@XmlValue
	private Integer metricValue;
	@XmlAttribute(required = false)
	private String type;
	@XmlAttribute(name = "code-line")
	private int sourceCodeLine;
	@XmlAttribute(name = "name")
	private String elementName;
	
	//JAXB
	public ElementMetric() {	}
	
	public ElementMetric(Integer metricValue, String type, int line, String elementName) {
		this.metricValue = metricValue;
		this.type = type;
		this.sourceCodeLine = line;
		this.elementName = elementName;
	}
	public Integer getMetricValue() {
		return metricValue;
	}
	public String getType() {
		return type;
	}
	public int getLine() {
		return sourceCodeLine;
	}
	public String getElementName() {
		return elementName;
	}

}
