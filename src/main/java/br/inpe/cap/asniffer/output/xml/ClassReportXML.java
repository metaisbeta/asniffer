package br.inpe.cap.asniffer.output.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ClassReportXML {

	@XmlAttribute(name = "name")
	private String className;
	
	@XmlAttribute(name = "type")
	private String type;

	@XmlElement(name = "metric")
	private List<ClassMetricXML> classMetricXML;
	
	@XmlElementWrapper(name = "code-elements")
	@XmlElement(name = "code-element")
	private List<CodeElementXMLReport> codeElementXMLReport;
	
	public ClassReportXML(String className, String type) {
		this.className = className;
		this.type = type;
	}
	
	public void setClassMetricsXML(List<ClassMetricXML> classMetricXML) {
		this.classMetricXML = classMetricXML;
	}
	
	public void setCodeElementXMLReport(List<CodeElementXMLReport> codeElementXMLReport) {
		this.codeElementXMLReport = codeElementXMLReport;
	}
	
}
