package br.inpe.cap.asniffer.output.json.d3hierarchy.packageview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections4.map.HashedMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.d3hierarchy.systemview.PackageContentSV;

public class JSONReportPV implements IReport {

	
	ProjectReportPV projectReportJson;
	@Override
	public void generateReport(AMReport report, String path) {
		
		
		projectReportJson = prepareJson(report);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Path jsonFilePath = Paths.get(path + File.separator + report.getProjectName() + ".json").normalize();
		
		String json = gson.toJson(projectReportJson);
		
		try {
			FileWriter writer = new FileWriter(jsonFilePath.toString());
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private ProjectReportPV prepareJson(AMReport report) {
	
		ProjectReportPV projectReport = new ProjectReportPV(report.getProjectName());
		
		projectReport.addPackages(fetchPackages(report.getPackages()));
		
		return projectReport;
		
	}
	
	
	public List<Children> fetchPackages(List<PackageModel> packages) {
		
		List<Children> packages_ = new ArrayList<Children>();
		Stack<Children> childrenStack = new Stack<Children>();
		
		//Ordering package models
		List<PackageModel> orderedPackModel = new ArrayList<PackageModel>(packages);
		Collections.sort(orderedPackModel);
		
		
		String rootPackageName = null;
		Children rootPackage = null, topStackPackage = null;
		String previousPackageName = null;
		//Define root packages
		for(PackageModel packageModel : orderedPackModel) {
			
			Children packageContent = 
					new Children(packageModel.getPackageName(), "package", null);
			
			//Add classes to packages
			packageContent.addAllChidren(fetchClassReport(packageModel));
			
			if(childrenStack.isEmpty()) {
				rootPackage = 
						new Children(packageModel.getParentPackageName(), "package", null);
				if(!packageModel.getPackageName().equals(packageModel.getParentPackageName()))
					rootPackage.addChildren(packageContent);
				childrenStack.push(rootPackage);
				rootPackageName = rootPackage.getName();
				packages_.add(packageContent);
				topStackPackage = rootPackage;
			}else {
				if(topStackPackage.getName().equals(packageModel.getParentPackageName())) {
					topStackPackage.addChildren(packageContent);
				}else if(packageModel.getParentPackageName().equals(previousPackageName)) {
					topStackPackage = topStackPackage.getChildByName(previousPackageName);
					childrenStack.push(topStackPackage);
					topStackPackage.addChildren(packageContent);
				} else if(packageModel.getPackageName().contains(rootPackageName)) {
					while(!topStackPackage.getName().equals(packageModel.getParentPackageName())) {
						
						if(topStackPackage.getName().length() < packageModel.getParentPackageName().length()) {
							Children temp = new Children(packageModel.getParentPackageName(), "package", null);
							//temp.addPackageChildren(packageContent);
							childrenStack.push(topStackPackage);
							childrenStack.push(temp);
							topStackPackage.addChildren(temp);
						}
						topStackPackage = childrenStack.pop();
					}
					topStackPackage.addChildren(packageContent);
					childrenStack.push(topStackPackage);
				} else {
					rootPackage = 
							new Children(packageModel.getParentPackageName(), "package", null);
					if(!packageModel.getPackageName().equals(packageModel.getParentPackageName()))
						rootPackage.addChildren(packageContent);
					childrenStack.push(rootPackage);
					childrenStack.push(packageContent);
					rootPackageName = rootPackage.getName();
					packages_.add(rootPackage);
					topStackPackage = childrenStack.peek();
				}
			}
			
			previousPackageName = packageModel.getPackageName();
		}
		
		return packages_;
		
	}
	
	
	private List<Children> fetchClassReport(PackageModel package_) {
		
		List<Children> classes_ = new ArrayList<Children>();
		
		for (ClassModel classReport : package_.getResults()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			
			Children classZ = new Children
					(classReport.getClassName(), classReport.getType(), null);
			classZ.addAllChidren(fetchAnnotations(classReport));
			classes_.add(classZ);
		}
		
		return classes_;
		
	}
	private List<Children> fetchAnnotations(ClassModel classReport) {
		
		List<Children> classes_ = new ArrayList<Children>();
		
		for (CodeElementModel codeElements : classReport.getElementsReport()) {
			for (AnnotationMetricModel annotation : codeElements.getAnnotationMetrics()) {
				//Considering AA
				Children children = new Children
						(annotation.getName(), 
						 "annotation", 
						 annotation.getAnnotationMetrics().get("AA"));
				children.addProperty("schema", annotation.getSchema());
				classes_.add(children);
			}
		}
		
		return classes_;
	}

}
