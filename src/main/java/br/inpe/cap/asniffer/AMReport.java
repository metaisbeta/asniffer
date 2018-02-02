package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class AMReport {
	
	@XmlTransient
	private Map<String, MetricResult> results;

	@XmlElementWrapper(name = "class")
	private List<MetricResult> xmlPreparedOutput;
	
	@XmlAttribute(name = "name")
	private String projectName;
	
	public AMReport(String projectName) {
		this.results = new HashMap<String, MetricResult>();
		this.projectName = projectName;
	}
	
	//Required for JAXB
	public AMReport() {
		
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public void add(MetricResult metric) {
		results.put(metric.getClassName(), metric);
	}

	public MetricResult get(String name) {
		return results.get(name);
	}

	public Collection<MetricResult> all() {
		return results.values();
	}

	public MetricResult getByClassName(String name) {
		for (MetricResult metric : all()) {
			if (metric.getClassName().equals(name))
				return metric;
		}
		return null;
	}
	
	public void preapareXMLFiles() {
		xmlPreparedOutput = new ArrayList<>();
		results.forEach((k,v)->{
			xmlPreparedOutput.add(v);
		});
	}
}
