package br.inpe.cap.asniffer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AMReport {

	private Map<String, Metric> results;

	public AMReport() {
		this.results = new HashMap<String, Metric>();
	}

	public void add(Metric metric) {
		results.put(metric.getClassName(), metric);
	}

	public Metric get(String name) {
		return results.get(name);
	}

	public Collection<Metric> all() {
		return results.values();
	}

	public Metric getByClassName(String name) {
		for (Metric metric : all()) {
			if (metric.getClassName().equals(name))
				return metric;
		}

		return null;
	}
	
}
