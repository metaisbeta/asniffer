package br.inpe.cap.asniffer.output.json.d3hierarchy;

import com.google.gson.annotations.SerializedName;

public class ClassMetricJSON {
	
	@SerializedName("name")
	private String metricName;
	
	@SerializedName("size")
	private int metricValue;
	
	public ClassMetricJSON(String metricName, int metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

}
