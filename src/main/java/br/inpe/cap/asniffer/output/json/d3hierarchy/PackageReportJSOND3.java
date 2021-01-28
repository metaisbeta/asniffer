package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PackageReportJSOND3 {

	@SerializedName(value = "name")
	private String packageName;
	
	@SerializedName("children")
	private List<ClassReportJSOND3> classReportJSON;

	public PackageReportJSOND3(String packageName) {
		this.packageName = packageName;
		this.classReportJSON = new ArrayList<ClassReportJSOND3>();
	}
	
	public void setClassReportJSON(List<ClassReportJSOND3> classReportJSON) {
		this.classReportJSON = classReportJSON;
	}
	
	public List<ClassReportJSOND3> getClassReportJSON() {
		return classReportJSON;
	}
	

}