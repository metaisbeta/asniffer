package br.inpe.cap.asniffer.output;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class PackagePojo {

	String packageName;
	List<ClassPojo> classes;
	
	public PackagePojo() {
		
	}
	
	public PackagePojo(String packageName, List<ClassPojo> classes) {
		this.packageName = packageName;
		this.classes = classes;
	}
	
	@XmlAttribute(name = "name")
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	@XmlElement(name = "class")
	public void setClasses(List<ClassPojo> classes) {
		this.classes = classes;
	}
	
	public List<ClassPojo> getClasses() {
		return classes;
	}
}
