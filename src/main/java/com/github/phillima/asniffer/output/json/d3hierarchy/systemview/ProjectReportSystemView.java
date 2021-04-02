package com.github.phillima.asniffer.output.json.d3hierarchy.systemview;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import com.github.phillima.asniffer.output.json.d3hierarchy.Children;

public class ProjectReportSystemView {

	
	@SerializedName(value = "name")
	private String projectName;

	@SerializedName(value = "children")
	private List<Children> packagesJSON;
	
	public ProjectReportSystemView(String projectName) {
		this.projectName = projectName;
		this.packagesJSON = new ArrayList<Children>();
	}

	public void addPackageJSON(Children packageJSON) {
		this.packagesJSON.add(packageJSON);
	}

	public void addPackages(List<Children> packages) {
		this.packagesJSON = packages;
	}
	
}