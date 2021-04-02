package com.github.phillima.asniffer.parameters.container;

import com.github.phillima.asniffer.annotations.IsParameterPresent;
import net.sf.esfinge.metadata.annotation.container.AnnotationProperty;
import net.sf.esfinge.metadata.annotation.container.ContainerFor;
import net.sf.esfinge.metadata.annotation.container.ElementName;
import net.sf.esfinge.metadata.container.ContainerTarget;

@ContainerFor(ContainerTarget.FIELDS)
public class IsParameterPresentContainer {

	@ElementName
	private String name;
	
	@AnnotationProperty(annotation = IsParameterPresent.class, property = "name")
	private String value;
	
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
	
}
