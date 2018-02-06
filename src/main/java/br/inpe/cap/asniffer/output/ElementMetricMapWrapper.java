package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ElementMetricMapWrapper {

	@XmlElement(name = "metric")
	List<ElementMetricMap> elementMetricMaps;
	
	public ElementMetricMapWrapper() {this.elementMetricMaps = new ArrayList<>();}
}
