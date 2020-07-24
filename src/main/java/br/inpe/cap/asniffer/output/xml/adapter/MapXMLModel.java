package br.inpe.cap.asniffer.output.xml.adapter;

import javax.xml.bind.annotation.XmlAttribute;

public class MapXMLModel {
   
	@XmlAttribute(name = "metric")
    private String metricName;
	
	@XmlAttribute(name = "value")
    private Integer metricValue;

	//Required for JAXB
	public MapXMLModel() {}
	
	public MapXMLModel(String metricName, Integer metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public void setMetricValue(Integer value) {
		this.metricValue = value;
	}
}
