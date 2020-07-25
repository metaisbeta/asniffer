package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProjectReportJSON {
	
	@SerializedName(value = "name")
	private String projectName;

	@SerializedName(value = "children")
	private List<PackageReportJSON> packagesJSON;
	
	public ProjectReportJSON(String projectName) {
		this.projectName = projectName;
		this.packagesJSON = new ArrayList<PackageReportJSON>();
	}

	public void addPackageJSON(PackageReportJSON packageJSON) {
		this.packagesJSON.add(packageJSON);
	}
}
