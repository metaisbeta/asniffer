package br.inpe.cap.asniffer.output.json.d3hierarchy.systemview;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.inpe.cap.asniffer.annotations.ExcludeSerialisation;

public class PackageContentSV {

	//Package or Schema name
	private String name;
	//Package or type
	private String type;
	//If is a schema this will be the number of annotations/
	//If is a package it will be null
	private Integer size = null;
	
	@SerializedName("children")
	//In case package contain other packages
	private List<PackageContentSV> packageChildrens;
	
	public PackageContentSV(String childName, String type, Integer size) {
		this.name = childName;
		this.type = type;
		this.size = size;
		this.packageChildrens = new ArrayList<PackageContentSV>();
	}
	
	public List<PackageContentSV> getPackageChildrens() {
		return packageChildrens;
	}
	
	public String getName() {
		return name;
	}
	
	public void addAllPackageChildren(List<PackageContentSV> packageChildren) {
		this.packageChildrens.addAll(packageChildren);
	}
	
	public PackageContentSV getPackageContentByName(String name) {
		for (PackageContentSV packageContent : packageChildrens) {
			if(packageContent.getName().equals(name))
				return packageContent;
		}	
		return null;
	}
	
	public void addPackageChildren(PackageContentSV packageContent) {
		this.packageChildrens.add(packageContent);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PackageContentSV) {
			PackageContentSV temp = (PackageContentSV) obj;
			return temp.name.equals(this.name);
		}
		return super.equals(obj);
	}
	
	
}
