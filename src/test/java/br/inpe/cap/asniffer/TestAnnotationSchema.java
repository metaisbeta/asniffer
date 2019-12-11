package br.inpe.cap.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

public class TestAnnotationSchema {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAnnotationSchema() {
		
		MetricResult a = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
		List<CodeElementModel> codeElements = a.getElementsReport();
		String schema1 = "java.lang", expectedSchema1 = null;
		String schema2 = "javax.persistence", expectedSchema2 = null;
		String annot1 = "Override";
		String annot2 = "Entity";
		
		for (CodeElementModel codeElement : codeElements) {
			if(codeElement.getLine()==36) {
				for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
					if(annotationMetric.getName().equals(annot1)) {
						expectedSchema1 = annotationMetric.getSchema();
						break;
					}
				} 
			}
			if(codeElement.getLine()==131) {
				for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
					if(annotationMetric.getName().equals(annot2)) {
						expectedSchema2 = annotationMetric.getSchema();
						break;
					}
				} 
			}
		}
		assertEquals(schema1, expectedSchema1);
		assertEquals(schema2, expectedSchema2);
	}
	
}
