package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

public class MetricRepresentation {

	private int singleMetricValue;
	private String name, alias;
	private boolean isMultiMetric = false;
	private List<String> elementName;
	private List<Integer> multiMetricValue;
	private List<String> elementType = new ArrayList<>();
	
	public MetricRepresentation(String alias, String name, List<Integer> multiMetricValue, 
			List<String> elementName, List<Integer> types) {
		this.alias = alias;
		this.name = name;
		this.multiMetricValue = new ArrayList<>(multiMetricValue);
		this.elementName = new ArrayList<>(elementName);
		setType(types);
		setMultiMetric(true);
	}
	
	public MetricRepresentation(String alias, String name, int singleMetricValue) {
		this.alias = alias;
		this.name = name;
		this.singleMetricValue = singleMetricValue;
	}
	public double getSingleMetricValue() {
		return singleMetricValue;
	}
	public void setSingleMetricValue(int singleMetricValue) {
		this.singleMetricValue = singleMetricValue;
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
	
	public List<String> getElementName() {
		return elementName;
	}

	public void setElementName(List<String> elementName) {
		this.elementName = elementName;
	}

	public List<Integer> getMultiMetricValue() {
		return multiMetricValue;
	}

	public void setMultiMetricValue(List<Integer> multiMetricValue) {
		this.multiMetricValue = multiMetricValue;
	}

	public List<String> getElementType() {
		return elementType;
	}

	public void setType(List<Integer> types) {
		this.elementType.clear();
		for(Integer type : types)
			switch (type) {
			case IJavaElement.ANNOTATION:
				this.elementType.add("Annotation");
				break;
			case IJavaElement.FIELD:
				this.elementType.add("Field");
				break;
			case IJavaElement.METHOD:
				this.elementType.add("Method");
				break;
			case IJavaElement.TYPE:
				this.elementType.add("Class");
				break;
			case IJavaElement.LOCAL_VARIABLE:
				this.elementType.add("Parameter");
				break;
			default:
				break;
			}
	}
	
	public boolean isMultiMetric() {
		return isMultiMetric;
	}

	public void setMultiMetric(boolean isMultiMetric) {
		this.isMultiMetric = isMultiMetric;
	}
	

}
