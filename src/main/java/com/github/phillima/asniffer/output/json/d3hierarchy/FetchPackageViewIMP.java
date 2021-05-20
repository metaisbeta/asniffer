package com.github.phillima.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.github.phillima.asniffer.model.AnnotationMetricModel;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.PackageModel;

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
				//Considering LOCAD
				Children children = new Children
						(annotation.getName(), 
						 "annotation", 
						 annotation.getAnnotationMetrics().get("LOCAD"));
				children.addProperty("schema", annotation.getSchema());
				classes_.add(children);
			}
		}
		
		return classes_;
	}
	
}
