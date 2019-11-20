package br.inpe.cap.asniffer.output.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class CodeElementXMLReport {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String type;
	@XmlAttribute(name = "code-line")
	private int codeLine;
	@XmlAttribute
	private int aed;

	@XmlElement(name = "annotation")
	private List<AnnotationXMLReport> annotationXMlReport;
	
	public CodeElementXMLReport(String name, String type, int codeLine, int aed) {
		this.aed = aed;
		this.codeLine = codeLine;
		this.name = name;
		this.type = type;
		this.annotationXMlReport = new ArrayList<AnnotationXMLReport>();
	}
	
	public void setAnnotationXMlReport(List<AnnotationXMLReport> annotationXMlReport) {
		this.annotationXMlReport = annotationXMlReport;
	}
	
}
