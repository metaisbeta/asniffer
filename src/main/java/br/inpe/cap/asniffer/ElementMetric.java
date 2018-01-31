package br.inpe.cap.asniffer;

import java.util.HashMap;
import java.util.Map;

public class ElementMetric {
	
	private Map<String,Integer> metricValue;

	public ElementMetric() {
		metricValue = new HashMap<>();
	}
	
	public Map<String, Integer> getMetricValue() {
		return this.metricValue;
	}

	public void addMetricValue(Map<String, Integer> metricValue) {
		this.metricValue = metricValue;
	}

}
