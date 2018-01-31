package br.inpe.cap.asniffer;

import java.util.HashMap;
import java.util.Map;

public class Metric {
	
	private String sourceFilePath;
	private String className;
	private String type;
	
	private ClassMetric classMetric;
	private Map<String, ElementMetric> elementMetric;
	
	//JAXB requires an empty constructor
	public Metric() {
		
	}
	
	public Metric(String sourceFilePath, String className, String type) {
		super();
		this.sourceFilePath = sourceFilePath;
		this.className = className;
		this.type = type;
		this.classMetric = new ClassMetric();
		this.elementMetric = new HashMap<>();
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSourceFilePath() {
		return sourceFilePath;
	}
	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	//public void addElementMetric(String elementName)
	
	public int getClassMetric(String metricName) {
		return classMetric.getMetricValue(metricName);
	}
	public void addClassMetric(String metricName, int metricValue) {
		this.classMetric.addMetricValue(metricName, metricValue); 
	}
}
