package br.inpe.cap.asniffer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class MetricResult {
	
	private String sourceFilePath;
	private String className;
	private String type;
	
	@XmlElement(name = "class-metric")
	//@XmlJavaTypeAdapter(MapXMLAdapter.class)
	private Map<String,Integer> classMetric;
	
	@XmlElementWrapper(name = "code-elements")
	@XmlElement(name = "code-element")
	private List<CodeElementModel> elementsReport;
	
	//JAXB requires an empty constructor
	public MetricResult() {}
	
	public MetricResult(String sourceFilePath, String className, String type, int loc) {
		super();
		this.sourceFilePath = sourceFilePath;
		this.className = className;
		this.type = type;
		this.classMetric = new HashMap<>();
		this.elementsReport = new ArrayList<CodeElementModel>();
		addClassMetric("LOC", loc);
	}
	
	public int getClassMetric(String metricName) {
		if(classMetric.containsKey(metricName))
			return classMetric.get(metricName);
		return -1;
	}
	
	public void addClassMetric(String metricName, int metricValue) {
		this.classMetric.put(metricName, metricValue);
	}
	
	public CodeElementModel getElementReport(String elementName) {
		for (CodeElementModel codeElement : elementsReport) {
			if(codeElement.getElementName().equals(elementName))
				return codeElement;
		}
		return null;
	}

	public void addElementReport(CodeElementModel elementReport) {
		this.elementsReport.add(elementReport);
	}
	
	//GETTERS and SETTERS
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
	public List<CodeElementModel> getElementsReport(){
		return elementsReport;
	}
	
	public Map<String, Integer> getAllClassMetrics() {
		return classMetric;
	}
}