package com.github.phillima.asniffer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassModel {
	
	private String sourceFilePath;
	private String className;
	private String type;
	
	private HashMap<String,String> annotSchemasMap;//simple name + code line, fully qualified name 
	
	private Map<String,Integer> classMetric;
	
	private List<CodeElementModel> elementsReport;
	
	public ClassModel(String sourceFilePath, String className, String type, int loc, int nec) {
		super();
		this.sourceFilePath = sourceFilePath;
		this.className = className;
		this.type = type;
		this.classMetric = new HashMap<>();
		this.elementsReport = new ArrayList<CodeElementModel>();
		addClassMetric("LOC", loc);
		addClassMetric("NEC", nec);
		this.annotSchemasMap = new HashMap<>();
	}
	
	public int getClassMetric(String metricName) {
		if(classMetric.containsKey(metricName))
			return classMetric.get(metricName);
		return -1;
	}
	
	public void addClassMetric(String metricName, int metricValue) {
		this.classMetric.put(metricName, metricValue);
	}
	
	public void addAnnotationSchema(String fullyqualifiedName, String simpleName) {
		annotSchemasMap.put(fullyqualifiedName, simpleName);
	}
	
	public CodeElementModel getElementReport(String elementName) {

		return elementsReport.stream()
				.filter(e -> e.getElementName().equals(elementName))
				.findFirst()
				.get();
	}
	
	public CodeElementModel getElementReport(String elementName, String elementType) {

		return elementsReport.stream()
				.filter(e -> e.getElementName().equals(elementName))
				.filter(e -> e.getType().equals(elementType))
				.findFirst()
				.get();
	}

	public void addElementReport(CodeElementModel elementReport) {
		this.elementsReport.add(elementReport);
	}
	
	public String getAnnotationSchema(String annotationName) {
		if(annotSchemasMap.containsKey(annotationName))
			return annotSchemasMap.get(annotationName);
		else
			return null;
	}
	
	//GETTERS and SETTERS
	public String getSimpleName() {
		int lastIndex = className.lastIndexOf(".");
		if(lastIndex!=-1)
			return className.substring(lastIndex+1);
		return className;
	}
	public String getFullyQualifiedName() {
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
	public List<CodeElementModel> getElementsReport(){
		return elementsReport;
	}
	public Map<String, Integer> getAllClassMetrics() {
		return classMetric;
	}
	
	public Set<String> getAnnotationSchemas() {
		Set<String> schemas = new HashSet<>();
		this.annotSchemasMap.forEach((k,v) -> {
			schemas.add(v);
		});
		return schemas;
	}
	
	public HashMap<String, String> getAnnotationSchemasMap() {
		return annotSchemasMap;
	}
	public void setSchemas(HashMap<String, String> annotationSchemas) {
		this.annotSchemasMap = annotationSchemas;
	}
	
}