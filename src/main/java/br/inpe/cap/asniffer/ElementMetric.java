package br.inpe.cap.asniffer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import br.inpe.cap.asniffer.output.ElementMetricAdapter;
public class ElementMetric {
	
	@XmlElement
	@XmlJavaTypeAdapter(ElementMetricAdapter.class)
	private Map<String,Integer> metricValue;
	
	private String type;
	private String line;

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
