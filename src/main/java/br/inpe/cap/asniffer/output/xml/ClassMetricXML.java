package br.inpe.cap.asniffer.output.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ClassMetricXML {

	@XmlAttribute(name = "name")
	private String metricName;
	
	@XmlAttribute(name = "value")
	private int metricValue;
	
	public ClassMetricXML(String metricName, int metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}
}