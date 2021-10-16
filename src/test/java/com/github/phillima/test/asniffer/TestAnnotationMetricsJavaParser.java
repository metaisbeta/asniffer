package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AMJavaParser;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.AnnotationMetricModel;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class TestAnnotationMetricsJavaParser {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();
		report = new AMJavaParser().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAnnotationMetric() {

		ClassModel a = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");

		List<CodeElementModel> codeElements = a.getElementsReport();
		int aa = 0, anl = 0, locad = 0;

		for (CodeElementModel codeElement : codeElements) {
			if(codeElement.getLine()==161) {
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
