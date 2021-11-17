package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.CodeElementType;
import com.github.phillima.asniffer.output.json.d3hierarchy.Children;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchSystemViewIMP;
import com.github.phillima.asniffer.utils.ReportTypeUtils;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestAnnotationSchema {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();
		report = AmFactory.createAm(testFilePath, "project").calculate();
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

		assertEquals(expectedSchema1, schema1);
		assertEquals(expectedSchema2, schema2);
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

	@Test
	public void testDifferentAnnotationTypes() {
		ClassModel classModel = report.getPackages()
			.stream()
			.filter(pk -> pk.getPackageName().equals("annotationtest"))
			.findFirst()
			.get()
		.getClassModel("annotationtest.SchemaTest");
		
		var annnotationNameBySchema = Map.of(
			"Import-15", "org.springframework.context.annotation",
			"MyImport-18", "org.springframework.context.myimport",
			"Override-21", "java.lang",
			"Controller-8", "org.springframework.web.bind.annotation",
			"RequestMapping-9", "org.springframework.web.bind.annotation",
			"FieldMatch.List-10", "com.salesmanager.shop.validation",
			"FieldMatch-11", "com.salesmanager.shop.validation",
			"FieldMatch.List-24","com.salesmanager.shop.validation",
			"FieldMatch-25","com.salesmanager.shop.validation"
		);

		classModel.getAnnotationSchemasMap().forEach((annotationName, schema) -> {
			assertEquals(schema, annnotationNameBySchema.get(annotationName));
		});

	}

	public void testAnnotationDelcaredInsideAnnotationSchema(){


		ClassModel classModel = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.SchemaTest");

		int ac = classModel.getClassMetric("AC");
		int asc = classModel.getClassMetric("ASC");
		String schema1 = classModel.getAnnotationSchema("FieldMatch-11");
		String schema2 = classModel.getAnnotationSchema("FieldMatch.List-10");

		Assert.assertEquals(5,asc);
		Assert.assertEquals(9,ac);
		Assert.assertEquals("com.salesmanager.shop.validation",schema1);
		Assert.assertEquals("com.salesmanager.shop.validation",schema2);
		Assert.assertNull(classModel.getAnnotationSchema("List-10"));//Must not find an annotation named List
		Assert.assertNull(classModel.getAnnotationSchema("List-24"));//Must not find an annotation named List

	}

	@Test
	public void testSchemaChildShouldNotHaveChildren() {
		var schemaChildTestPath = Paths.get(System.getProperty("user.dir") + "/annotationtest/schemaChildTest").toString();
		var schemaChildReport = AmFactory.createAm(schemaChildTestPath, "asniffer").calculate();

		List<Children> packagesContentReport = ReportTypeUtils.fetchPackages(schemaChildReport.getPackages(), new FetchSystemViewIMP());

		var childrenOfSchema = packagesContentReport.get(0).getChildrens().stream()
				.filter(children -> CodeElementType.SCHEMA.equals(children.getType()))
				.filter(children -> !children.getChildrens().isEmpty())
				.collect(Collectors.toList());

		assertEquals(0, childrenOfSchema.size());
	}


}
