package br.inpe.cap.asniffer.output.json.d3hierarchy.classview;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.output.json.d3hierarchy.Children;

public class ProjectReportCV {
	
	@SerializedName(value = "name")
	private String projectName;

	@SerializedName(value = "children")
	private List<Children> packagesJSON;
	
	public ProjectReportCV(String projectName) {
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
