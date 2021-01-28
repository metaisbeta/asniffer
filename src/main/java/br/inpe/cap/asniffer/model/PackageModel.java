package br.inpe.cap.asniffer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class PackageModel implements Comparable<PackageModel> {
	
	private String packageName;
	private String parentPackageName;
	
	private List<ClassModel> results;
	
	public PackageModel(String packageName) {
		this.packageName = packageName;
		this.results = new ArrayList<ClassModel>();
		int previouPackagePos = packageName.lastIndexOf(".");
		if(previouPackagePos!=-1) {
			parentPackageName = packageName.substring(0, previouPackagePos);
		}else {
			parentPackageName = packageName;
		}
	}
	
	public void addClassModel(ClassModel metric) {
		results.add(metric);
	}

	public ClassModel getClassModel(String name) {
		for (ClassModel classModel : results) {
			if(classModel.getClassName().equals(name))
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
	
	public String getParentPackageName() {
		return parentPackageName;
	}

	@Override
	public int compareTo(PackageModel packageModel) {
		return this.packageName.compareTo(packageModel.getPackageName());
	}
}
