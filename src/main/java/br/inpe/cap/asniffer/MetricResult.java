package br.inpe.cap.asniffer;

import br.inpe.cap.asniffer.output.ClassMetricAdapter;
import br.inpe.cap.asniffer.output.ElementMetricAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class MetricResult {
	
	@XmlTransient
	private String sourceFilePath;
	@XmlAttribute(name = "name")
	private String className;
	@XmlTransient
	private String type;
	
	@XmlElement(name = "class-metric")
	@XmlJavaTypeAdapter(ClassMetricAdapter.class)
	private Map<String,Integer> classMetric;
	
	@XmlElement(name = "element-metric")
	@XmlJavaTypeAdapter(ElementMetricAdapter.class)
	private Map<String, List<ElementMetric>> elementMetric;
	
	//JAXB requires an empty constructor
	public MetricResult() {
		
	}
	
	public MetricResult(String sourceFilePath, String className, String type) {
		super();
		this.sourceFilePath = sourceFilePath;
		this.className = className;
		this.type = type;
		this.classMetric = new HashMap<>();
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
	
	public int getClassMetric(String metricName) {
		if(classMetric.containsKey(metricName))
			return classMetric.get(metricName);
		return -1;
	}
	
	public void addClassMetric(String metricName, int metricValue) {
		this.classMetric.put(metricName, metricValue);
	}
	
	public List<ElementMetric> getElementMetric(String metricName) {
		if(elementMetric.containsKey(metricName))
			return elementMetric.get(metricName);
		return null;
	}

	public void addElementMetric(String metricName, List<ElementMetric> metricValues) {
		elementMetric.put(metricName,metricValues);
	}
}
