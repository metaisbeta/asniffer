package br.inpe.cap.asniffer.output.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.inpe.cap.asniffer.output.xml.adapter.MapXMLAdapter;

public class AnnotationXMLReport {

	@XmlAttribute
	private String name;
	@XmlAttribute(name = "code-line")
	private int codeLine;
	@XmlAttribute
	private String schema;
	
	@XmlElement(name = "annotation-metrics")
	@XmlJavaTypeAdapter(MapXMLAdapter.class)
	private Map<String, Integer> annotationMetrics;
	
		
	public AnnotationXMLReport(String name, int codeLine, String schema) {
		this.name = name;
		this.codeLine = codeLine;
		this.annotationMetrics = new HashMap<String, Integer>();
		this.schema = schema;
	}
	
	public void setAnnotationMetrics(Map<String, Integer> annotationMetrics) {
		this.annotationMetrics = annotationMetrics;
	}
}