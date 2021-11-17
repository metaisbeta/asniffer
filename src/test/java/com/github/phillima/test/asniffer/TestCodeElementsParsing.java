package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.model.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestCodeElementsParsing {

private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = AmFactory.createAm(testFilePath, "project").calculate();
	}
	
	
	@Test
	public void testCodeElementsTypes() {
		
		//Get the annotationtest package
		PackageModel testPackage_ = report.getPackageByName("annotationtest");
		//get the InnerClassTest
		
		ClassModel clazz = testPackage_.getClassModel("annotationtest.InnerClassTest");

		assertEquals(8,clazz.getElementsReport().size());
		assertEquals(clazz.getElementReport("InnerClass1").getType(), CodeElementType.CLASS);
		assertEquals(clazz.getElementReport("InnerClass2").getType(), CodeElementType.CLASS);
		assertEquals(clazz.getElementReport("InnerClassTest", CodeElementType.CLASS).getType(), CodeElementType.CLASS);
		assertEquals(clazz.getElementReport("InnerClassTest", CodeElementType.CONSTRUCTOR).getType(), CodeElementType.CONSTRUCTOR);
		assertEquals(clazz.getElementReport("Enum1").getType(), CodeElementType.ENUM);
		assertEquals(clazz.getElementReport("Enum2").getType(), CodeElementType.ENUM);
		assertEquals(clazz.getElementReport("member1").getType(), CodeElementType.FIELD);
		assertEquals(clazz.getElementReport("method1").getType(), CodeElementType.METHOD);
	}
	
	
	@Test
	public void testReadEnumType() {
		
    	//Get the annotationtest package
		PackageModel testPackage_ = report.getPackageByName("annotationtest");
		//get the EnumTest
		
		ClassModel clazz = testPackage_.getClassModel("annotationtest.EnumTest");
		assertEquals(CodeElementType.ENUM, clazz.getType());
		assertEquals(1, clazz.getElementsReport().size());

	}

	@Test
	public void testUnnamedPackage() {
		var unnamedPackageModel = report.getPackages().stream()
				.filter(packageModel -> PackageType.UNNAMED.equals(packageModel.getPackageName()))
				.findFirst()
				.get();

		var className = unnamedPackageModel.getResults().get(0).getFullyQualifiedName();

		assertEquals("unnamed", unnamedPackageModel.getPackageName());
		assertEquals("NoPackageTest", className);
	}

}