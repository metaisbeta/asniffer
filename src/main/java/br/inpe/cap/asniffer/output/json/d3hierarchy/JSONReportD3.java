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
import br.inpe.cap.asniffer.model.MetricResult;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.adapter.ExcludeFieldsJSON;


public class JSONReportD3 implements IReport {
	
	ProjectReportJSON projectReportJson;

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

	private ProjectReportJSON prepareJson(AMReport report) {
		
		ProjectReportJSON projectReportJson =
					new ProjectReportJSON(report.getProjectName());
		
		for (PackageModel package_ : report.getPackages()) {
			PackageReportJSON packageJSON = new PackageReportJSON(package_.getPackageName());
			packageJSON.setClassReportJSON(fetchClassReport(package_));
			if(!packageJSON.getClassReportJSON().isEmpty())
				projectReportJson.addPackageJSON(packageJSON);
		}
		
		return projectReportJson;
		
	}

	private List<ClassReportJSON> fetchClassReport(PackageModel package_) {

		List<ClassReportJSON> classesReportJSON = new ArrayList<ClassReportJSON>();
		
		for (MetricResult classReport : package_.all()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			ClassReportJSON classReportJSON = new ClassReportJSON(classReport.getClassName(),
																  classReport.getType(), classReport.getClassMetric("AC"));
			classReportJSON.setCodeElementsJSON(fetchCodeElementReport(classReport));
			classesReportJSON.add(classReportJSON);
		}
		return classesReportJSON;
	}

	private List<CodeElementJSON> fetchCodeElementReport(MetricResult classReport) {
	
		List<CodeElementJSON> codeElementsJSON = new ArrayList<CodeElementJSON>();
		
		for (CodeElementModel codeElementModel : classReport.getElementsReport()) {
			if(codeElementModel.getAed()==0)//eliminate elements without annotations
				continue;
			CodeElementJSON codeElementJSON = new CodeElementJSON(codeElementModel.getElementName(), 
																  codeElementModel.getType(),codeElementModel.getAed());
			codeElementJSON.setAnnotationJSON(fetchAnnotationReport(codeElementModel.getAnnotationMetrics()));
			codeElementsJSON.add(codeElementJSON);
		}
		return codeElementsJSON;
		
	}

	private List<AnnotationJSON> fetchAnnotationReport(List<AnnotationMetricModel> annotationMetrics) {
		
		List<AnnotationJSON> annotationsJSON = new ArrayList<AnnotationJSON>();
		
		for (AnnotationMetricModel annotationMetricModel : annotationMetrics) {
			AnnotationJSON annotJSON = new AnnotationJSON(annotationMetricModel.getName());
			annotationMetricModel.getAnnotationMetrics().forEach((metric,value) ->{
				AnnotationMetricJSON annotMetricJSON = new AnnotationMetricJSON(metric, value);
				annotJSON.addAnnotMetricJSON(annotMetricJSON);
			});
			annotationsJSON.add(annotJSON);
		}
		
		return annotationsJSON;
	}
}
