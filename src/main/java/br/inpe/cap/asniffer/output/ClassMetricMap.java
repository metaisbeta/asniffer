package br.inpe.cap.asniffer.output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ClassMetricMap {
   
	@XmlAttribute(name = "name")
    private String metricName;
	
	@XmlValue
    private Integer value;

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
