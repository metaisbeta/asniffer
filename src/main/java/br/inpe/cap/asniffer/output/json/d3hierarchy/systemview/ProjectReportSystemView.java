package br.inpe.cap.asniffer.output.json.d3hierarchy.systemview;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.model.PackageModel;

public class ProjectReportSystemView {

	
	@SerializedName(value = "name")
	private String projectName;

	@SerializedName(value = "children")
	private List<PackageContentSV> packagesJSON;
	
	public ProjectReportSystemView(String projectName) {
		this.projectName = projectName;
		this.packagesJSON = new ArrayList<PackageContentSV>();
	}

	public void addPackageJSON(PackageContentSV packageJSON) {
		this.packagesJSON.add(packageJSON);
	}

	public void addPackages(List<PackageContentSV> packages) {
		this.packagesJSON = packages;
		
	}
	
}