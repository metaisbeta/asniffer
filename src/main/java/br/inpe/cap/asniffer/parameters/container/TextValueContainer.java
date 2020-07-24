package br.inpe.cap.asniffer.parameters.container;

import br.inpe.cap.asniffer.annotations.Mandatory;
import br.inpe.cap.asniffer.annotations.TextValue;
import net.sf.esfinge.metadata.annotation.container.AnnotationProperty;
import net.sf.esfinge.metadata.annotation.container.ContainerFor;
import net.sf.esfinge.metadata.annotation.container.ContainsAnnotation;
import net.sf.esfinge.metadata.annotation.container.ElementName;
import net.sf.esfinge.metadata.container.ContainerTarget;

@ContainerFor(ContainerTarget.FIELDS)
public class TextValueContainer {

	@ElementName
	private String name;
	
	@AnnotationProperty(annotation = TextValue.class, property = "name")
	private String value;
	
	@ContainsAnnotation(Mandatory.class)
	private boolean mandatory;
	
	//Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
}
