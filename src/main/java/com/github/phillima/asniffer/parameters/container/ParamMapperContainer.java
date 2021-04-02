package com.github.phillima.asniffer.parameters.container;

import java.util.List;

import com.github.phillima.asniffer.annotations.IsParameterPresent;
import com.github.phillima.asniffer.annotations.TextValue;
import net.sf.esfinge.metadata.annotation.container.AllFieldsWith;
import net.sf.esfinge.metadata.annotation.container.ContainerFor;
import net.sf.esfinge.metadata.annotation.container.ReflectionReference;
import net.sf.esfinge.metadata.container.ContainerTarget;

@ContainerFor(ContainerTarget.TYPE)
public class ParamMapperContainer {
	
	@ReflectionReference
	private Class<?> classToParameter;
	
	@AllFieldsWith(IsParameterPresent.class)
	private List<IsParameterPresentContainer> fieldsWithIsPresent;
	
	@AllFieldsWith(TextValue.class)
	private List<TextValueContainer> fieldsWithTextValue;
	
	public Class<?> getClassToParameter(){
		return classToParameter;
	}
	
	//Getters and Setters
	public void setClassToParameter(Class<?> className) {
		this.classToParameter = className;
	}
	public List<IsParameterPresentContainer> getFieldsWithIsPresent() {
		return fieldsWithIsPresent;
	}

	public void setFieldsWithIsPresent(List<IsParameterPresentContainer> fieldsWithIsPresent) {
		this.fieldsWithIsPresent = fieldsWithIsPresent;
	}

	public List<TextValueContainer> getFieldsWithTextValue() {
		return fieldsWithTextValue;
	}
	public void setFieldsWithTextValue(List<TextValueContainer> fieldsWithTextValue) {
		this.fieldsWithTextValue = fieldsWithTextValue;
	}
}
