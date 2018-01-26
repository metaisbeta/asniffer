package br.inpe.cap.asniffer.output;

import javax.xml.bind.annotation.XmlAttribute;

//Class used for multimetric
public class MetricsValues {

	private String name;
	private int value;
	private String type;
	
	public MetricsValues() {
		
	}
	
	public MetricsValues(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public MetricsValues(String name, int value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	@XmlAttribute(name = "value")
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	@XmlAttribute(required = false)
	public void setType(String type) {
		this.type = type;
	}
	
	
}
