package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AnnotationJSOND3 {
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("children")
	private List<AnnotationMetricJSOND3> annotMetricJSON;

	public AnnotationJSOND3(String name) {
		this.name = name;
		annotMetricJSON = new ArrayList<AnnotationMetricJSOND3>();
	}
	
	//Behaviors
	public void addAnnotMetricJSON(AnnotationMetricJSOND3 annotMetricJSON) {
		this.annotMetricJSON.add(annotMetricJSON);
	}
}
