package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PackageReportJSON {

	@SerializedName(value = "name")
	private String packageName;
	
	@SerializedName("children")
	private List<ClassReportJSON> classReportJSON;

	public PackageReportJSON(String packageName) {
		this.packageName = packageName;
		this.classReportJSON = new ArrayList<ClassReportJSON>();
	}
	
	public void setClassReportJSON(List<ClassReportJSON> classReportJSON) {
		this.classReportJSON = classReportJSON;
	}
	
	public List<ClassReportJSON> getClassReportJSON() {
		return classReportJSON;
	}
	
	
	

}
