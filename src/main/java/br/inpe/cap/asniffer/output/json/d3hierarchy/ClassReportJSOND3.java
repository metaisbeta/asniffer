package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.annotations.ExcludeSerialisation;

public class ClassReportJSOND3 {

	@SerializedName("name")
	private String className;
	
	@ExcludeSerialisation
	private int ac;
	
	//@SerializedName("children")
	//private List<ClassMetricJSON> classMetricsJSON;
	
	@SerializedName("children")
	private List<CodeElementJSOND3> codeElementsJSON;
	
	public ClassReportJSOND3(String className, String type, int ac) {
		this.className = (className + " - " + type);
		this.ac = ac;
	}
	
	public void setClassMetricsJSON(List<ClassMetricJSOND3> classMetricsJSON) {
	//	this.classMetricsJSON = classMetricsJSON;
	}
	
	//Getters and Setters
	public void setCodeElementsJSON(List<CodeElementJSOND3> codeElementsJSON) {
		this.codeElementsJSON = codeElementsJSON;
	}
	public List<CodeElementJSOND3> getCodeElementsJSON() {
		return codeElementsJSON;
	}
	public int getAc() {
		return this.ac;
	}

}