package com.github.phillima.test.asniffer;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.AnnotationMetricModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.ClassModel;

public class TestAnnotationSchema {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAnnotationSchemaWithFullyDeclared() {

		//Google Guava class
		ClassModel classModel = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AbstractService");

		Map<String, String> expectedSchemas = new HashMap<String, String>();
		expectedSchemas.put("GwtIncompatible-51", "com.google.common.annotations");
		expectedSchemas.put("WeakOuter-123", "com.google.j2objc.annotations");
		expectedSchemas.put("ForOverride-204", "com.google.errorprone.annotations");
		expectedSchemas.put("Beta-238", "com.google.common.annotations");
		expectedSchemas.put("CanIgnoreReturnValue-242", "com.google.errorprone.annotations");
		expectedSchemas.put("GuardedBy-371", "com.google.errorprone.annotations.concurrent");
		expectedSchemas.put("Nullable-591", "org.checkerframework.checker.nullness.qual");
		expectedSchemas.put("Override-129", "java.lang");
		expectedSchemas.put("Nullable-614", "br.inatel.cdg.annotation");

		Map<String, String> returnedSchemas = classModel.getAnnotationSchemasMap();
		assertEquals(expectedSchemas.get("GwtIncompatible-51"), returnedSchemas.get("GwtIncompatible-51"));
		assertEquals(expectedSchemas.get("WeakOuter-123"), returnedSchemas.get("WeakOuter-123"));
		assertEquals(expectedSchemas.get("ForOverride-204"), returnedSchemas.get("ForOverride-204"));
		assertEquals(expectedSchemas.get("Beta-238"), returnedSchemas.get("Beta-238"));
		assertEquals(expectedSchemas.get("CanIgnoreReturnValue-242"), returnedSchemas.get("CanIgnoreReturnValue-242"));
		assertEquals(expectedSchemas.get("GuardedBy-371"), returnedSchemas.get("GuardedBy-371"));
		assertEquals(expectedSchemas.get("Nullable-591"), returnedSchemas.get("Nullable-591"));
		assertEquals(expectedSchemas.get("Override-129"), returnedSchemas.get("Override-129"));
		assertEquals(expectedSchemas.get("Nullable-614"), returnedSchemas.get("Nullable-614"));
	}

	@Test
	public void testAnnotationSchemaBySimpleName() {


		//Hibernate class
		ClassModel classModel = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");


		Map<String, String> expectedSchemas2 = new HashMap<String, String>();
		expectedSchemas2.put("Override", "java.lang");
		expectedSchemas2.put("Entity", "javax.persistence");
		expectedSchemas2.put("GeneratedValue", "javax.persistence");
		expectedSchemas2.put("Id", "javax.persistence");
		expectedSchemas2.put("Inheritance", "javax.persistence");
		expectedSchemas2.put("InheritanceType", "javax.persistence");
		expectedSchemas2.put("Before", "org.junit");
		expectedSchemas2.put("Test", "org.junit");
		expectedSchemas2.put("Annotation0", "annotationtest");
		expectedSchemas2.put("AssociationOverrides", "javax.persistence");
		expectedSchemas2.put("AssociationOverride", "javax.persistence");
		expectedSchemas2.put("NamedQuery", "javax.persistence");
		expectedSchemas2.put("JoinColumn", "javax.persistence");
		expectedSchemas2.put("Annotation2", "annotationtest");
		expectedSchemas2.put("GET", "javax.ws.rs");
		expectedSchemas2.put("Path", "javax.ws.rs");
		expectedSchemas2.put("Produces", "javax.ws.rs");
		expectedSchemas2.put("Secured", "org.springframework.security.access.annotation");
		expectedSchemas2.put("QueryParam", "javax.ws.rs");
		expectedSchemas2.put("Context", "javax.ws.rs.core");
		expectedSchemas2.put("PathParam", "javax.ws.rs");

		classModel.getAnnotationSchemasMap().forEach((annotation,schema) -> {
			int lastIndex = annotation.lastIndexOf("-");
			String annotName = annotation.substring(0,lastIndex);
			assertEquals(expectedSchemas2.get(annotName), schema);
		});

	}

	@Test
	public void testAnnotationSchemaByCodeElementReport(){

		ClassModel classModel = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");

		List<CodeElementModel> codeElements = classModel.getElementsReport();

		String expectedSchema1 = "java.lang";
		String expectedSchema2 = "javax.persistence";



		CodeElementModel codeElement1 = codeElements.stream().filter(codeElement -> codeElement.getLine()==66).findFirst().get();
		CodeElementModel codeElement2 = codeElements.stream().filter(codeElement -> codeElement.getLine()==161).findFirst().get();

		String schema1 = codeElement1.getAnnotationMetrics().stream().
				filter(annotMetric -> annotMetric.getName().equals("Override")).findFirst().get().getSchema();
		String schema2 = codeElement2.getAnnotationMetrics().stream().
				filter(annotMetric -> annotMetric.getName().equals("Entity")).findFirst().get().getSchema();

		assertEquals(schema1, expectedSchema1);
		assertEquals(schema2, expectedSchema2);
	}

	@Test
	public void testFullyQualifiedAnnotationSchema(){

		ClassModel classModel = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.InnerAnnotationTypeTest");
		String schema = classModel.getAnnotationSchema("AnnotationFullyName-9");
		Assert.assertEquals("br.inatel.cdg.annotation",schema);

	}
}
