package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ElementMetricColumn {

	@XmlAttribute(name = "name")
    public String element;
   
	@XmlElement(name = "element")
    public List<ElementMetricValues> metricValues = new ArrayList<>();
}
