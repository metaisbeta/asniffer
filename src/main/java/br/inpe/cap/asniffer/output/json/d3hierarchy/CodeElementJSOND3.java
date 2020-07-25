package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.annotations.ExcludeSerialisation;

public class CodeElementJSOND3 {
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("children")
	private List<AnnotationJSOND3> annotationJSON;
	
	@ExcludeSerialisation
	private int aed;

	public CodeElementJSOND3(String name, String type, int aed) {
		this.name = (name + " - " + type);
		annotationJSON = new ArrayList<AnnotationJSOND3>();
		this.aed = aed;
	}
	//Getters and Setters
	public void setAnnotationJSON(List<AnnotationJSOND3> annotationJSON) {
		this.annotationJSON = annotationJSON;
	}
	
	public int getAed() {
		return aed;
	}
	
	public List<AnnotationJSOND3> getAnnotationJSON() {
		return annotationJSON;
	}
	
}