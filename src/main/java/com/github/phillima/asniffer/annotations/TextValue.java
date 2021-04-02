package com.github.phillima.asniffer.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import net.sf.esfinge.metadata.annotation.validator.field.ValidFieldTypes;

@Retention(RUNTIME)
@ValidFieldTypes(listValidTypes = {String.class})
public @interface TextValue {
	String name();
}
