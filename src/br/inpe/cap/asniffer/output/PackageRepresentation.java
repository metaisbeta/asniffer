package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;

public class PackageRepresentation {
	
	private String name = "";
	private List<ClassRepresentation> class_;
	
	public PackageRepresentation(String name, List<ClassRepresentation> class_){
		this.name = name;
		this.class_ = new ArrayList<ClassRepresentation>(class_);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ClassRepresentation> getMetrics() {
		return class_;
	}
	public void setMetrics(List<ClassRepresentation> class_) {
		this.class_ = class_;
	}

}
