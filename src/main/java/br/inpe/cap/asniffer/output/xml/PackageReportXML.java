package br.inpe.cap.asniffer.output.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PackageReportXML {

	@XmlAttribute(name = "name")
	private String packageName;
	
	@XmlElement(name = "class")
	private List<ClassReportXML> classReportXML;
	
	public PackageReportXML(String packageName) {
		this.packageName = packageName;
		this.classReportXML = new ArrayList<ClassReportXML>();
	}
	
	public void setClassReportXML(List<ClassReportXML> classReportXML) {
		this.classReportXML = classReportXML;
	}
}
