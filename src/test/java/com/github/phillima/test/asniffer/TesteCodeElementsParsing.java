package com.github.phillima.test.asniffer;

import static org.junit.Assert.*;

import com.github.phillima.asniffer.model.CodeElementType;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.PackageModel;

public class TesteCodeElementsParsing {

private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	
	@Test
	public void testReadInnerClass() {
		
		//There should be 5 packages
		assertEquals(5, report.getPackages().size());
		
		//Get the annotationtest package
		PackageModel testPackage_ = report.getPackageByName("annotationtest");
		//get the InnerClassTest
		
		ClassModel clazz = testPackage_.getClassModel("annotationtest.InnerClassTest");
		
		assertEquals(5, clazz.getElementsReport().size());
		assertEquals(CodeElementType.CLASS, clazz.getElementReport("InnerClass1").getType());
		assertEquals(CodeElementType.CLASS, clazz.getElementReport("InnerClass2").getType());
		assertEquals(CodeElementType.ENUM, clazz.getElementReport("Enum1").getType());
		assertEquals(CodeElementType.ENUM, clazz.getElementReport("Enum2").getType());
		
	}
	
	
	@Test
	public void testReadEnumType() {
		
		//There should be 5 packages
		assertEquals(5, report.getPackages().size());
		
		//Get the annotationtest package
		PackageModel testPackage_ = report.getPackageByName("annotationtest");
		//get the EnumTest
		
		ClassModel clazz = testPackage_.getClassModel("annotationtest.EnumTest");
		assertEquals(CodeElementType.ENUM, clazz.getType());
		assertEquals(1, clazz.getElementsReport().size());

	}

}