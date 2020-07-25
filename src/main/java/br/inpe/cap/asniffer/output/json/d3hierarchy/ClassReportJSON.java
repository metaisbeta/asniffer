package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.annotations.ExcludeSerialisation;

public class ClassReportJSON {

	@SerializedName("name")
	private String className;
	
	@ExcludeSerialisation
	private int ac;
	
	//@SerializedName("children")
	//private List<ClassMetricJSON> classMetricsJSON;
	
	@SerializedName("children")
	private List<CodeElementJSON> codeElementsJSON;
	
	public ClassReportJSON(String className, String type, int ac) {
		this.className = (className + " - " + type);
		this.ac = ac;
	}
	
	public void setClassMetricsJSON(List<ClassMetricJSON> classMetricsJSON) {
	//	this.classMetricsJSON = classMetricsJSON;
	}
	
	//Getters and Setters
	public void setCodeElementsJSON(List<CodeElementJSON> codeElementsJSON) {
		this.codeElementsJSON = codeElementsJSON;
	}
	public List<CodeElementJSON> getCodeElementsJSON() {
		return codeElementsJSON;
	}
	public int getAc() {
		return this.ac;
	}

}