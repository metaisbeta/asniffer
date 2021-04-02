package com.github.phillima.asniffer.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.github.phillima.asniffer.exceptions.ReportTypeException;
import com.github.phillima.asniffer.model.PackageModel;
import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.output.json.d3hierarchy.Children;
import com.github.phillima.asniffer.output.json.d3hierarchy.IFetchChildren;

public class ReportTypeUtils {
	
	
	public static IReport getReportInstance(String reportType) {
		Object reportInstance = null;
		String classReport = "";
		try {
			classReport = PropertiesUtil.getReportType(reportType);
			Class<?> reportClazz = Class.forName(classReport);
			reportInstance = reportClazz.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		if(reportInstance instanceof IReport)
			return (IReport) reportInstance;
		else
			throw new ReportTypeException("Report Type class" + classReport + " does not implement IReport interface");
	}
	
	public static boolean isParentPackage(String rootPackageName, String currentPackageName) {
		
		String[] splitRootPackage = rootPackageName.split("\\.");
		String[] splitCurrentPackageName = currentPackageName.split("\\.");
		
		int rootPackageLen = splitRootPackage.length;
		int currentPackageLen = splitCurrentPackageName.length;
		
		if(rootPackageLen >= currentPackageLen)
			return false;
		
		for (int i = 0; i < rootPackageLen; i++) {
			if(!splitCurrentPackageName[i].equals(splitRootPackage[i]))
				return false;
		}
		return true;
	}
	
	public static List<Children> fetchPackages(List<PackageModel> packages, IFetchChildren fetchChildren) {

		List<Children> packageContents = new ArrayList<Children>();
		Stack<Children> packageContentStack = new Stack<Children>();
		
		//Ordering package models
		List<PackageModel> orderedPackModel = new ArrayList<PackageModel>(packages);
		Collections.sort(orderedPackModel);
		
		String rootPackageName = null;
		for(PackageModel packageModel : orderedPackModel) {
			Children packageContent = 
					new Children(packageModel.getPackageName(), "package", null);
			//Fetch schemas
			packageContent.addAllChidren(fetchChildren.fetchChildren(packageModel));
			
			if(packageContentStack.isEmpty()) {
				packageContentStack.push(packageContent);
				rootPackageName = packageContent.getName();
				packageContents.add(packageContent);
			}else {
				int previousPackageLvl = packageModel.getPackageName().lastIndexOf(".");
				if(previousPackageLvl == -1)
					previousPackageLvl = packageModel.getPackageName().length();
				String packageParentName = packageModel.getPackageName().substring(0, previousPackageLvl);
				
				if(rootPackageName.equals(packageParentName)) {//direct package child
					packageContentStack.peek().addChildren(packageContent);
				}else if(ReportTypeUtils.isParentPackage(rootPackageName, packageContent.getName())) {
					Children newRootPackage = packageContentStack.peek().getChildByName(packageParentName);
					if(newRootPackage != null) {
						rootPackageName = packageParentName;
						newRootPackage.addChildren(packageContent);
						packageContentStack.push(newRootPackage);
					}else {
						packageContentStack.peek().addChildren(packageContent);
					}
				}else {
					//pop stack until root package is found, or create new root if stack gets empty
					while(!packageContentStack.isEmpty()
						 && !ReportTypeUtils.isParentPackage(packageContentStack.peek().getName(), packageContent.getName())) {
						packageContentStack.pop();
					}
					if(packageContentStack.isEmpty()) {
						packageContentStack.push(packageContent);
						packageContents.add(packageContent);
						rootPackageName = packageContent.getName();
					}else {
						packageContentStack.peek().addChildren(packageContent);
						rootPackageName = packageContentStack.peek().getName();
					}
				}
			}
		}
		return packageContents;
	}

}
