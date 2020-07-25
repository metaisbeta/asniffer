package br.inpe.cap.asniffer.output.json.d3hierarchy;

import com.google.gson.annotations.SerializedName;

public class AnnotationMetricJSOND3 {
	
	@SerializedName("name")
	private String metricName;
	
	@SerializedName("value")
	private int metricValue;

	public AnnotationMetricJSOND3(String metricName, int metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

}
