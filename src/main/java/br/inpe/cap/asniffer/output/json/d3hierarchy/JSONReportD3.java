package br.inpe.cap.asniffer.output.json.d3hierarchy;
//This class provides a hierarchical JSON output suitable for D3 Hierarchy Visualizations
//Includes three metrics: ANL, LOCAD and AA
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.adapter.ExcludeFieldsJSON;


public class JSONReportD3 implements IReport {
	
	ProjectReportJSOND3 projectReportJson;

	@Override
	public void generateReport(AMReport report, String path) {
		
		projectReportJson = prepareJson(report);
		
		Gson gson = new GsonBuilder()
				.addSerializationExclusionStrategy(new ExcludeFieldsJSON())
				.setPrettyPrinting().create();
		
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

	private ProjectReportJSOND3 prepareJson(AMReport report) {
		
		ProjectReportJSOND3 projectReportJson =
					new ProjectReportJSOND3(report.getProjectName());
		
		for (PackageModel package_ : report.getPackages()) {
			PackageReportJSOND3 packageJSON = new PackageReportJSOND3(package_.getPackageName());
			packageJSON.setClassReportJSON(fetchClassReport(package_));
			if(!packageJSON.getClassReportJSON().isEmpty())
				projectReportJson.addPackageJSON(packageJSON);
		}
		
		return projectReportJson;
		
	}

	private List<ClassReportJSOND3> fetchClassReport(PackageModel package_) {

		List<ClassReportJSOND3> classesReportJSON = new ArrayList<ClassReportJSOND3>();
		
		for (ClassModel classReport : package_.getResults()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			ClassReportJSOND3 classReportJSON = new ClassReportJSOND3(classReport.getClassName(),
																  classReport.getType(), classReport.getClassMetric("AC"));
			classReportJSON.setCodeElementsJSON(fetchCodeElementReport(classReport));
			classesReportJSON.add(classReportJSON);
		}
		return classesReportJSON;
	}

	private List<CodeElementJSOND3> fetchCodeElementReport(ClassModel classReport) {
	
		List<CodeElementJSOND3> codeElementsJSON = new ArrayList<CodeElementJSOND3>();
		
		for (CodeElementModel codeElementModel : classReport.getElementsReport()) {
			if(codeElementModel.getAed()==0)//eliminate elements without annotations
				continue;
			CodeElementJSOND3 codeElementJSON = new CodeElementJSOND3(codeElementModel.getElementName(), 
																  codeElementModel.getType(),codeElementModel.getAed());
			codeElementJSON.setAnnotationJSON(fetchAnnotationReport(codeElementModel.getAnnotationMetrics()));
			codeElementsJSON.add(codeElementJSON);
		}
		return codeElementsJSON;
		
	}

	private List<AnnotationJSOND3> fetchAnnotationReport(List<AnnotationMetricModel> annotationMetrics) {
		
		List<AnnotationJSOND3> annotationsJSON = new ArrayList<AnnotationJSOND3>();
		
		for (AnnotationMetricModel annotationMetricModel : annotationMetrics) {
			AnnotationJSOND3 annotJSON = new AnnotationJSOND3(annotationMetricModel.getName());
			annotationMetricModel.getAnnotationMetrics().forEach((metric,value) ->{
				AnnotationMetricJSOND3 annotMetricJSON = new AnnotationMetricJSOND3(metric, value);
				annotJSON.addAnnotMetricJSON(annotMetricJSON);
			});
			annotationsJSON.add(annotJSON);
		}
		
		return annotationsJSON;
	}
}
