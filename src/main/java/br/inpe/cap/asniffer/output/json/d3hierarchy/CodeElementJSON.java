package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.annotations.ExcludeSerialisation;

public class CodeElementJSON {
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("children")
	private List<AnnotationJSON> annotationJSON;
	
	@ExcludeSerialisation
	private int aed;

	public CodeElementJSON(String name, String type, int aed) {
		this.name = (name + " - " + type);
		annotationJSON = new ArrayList<AnnotationJSON>();
		this.aed = aed;
	}
	//Getters and Setters
	public void setAnnotationJSON(List<AnnotationJSON> annotationJSON) {
		this.annotationJSON = annotationJSON;
	}
	
	public int getAed() {
		return aed;
	}
	
	public List<AnnotationJSON> getAnnotationJSON() {
		return annotationJSON;
	}
	
}