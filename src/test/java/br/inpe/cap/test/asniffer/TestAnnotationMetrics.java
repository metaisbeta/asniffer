package br.inpe.cap.test.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.ClassModel;

public class TestAnnotationMetrics {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAnnotationSchema() {
		
		ClassModel a = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
		List<CodeElementModel> codeElements = a.getElementsReport();
		int aa = 0, anl = 0, locad = 0;
		
		for (CodeElementModel codeElement : codeElements) {
			if(codeElement.getLine()==135) {
				for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
					if(annotationMetric.getName().equals("Test"))
						aa = annotationMetric.getAnnotationMetrics().get("AA");
					if(annotationMetric.getName().equals("JoinColumn"))
						anl = annotationMetric.getAnnotationMetrics().get("ANL");
					if(annotationMetric.getName().equals("AssociationOverrides"))
						locad = annotationMetric.getAnnotationMetrics().get("LOCAD");
				} 
			}
		}
		assertEquals(3, aa);
		assertEquals(2, anl);
		assertEquals(5, locad);
	}
	
}
