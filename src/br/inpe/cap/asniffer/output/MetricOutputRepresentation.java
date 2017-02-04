package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

public class MetricOutputRepresentation {
	
	private List<PackageRepresentation> packages_;
	private String projectName;
	
	public MetricOutputRepresentation(List<PackageRepresentation> packages_, String projectName){
		
		this.packages_ = new ArrayList<>(packages_);
		this.projectName = projectName;
	}
	
	public List<PackageRepresentation> getPackages_() {
		return packages_;
	}
	public void setPackages_(List<PackageRepresentation> packages_) {
		this.packages_ = packages_;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
