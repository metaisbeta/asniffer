package br.inpe.cap.asniffer.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PackageModel {
	
	private String packageName;
	
	private Map<String, MetricResult> results;
	
	public PackageModel(String packageName) {
		this.packageName = packageName;
		this.results = new HashMap<String, MetricResult>();
	}
	
	public void add(MetricResult metric) {
		results.put(metric.getClassName(), metric);
	}

	public MetricResult get(String name) {
		return results.get(name);
	}

	public Collection<MetricResult> all() {
		return results.values();
	}

	public MetricResult getByClassName(String name) {
		for (MetricResult metric : all()) {
			if (metric.getClassName().equals(name))
				return metric;
		}
		return null;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
}
