package br.inpe.cap.asniffer.output;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ClassPojo {
	
	String className;
	List<SimpleMetric> simpleMetric;
	List<MultiMetric> multiMetrics;
	
	
	public String getClassName() {
		return className;
	}
	
	@XmlAttribute(name = "name")
	public void setClassName(String className) {
		this.className = className;
	}

	public List<SimpleMetric> getSimpleMetric() {
		return simpleMetric;
	}

	@XmlElement(name = "metric")
	public void setSimpleMetric(List<SimpleMetric> simpleMetric) {
		this.simpleMetric = simpleMetric;
	}
	
	public List<MultiMetric> getMultiMetrics() {
		return multiMetrics;
	}

	@XmlElement(name = "metric")
	public void setMultiMetrics(List<MultiMetric> multiMetrics) {
		this.multiMetrics = multiMetrics;
	}
	
	public ClassPojo(String className, List<SimpleMetric> simpleMetric) {
		this.className = className;
		this.simpleMetric = simpleMetric;
	}
	
	public ClassPojo(String className, List<SimpleMetric> simpleMetric, List<MultiMetric> multiMetrics) {
		this.className = className;
		this.simpleMetric = simpleMetric;
		this.multiMetrics = multiMetrics;
	}
	
}
