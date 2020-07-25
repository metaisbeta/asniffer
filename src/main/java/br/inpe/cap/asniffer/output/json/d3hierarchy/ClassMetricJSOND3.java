package br.inpe.cap.asniffer.output.json.d3hierarchy;

import com.google.gson.annotations.SerializedName;

public class ClassMetricJSOND3 {
	
	@SerializedName("name")
	private String metricName;
	
	@SerializedName("size")
	private int metricValue;
	
	public ClassMetricJSOND3(String metricName, int metricValue) {
		this.metricName = metricName;
		this.metricValue = metricValue;
	}

}
