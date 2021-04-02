package com.github.phillima.asniffer.output.json.adapter;

import com.github.phillima.asniffer.annotations.ExcludeSerialisation;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExcludeFieldsJSON implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(ExcludeSerialisation.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
