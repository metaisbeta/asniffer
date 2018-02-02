package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ElementMetricMap {
   
	@XmlElement(name = "metric")
	public List<ElementMetricColumn> elements = new ArrayList<>();

}