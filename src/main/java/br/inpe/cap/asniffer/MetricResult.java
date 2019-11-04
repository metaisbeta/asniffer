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

	@XmlAttribute(name = "loc")
	private int loc;
	
	@XmlAttribute(name = "nec")
	private int nec;
	
	@XmlElement(name = "class-metric")
	@XmlJavaTypeAdapter(ClassMetricAdapter.class)
	private Map<String,Integer> classMetric;
	
	@XmlElement(name = "elements")
	@XmlJavaTypeAdapter(ElementMetricAdapter.class)
	private Map<String, List<ElementMetric>> elementReport;
	
	@XmlElement(name = "element-metric")
	@XmlJavaTypeAdapter(ElementMetricAdapter.class)
	private Map<String, List<ElementMetric>> elementMetric;
	
	//JAXB requires an empty constructor
	public MetricResult() {
		
	}
	
	public MetricResult(String sourceFilePath, String className, String type, int loc) {
		super();
		this.sourceFilePath = sourceFilePath;
		this.className = className;
		this.type = type;
		this.loc = loc;
		this.classMetric = new HashMap<>();
		this.elementMetric = new HashMap<>();
		this.elementReport = new HashMap<>();
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
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public int getNec() {
		return nec;
	}
	public void setNec(int nec) {
		this.nec = nec;
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
	
	public List<ElementMetric> getElementReport(String elementName) {
		if(elementReport.containsKey(elementName))
			return elementReport.get(elementName);
		return null;
	}

	public void addElementReport(String elementName, List<ElementMetric> elementAnnotations) {
		elementReport.put(elementName,elementAnnotations);
	}
}
