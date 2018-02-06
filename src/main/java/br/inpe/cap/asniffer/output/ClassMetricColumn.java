package br.inpe.cap.asniffer.output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ClassMetricColumn {
   
	@XmlAttribute(name = "name")
    public String metricName;
	
	@XmlValue
    public Integer value;
    
}
