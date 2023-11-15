package com.github.phillima.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.github.phillima.asniffer.output.json.d3hierarchy.Children;
import com.google.gson.annotations.SerializedName;

public class ProjectReport {
	
	@SerializedName("name")
	//@SerializedName(value = "name")
	private String projectName;

	@SerializedName("children")
	//@SerializedName(value = "children")
	private List<Children> packagesJSON;
	
	public ProjectReport(String projectName) {
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
