package br.inpe.cap.asniffer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class PackageModel implements Comparable<PackageModel> {
	
	private String packageName;
	
	private List<ClassModel> results;
	
	public PackageModel(String packageName) {
		this.packageName = packageName;
		this.results = new ArrayList<ClassModel>();
	}
	
	public void addClassModel(ClassModel metric) {
		results.add(metric);
	}

	public ClassModel getClassModel(String name) {
		for (ClassModel classModel : results) {
			if(classModel.getFullyQualifiedName().equals(name))
				return classModel;
		}
		return null;
	}

	public List<ClassModel> getResults() {
		return results;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
	
	@Override
	public int compareTo(PackageModel packageModel) {
		return this.packageName.compareTo(packageModel.getPackageName());
	}
}
