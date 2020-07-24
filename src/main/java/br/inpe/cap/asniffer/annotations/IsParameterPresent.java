package br.inpe.cap.asniffer.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import net.sf.esfinge.metadata.annotation.validator.field.ValidFieldTypes;

@Retention(RUNTIME)
@ValidFieldTypes(listValidTypes = {boolean.class, Boolean.class})
public @interface IsParameterPresent {
	String name();
}
