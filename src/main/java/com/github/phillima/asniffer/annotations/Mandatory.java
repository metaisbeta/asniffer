package com.github.phillima.asniffer.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.sf.esfinge.metadata.annotation.validator.NeedsToHave;

@Retention(RUNTIME)
@Target(ElementType.FIELD)
@NeedsToHave(TextValue.class)
public @interface Mandatory {

}
