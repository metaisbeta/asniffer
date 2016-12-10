package br.inpe.cap.asniffer.output;

import org.eclipse.jdt.core.IJavaElement;

public class MetricOutputRepresentation {

	private double metricValue;
	private String name, alias, package_, className, elementName, type;
	private boolean multiMetric = false;

	public MetricOutputRepresentation(String alias, String name, int metricValue, 
			boolean multiMetric, String elementName, int type) {
		this.alias = alias;
		this.name = name;
		this.metricValue = metricValue;
		this.multiMetric = multiMetric;
		this.elementName = elementName;
		setType(type);
	}
	
	public MetricOutputRepresentation(String package_, String className, String alias, String name, int metricValue) {
		this.alias = alias;
		this.name = name;
		this.metricValue = metricValue;
		this.className = className;
		this.package_ = package_;
	}
	
	public MetricOutputRepresentation(String alias, String name, int metricValue) {
		this.alias = alias;
		this.name = name;
		this.metricValue = metricValue;
	}
	public double getMetricValue() {
		return metricValue;
	}
	public String getPackage_() {
		return package_;
	}
	public void setPackage_(String package_) {
		this.package_ = package_;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setMetricValue(double metricValue) {
		this.metricValue = metricValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public boolean isMultiMetric() {
		return multiMetric;
	}

	public void setMultiMetric(boolean multiMetric) {
		this.multiMetric = multiMetric;
	}
	
	public String getType() {
		return type;
	}

	public void setType(int type) {

		switch (type) {
		case IJavaElement.ANNOTATION:
			this.type = "Annotation";
			break;
		case IJavaElement.FIELD:
			this.type = "Field";
			break;
		case IJavaElement.METHOD:
			this.type = "Method";
			break;
		case IJavaElement.TYPE:
			this.type = "Class";
			break;
		case IJavaElement.LOCAL_VARIABLE:
			this.type = "Parameter";
			break;
		default:
			break;
		}
	
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	

}
