package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import br.inpe.cap.asniffer.ElementMetric;

@XmlAccessorType(XmlAccessType.FIELD)
public class ElementMetricMap {
   
	@XmlAttribute(name = "name")
	public String metricName;
	
	@XmlElement
	public List<ElementMetric> elements;
	
	public ElementMetricMap() {
		this.elements = new ArrayList<>();
	}

}