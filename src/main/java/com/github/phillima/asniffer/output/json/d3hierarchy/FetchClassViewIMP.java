package com.github.phillima.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.github.phillima.asniffer.model.*;

public class FetchClassViewIMP implements IFetchChildren {

	@Override
	public List<Children> fetchChildren(PackageModel package_) {
		
		List<Children> classes_ = new ArrayList<Children>();
		
		for (ClassModel classReport : package_.getResults()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			
			Children classZ = new Children
					(classReport.getFullyQualifiedName(), classReport.getType(), null);
			classZ.addProperty("ac", String.valueOf(classReport.getClassMetric("AC")));
			classZ.addProperty("asc", String.valueOf(classReport.getClassMetric("ASC")));
			classZ.addProperty("uac", String.valueOf(classReport.getClassMetric("UAC")));
			//add annotations configuring the class as children
			classZ.addAllChidren(fetchAnnotations(classReport.getElementReport(classReport.getSimpleName(),
																			   classReport.getType())));
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
			if(codeElement.getElementName().equals(classReport.getSimpleName()) &&
			   codeElement.getType().equals(classReport.getType()))
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
		
		List<Children> annotations = new ArrayList<Children>();
		
		for (AnnotationMetricModel annotation : codeElementReport.getAnnotationMetrics()) {
			Children children = new Children
					(annotation.getName(),
							CodeElementType.ANNOTATION,
					 null);
			children.addProperty("schema", annotation.getSchema());
			children.addProperty("aa", annotation.getAnnotationMetrics().get("AA").toString());
			children.addProperty("anl", annotation.getAnnotationMetrics().get("ANL").toString());
			children.addProperty("locad", annotation.getAnnotationMetrics().get("LOCAD").toString());
			
			annotations.add(children);
		}
		
		return annotations;
	}

}
