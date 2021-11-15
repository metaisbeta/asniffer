package com.github.phillima.asniffer.model;

import java.util.ArrayList;
import java.util.List;


public class AMReport {

	private String projectName;

	private List<PackageModel> packages;

	public AMReport(String projectName) {
		this.projectName = projectName;
		this.packages = new ArrayList<>();
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void addPackageModel(PackageModel packageModel) {
		int index = this.packages.indexOf(packageModel);
		if (index >= 0)
			this.packages.set(index, packageModel);
		else
			this.packages.add(packageModel);
	}

	public List<PackageModel> getPackages() {
		return packages;
	}

	public PackageModel getPackageByName(String packageName) {
		return packages.stream()
				.filter(pkg -> pkg.getPackageName().equals(packageName))
				.findFirst()
				.orElse(null);
	}
}
