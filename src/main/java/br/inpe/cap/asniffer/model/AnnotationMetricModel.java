package br.inpe.cap.asniffer.model;

import java.util.HashMap;
import java.util.Map;

public class AnnotationMetricModel {

	private String annotationName;
	private int sourceCodeLine;
	private String schema;

	private Map<String, Integer> annotationMetrics = new HashMap<String, Integer>();
	
	public AnnotationMetricModel(String annotationName, int sourceCodeLine, String schema) {
		this.annotationName = annotationName;
		this.sourceCodeLine = sourceCodeLine;
		this.schema = schema;
	}
	
	public void addAnnotationMetric(String metricName, int metricValue) {
		annotationMetrics.put(metricName, metricValue);
	}
	
	public Map<String, Integer> getAnnotationMetrics() {
		return annotationMetrics;
	}

	public String getName() {
		return this.annotationName;
	}

	public int getLine() {
		return this.sourceCodeLine;
	}
	
	public String getSchema() {
		return schema;
	}
}