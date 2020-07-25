package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProjectReportJSOND3 {
	
	@SerializedName(value = "name")
	private String projectName;

	@SerializedName(value = "children")
	private List<PackageReportJSOND3> packagesJSON;
	
	public ProjectReportJSOND3(String projectName) {
		this.projectName = projectName;
		this.packagesJSON = new ArrayList<PackageReportJSOND3>();
	}

	public void addPackageJSON(PackageReportJSOND3 packageJSON) {
		this.packagesJSON.add(packageJSON);
	}
}
