package br.inpe.cap.asniffer.output;

public class MetricOutputRepresentation {

	private double metricValue;
	private String name, alias, package_, className;
	private boolean multiMetric = false;
	private String type, elementName;

	public MetricOutputRepresentation(String package_, String className, String alias, String name, int metricValue, 
			boolean multiMetric, String elementName, String type) {
		this.alias = alias;
		this.name = name;
		this.metricValue = metricValue;
		this.className = className;
		this.package_ = package_;
		this.multiMetric = multiMetric;
		this.elementName = elementName;
		this.type = type;
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

	public void setType(String type) {
		this.type = type;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	

}
