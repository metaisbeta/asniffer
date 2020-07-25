package br.inpe.cap.asniffer.output.json.adapter;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import br.inpe.cap.asniffer.annotations.ExcludeSerialisation;

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
