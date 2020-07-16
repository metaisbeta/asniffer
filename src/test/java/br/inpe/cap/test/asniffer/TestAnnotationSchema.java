package br.inpe.cap.test.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
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
		
		//Google Guava class
		MetricResult a = report.getPackages().get(0).getByClassName("annotationtest.AbstractService");
		
		Map<String, String> expectedSchemas = new HashMap<String, String>();
		expectedSchemas.put("GwtIncompatible","com.google.common.annotations");
		expectedSchemas.put("WeakOuter","com.google.j2objc.annotations");
		expectedSchemas.put("ForOverride","com.google.errorprone.annotations");
		expectedSchemas.put("Beta","com.google.common.annotations");
		expectedSchemas.put("CanIgnoreReturnValue","com.google.errorprone.annotations");
		expectedSchemas.put("GuardedBy","com.google.errorprone.annotations.concurrent");
		expectedSchemas.put("Nullable","org.checkerframework.checker.nullness.qual");
		expectedSchemas.put("Override","java.lang");
		
		a.getAnnotationSchemasMap().forEach((annotation,schema) -> {
			int lastIndex = annotation.lastIndexOf("-");
			String annotName = annotation.substring(0,lastIndex);
			assertEquals(expectedSchemas.get(annotName), schema);
		});
		
		//Hibernate class
		a = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
		System.out.println(a.getClassName());
		List<CodeElementModel> codeElements = a.getElementsReport();
		
		String schema1 = "java.lang", expectedSchema1 = null;
		String schema2 = "javax.persistence", expectedSchema2 = null;
		String annot1 = "Override";
		String annot2 = "Entity";
		
		for (CodeElementModel codeElement : codeElements) {
			
			System.out.println("CLASS TEST");
			for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
				System.out.println(annotationMetric.getName());
				System.out.println(annotationMetric.getSchema());
			}
			
			if(codeElement.getLine()==36) {
				System.out.println(codeElement.getElementName());
				for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
					System.out.println(annotationMetric.getName());
					System.out.println(annotationMetric.getSchema());
					if(annotationMetric.getName().equals(annot1)) {
						System.out.println("Here");
						expectedSchema1 = annotationMetric.getSchema();
						System.out.println(expectedSchema1);
						break;
					}
				} 
			}
			if(codeElement.getLine()==131) {
				for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
					if(annotationMetric.getName().equals(annot2)) {
						expectedSchema2 = annotationMetric.getSchema();
						System.out.println("EXPECTED2");
						System.out.println(expectedSchema2);
						break;
					}
				} 
			}
		}
		assertEquals(schema1, expectedSchema1);
		assertEquals(schema2, expectedSchema2);
	}
	
}
