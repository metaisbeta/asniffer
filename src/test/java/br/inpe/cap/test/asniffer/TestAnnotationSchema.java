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
import br.inpe.cap.asniffer.model.ClassModel;

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
		ClassModel a = report.getPackages().get(0).getByClassName("annotationtest.AbstractService");
		
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
		List<CodeElementModel> codeElements = a.getElementsReport();
		
		
		Map<String, String> expectedSchemas2 = new HashMap<String, String>();
		expectedSchemas2.put("Override","java.lang");
		expectedSchemas2.put("Entity","javax.persistence");
		expectedSchemas2.put("GeneratedValue","javax.persistence");
		expectedSchemas2.put("Id","javax.persistence");
		expectedSchemas2.put("Inheritance","javax.persistence");
		expectedSchemas2.put("InheritanceType","javax.persistence");
		expectedSchemas2.put("Before","org.junit");
		expectedSchemas2.put("Test","org.junit");
		expectedSchemas2.put("Annotation0","java.lang");
		expectedSchemas2.put("AssociationOverrides","javax.persistence");
		expectedSchemas2.put("AssociationOverride","javax.persistence");
		expectedSchemas2.put("NamedQuery","javax.persistence");
		expectedSchemas2.put("JoinColumn","javax.persistence");
		expectedSchemas2.put("Annotation2","java.lang");
		
		
		
		
		a.getAnnotationSchemasMap().forEach((annotation,schema) -> {
			int lastIndex = annotation.lastIndexOf("-");
			String annotName = annotation.substring(0,lastIndex);
			assertEquals(expectedSchemas2.get(annotName), schema);
		});
		
		
		String schema1 = "java.lang", expectedSchema1 = null;
		String schema2 = "javax.persistence", expectedSchema2 = null;
		String annot1 = "Override";
		String annot2 = "Entity";
		
		for (CodeElementModel codeElement : codeElements) {
			
			if(codeElement.getLine()==40) {
				for (AnnotationMetricModel annotationMetric : codeElement.getAnnotationMetrics()) {
					if(annotationMetric.getName().equals(annot1)) {
						expectedSchema1 = annotationMetric.getSchema();
						break;
					}
				} 
			}
			if(codeElement.getLine()==135) {
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
