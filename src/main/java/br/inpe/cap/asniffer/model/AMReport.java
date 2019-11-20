package br.inpe.cap.asniffer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


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
		if(index >= 0)
			this.packages.set(index, packageModel);
		else
			this.packages.add(packageModel);
	}
	
	public List<PackageModel> getPackages() {
		return packages;
	}
	
}
