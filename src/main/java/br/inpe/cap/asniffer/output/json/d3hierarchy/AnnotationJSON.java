package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AnnotationJSON {
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("children")
	private List<AnnotationMetricJSON> annotMetricJSON;

	public AnnotationJSON(String name) {
		this.name = name;
		annotMetricJSON = new ArrayList<AnnotationMetricJSON>();
	}
	
	//Behaviors
	public void addAnnotMetricJSON(AnnotationMetricJSON annotMetricJSON) {
		this.annotMetricJSON.add(annotMetricJSON);
	}
}
