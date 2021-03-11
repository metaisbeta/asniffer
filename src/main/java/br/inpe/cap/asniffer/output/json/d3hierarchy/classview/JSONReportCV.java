package br.inpe.cap.asniffer.output.json.d3hierarchy.classview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.d3hierarchy.packageview.Children;
import br.inpe.cap.asniffer.output.json.d3hierarchy.packageview.ProjectReportPV;

public class JSONReportCV implements IReport {
	
	private ProjectReportCV projectReport;

	@Override
	public void generateReport(AMReport report, String path) {
		
		projectReport = prepareJson(report);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Path jsonFilePath = Paths.get(path + File.separator + report.getProjectName() + ".json").normalize();
		
		String json = gson.toJson(projectReport);
		
		try {
			FileWriter writer = new FileWriter(jsonFilePath.toString());
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ProjectReportCV prepareJson(AMReport report) {
		
		ProjectReportCV projectReport = new ProjectReportCV(report.getProjectName());
		
		projectReport.addPackages(fetchPackages(report.getPackages()));
		
		return projectReport;
	}

	private List<Children> fetchPackages(List<PackageModel> packages) {
		
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
			classZ.addProperty("ac", String.valueOf(classReport.getClassMetric("AC")));
			classZ.addProperty("asc", String.valueOf(classReport.getClassMetric("ASC")));
			classZ.addProperty("uac", String.valueOf(classReport.getClassMetric("UAC")));
			//classZ.addAllChidren(fetchAnnotations(classReport));
			classZ.addAllChidren(fetchCodeElements(classReport));
			classes_.add(classZ);
		}
		
		return classes_;
	}
	
	private List<Children> fetchCodeElements(ClassModel classReport) {
		
		List<Children> codeElements = new ArrayList<Children>();
		
		for (CodeElementModel codeElement : classReport.getElementsReport()) {
			if(codeElement.getAed()==0)
				continue;
			Children children = new Children
						(codeElement.getElementName(), 
						 codeElement.getType(), 
						 codeElement.getAed());
				children.addAllChidren(fetchAnnotations(codeElement));
			codeElements.add(children);
		}
		
		return codeElements;
	}

	private List<Children> fetchAnnotations(CodeElementModel codeElementReport) {
		
		List<Children> classes_ = new ArrayList<Children>();
		
		for (AnnotationMetricModel annotation : codeElementReport.getAnnotationMetrics()) {
			Children children = new Children
					(annotation.getName(), 
					 "annotation", 
					 null);
			children.addProperty("schema", annotation.getSchema());
			children.addProperty("aa", annotation.getAnnotationMetrics().get("AA").toString());
			children.addProperty("anl", annotation.getAnnotationMetrics().get("ANL").toString());
			children.addProperty("locad", annotation.getAnnotationMetrics().get("LOCAD").toString());
			
			classes_.add(children);
		}
		
		return classes_;
	}

}
