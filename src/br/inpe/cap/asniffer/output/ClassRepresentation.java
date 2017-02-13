package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

public class ClassRepresentation {
	
	private List<MetricRepresentation> metricRepresentation;
	private String className;
	
	public List<MetricRepresentation> getMetricRepresentation() {
		return metricRepresentation;
	}

	public ClassRepresentation(List<MetricRepresentation> metricRepresentation, String className){
		this.className = className;
		this.metricRepresentation = new ArrayList<>(metricRepresentation);
	}
	
	public void setMetricRepresentation(List<MetricRepresentation> metricRepresentation) {
		this.metricRepresentation = metricRepresentation;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
