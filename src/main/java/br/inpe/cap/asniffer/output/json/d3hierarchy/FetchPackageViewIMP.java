package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.PackageModel;

public class FetchPackageViewIMP implements IFetchChildren {

	@Override
	public List<Children> fetchChildren(PackageModel package_) {
		List<Children> classes_ = new ArrayList<Children>();
		
		for (ClassModel classReport : package_.getResults()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			
			Children classZ = new Children
					(classReport.getFullyQualifiedName(), classReport.getType(), null);
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
