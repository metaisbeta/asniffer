package br.inpe.cap.asniffer.output;

import javax.xml.bind.annotation.XmlAttribute;

public class SimpleMetric {

	String metricName;
	int metricValue;

	public SimpleMetric(String metricName, int metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

	public String getMetricName() {
		return metricName;
	}

	@XmlAttribute(name = "name")
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public int getMetricValue() {
		return metricValue;
	}

	@XmlAttribute(name = "value")
	public void setMetricValue(int metricValue) {
		this.metricValue = metricValue;
	}
	
}
