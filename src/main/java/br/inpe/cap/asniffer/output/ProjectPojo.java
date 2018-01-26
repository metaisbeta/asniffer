package br.inpe.cap.asniffer.output;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
public class ProjectPojo {

	String projectName;
	List<PackagePojo> packages_;
	
	public ProjectPojo(String projectName, List<PackagePojo> packages_) {
		this.projectName = projectName;
		this.packages_ = packages_;
	}
	
	public ProjectPojo() {
		
	}

	
	public String getProjectName() {
		return projectName;
	}
	
	@XmlAttribute(name = "name")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public List<PackagePojo> getPackages_() {
		return packages_;
	}
	
	@XmlElement(name = "package")
	public void setPackages_(List<PackagePojo> packages_) {
		this.packages_ = packages_;
	}
	
	
	
	
}
