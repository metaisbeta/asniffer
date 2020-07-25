package br.inpe.cap.asniffer.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PackageModel {
	
	private String packageName;
	
	private Map<String, ClassModel> results;
	
	public PackageModel(String packageName) {
		this.packageName = packageName;
		this.results = new HashMap<String, ClassModel>();
	}
	
	public void add(ClassModel metric) {
		results.put(metric.getClassName(), metric);
	}

	public ClassModel get(String name) {
		return results.get(name);
	}

	public Collection<ClassModel> all() {
		return results.values();
	}

	public ClassModel getByClassName(String name) {
		for (ClassModel metric : all()) {
			if (metric.getClassName().equals(name))
				return metric;
		}
		return null;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
}
