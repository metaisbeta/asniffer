package br.inpe.cap.asniffer.output.json.d3hierarchy;

import com.google.gson.annotations.SerializedName;

public class AnnotationMetricJSON {
	
	@SerializedName("name")
	private String metricName;
	
	@SerializedName("value")
	private int metricValue;

	public AnnotationMetricJSON(String metricName, int metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

}
