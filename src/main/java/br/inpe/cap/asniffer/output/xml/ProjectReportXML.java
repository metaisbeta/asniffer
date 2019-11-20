package br.inpe.cap.asniffer.output.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectReportXML {
	
	@XmlAttribute(name = "name")
	private String projectName;
	@XmlElement(name = "package")
	private List<PackageReportXML> packagesXML;
	
	public ProjectReportXML(String projectName) {
		this.projectName = projectName;
		packagesXML = new ArrayList<PackageReportXML>();
	}
	
	public void addPackageXML(PackageReportXML  packageXML) {
		this.packagesXML.add(packageXML);
	}
	
	//JAXB
	public ProjectReportXML() {	}
	
}
