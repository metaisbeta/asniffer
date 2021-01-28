package br.inpe.cap.asniffer.output.json.d3hierarchy.systemview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections4.map.HashedMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.adapter.ExcludeFieldsJSON;

public class JSONReportSV implements IReport{

	ProjectReportSystemView projectReportJson;
	
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
	
	private ProjectReportSystemView prepareJson(AMReport report) {
		
		ProjectReportSystemView projectReportJson =
					new ProjectReportSystemView(report.getProjectName());
		
		projectReportJson.addPackages(fetchPackages(report.getPackages()));
		
		
		return projectReportJson;
		
	}

	public List<PackageContentSV> fetchPackages(List<PackageModel> packages) {

		List<PackageContentSV> packageContents = new ArrayList<PackageContentSV>();
		Stack<PackageContentSV> packageContentStack = new Stack<PackageContentSV>();
		
		//Ordering package models
		List<PackageModel> orderedPackModel = new ArrayList<PackageModel>(packages);
		Collections.sort(orderedPackModel);
		
		String rootPackageName = null;
		PackageContentSV rootPackage = null, topStackPackage = null;
		String previousPackageName = null;
		//Define root packages
		for(PackageModel packageModel : orderedPackModel) {
			
			PackageContentSV packageContent = 
					new PackageContentSV(packageModel.getPackageName(), "package", null);
			//Fetch schemas
			packageContent.addAllPackageChildren(fetchAnnotationReport(packageModel));
			
			if(packageContentStack.isEmpty()) {
				rootPackage = 
						new PackageContentSV(packageModel.getParentPackageName(), "package", null);
				if(!packageModel.getPackageName().equals(packageModel.getParentPackageName()))
					rootPackage.addPackageChildren(packageContent);
				packageContentStack.push(rootPackage);
				rootPackageName = rootPackage.getName();
				packageContents.add(packageContent);
				topStackPackage = rootPackage;
			}else {
				if(topStackPackage.getName().equals(packageModel.getParentPackageName())) {
					topStackPackage.addPackageChildren(packageContent);
				}else if(packageModel.getParentPackageName().equals(previousPackageName)) {
					topStackPackage = topStackPackage.getPackageContentByName(previousPackageName);
					packageContentStack.push(topStackPackage);
					topStackPackage.addPackageChildren(packageContent);
				} else if(packageModel.getPackageName().contains(rootPackageName)) {
					while(!topStackPackage.getName().equals(packageModel.getParentPackageName())) {
						
						if(topStackPackage.getName().length() < packageModel.getParentPackageName().length()) {
							PackageContentSV temp = new PackageContentSV(packageModel.getParentPackageName(), "package", null);
							//temp.addPackageChildren(packageContent);
							packageContentStack.push(topStackPackage);
							packageContentStack.push(temp);
							topStackPackage.addPackageChildren(temp);
						}
						topStackPackage = packageContentStack.pop();
					}
					topStackPackage.addPackageChildren(packageContent);
					packageContentStack.push(topStackPackage);
				} else {
					rootPackage = 
							new PackageContentSV(packageModel.getParentPackageName(), "package", null);
					if(!packageModel.getPackageName().equals(packageModel.getParentPackageName()))
						rootPackage.addPackageChildren(packageContent);
					packageContentStack.push(rootPackage);
					packageContentStack.push(packageContent);
					rootPackageName = rootPackage.getName();
					packageContents.add(rootPackage);
					topStackPackage = packageContentStack.peek();
				}
			}
			
			previousPackageName = packageModel.getPackageName();
		}
		
		return packageContents;
	}


	private List<PackageContentSV> fetchAnnotationReport(PackageModel package_) {
		
		List<PackageContentSV> annotationSV = new ArrayList<PackageContentSV>();
		
		Map<String, Integer> schemaMap = new HashedMap<String, Integer>();
		
		for (ClassModel classReport : package_.getResults()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			
			classReport.getAnnotationSchemasMap().forEach((name,schema) -> {
				
				schemaMap.compute(schema, (k,v) -> (v == null ? 0 : v) + 1);
				
			});
			
		}
		
		schemaMap.forEach((k,v) -> {
			PackageContentSV annotaSV = 
					new PackageContentSV(k,"schema",v);
			annotationSV.add(annotaSV);
		});
		
		return annotationSV;
		
	}

}
