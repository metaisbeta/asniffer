package br.inpe.cap.asniffer.output.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.inpe.cap.asniffer.output.MapXMLAdapter;

public class AnnotationXMLReport {

	@XmlAttribute
	private String name;
	@XmlAttribute(name = "code-line")
	private int codeLine;
	
	@XmlElement(name = "annotation-metrics")
	@XmlJavaTypeAdapter(MapXMLAdapter.class)
	private Map<String, Integer> annotationMetrics;
	
	public AnnotationXMLReport(String name, int codeLine) {
		this.name = name;
		this.codeLine = codeLine;
		this.annotationMetrics = new HashMap<String, Integer>();
	}
	
	public void setAnnotationMetrics(Map<String, Integer> annotationMetrics) {
		this.annotationMetrics = annotationMetrics;
	}
}