package br.inpe.cap.asniffer.model;

import java.util.ArrayList;
import java.util.List;

public class CodeElementModel {
	
	private String type;
	private int sourceCodeLine;
	private String elementName;
	private int aedValue;
	
	private List<AnnotationMetricModel> annotationMetrics = new ArrayList<AnnotationMetricModel>();
	
	public CodeElementModel(String elementName, String type, int sourceCodeLine) {
		this.type = type;
		this.sourceCodeLine = sourceCodeLine;
		this.elementName = elementName;
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
	
	public void setAed(int aedValue) {
		this.aedValue = aedValue;
	}
	
	public int getAed() {
		return this.aedValue;
	}
	
	public void addAnnotationMetric(AnnotationMetricModel annotationMetric) {
		this.annotationMetrics.add(annotationMetric);
	}
	
	public List<AnnotationMetricModel> getAnnotationMetrics() {
		return annotationMetrics;
	}
	
}
