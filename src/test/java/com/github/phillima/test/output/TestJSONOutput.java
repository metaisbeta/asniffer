package com.github.phillima.test.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.github.phillima.asniffer.utils.FileUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.ASniffer;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.json.d3hierarchy.Children;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchClassViewIMP;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchPackageViewIMP;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchSystemViewIMP;
import com.github.phillima.asniffer.output.json.d3hierarchy.JSONReportAvisuIMP;
import com.github.phillima.asniffer.utils.ReportTypeUtils;

public class TestJSONOutput {

	
	private static AMReport report;
	private static String testFilePath;
	
	//Fixture
	@BeforeClass
	public static void setUp() {
		//Collecting ASniffer
		testFilePath = System.getProperty("user.dir");
		report = new AM().calculate(testFilePath , "asniffer");
	}
	
	@Ignore
	public void testJsonSV() {
		
		List<Children> packagesContentReport = ReportTypeUtils.fetchPackages(report.getPackages(), 
				new FetchSystemViewIMP());
		
		assertEquals(8, packagesContentReport.size());
		
		assertEquals("annotationtest", packagesContentReport.get(0).getName());
		assertEquals("br.inpe.climaespacial.tsi.business", packagesContentReport.get(1).getName());
		assertEquals("br.inpe.climaespacial.tsi.businesscli", packagesContentReport.get(2).getName());
		assertEquals("com.github.phillima.asniffer", packagesContentReport.get(3).getName());
		assertEquals("com.github.phillima.test.asniffer", packagesContentReport.get(4).getName());
		assertEquals("com.github.phillima.test.output", packagesContentReport.get(5).getName());
		assertEquals("com.github.phillima.test.parameter", packagesContentReport.get(6).getName());

		List<Children> packageChild1 = packagesContentReport.get(3).getChildrens();
		
		//8 root packages and java lang schema
		assertEquals(9, packageChild1.size());
		
		//the package br.inpe.cap.output
		Children childrens = packagesContentReport.get(3).getChildByName("com.github.phillima.asniffer.output");
		assertEquals(1, childrens.getChildrens().size());
		
		//the package br.inpe.cap.output.json.d3hierarchy
		List<Children> childrens2 = childrens.getChildrens().
						get(0).getChildrens().get(2).getChildrens();
		//2 schemas and 3 inner packages
		assertEquals(5, childrens2.size());
		
	}
	
	@Ignore
	public void testJsonPV() {
		
		List<Children> childrens = ReportTypeUtils.fetchPackages(report.getPackages(), new FetchPackageViewIMP());
		
		assertEquals(8, childrens.size());


		//br.inpe.cap.asniffer packages
		Children packageRoot1 = childrens.stream().
					filter(children -> children.getName().equals("com.github.phillima.asniffer")).
					findFirst().get().getChildByName("com.github.phillima.asniffer.model");
		
		//1 class with annotation
		assertEquals(1, packageRoot1.getChildrens().size());
		
		//br.inpe.cap packages
		Children packageRoot2 = childrens.stream().
				filter(children -> children.getName().equals("com.github.phillima.asniffer")).
				findFirst().get().getChildByName("com.github.phillima.asniffer.parameters");
		
		//2 classes and 1 package
		assertEquals(3, packageRoot2.getChildrens().size());
	}
	
	@Ignore
	public void testJsonCV() {
		
		List<Children> childrens = ReportTypeUtils.fetchPackages(report.getPackages()
				, new FetchClassViewIMP());
		assertEquals(8, childrens.size());
		
		//first package annotationtest
		Children package1 = childrens.get(0);
		String classTestName = "annotationtest.AbstractService";
		Children classZ = package1.getChildByName(classTestName);
		
		assertEquals(classTestName,classZ.getName());

		assertEquals(39,classZ.getChildrens().size());
		
		//Get annotations on class
		Children annotation = classZ.getChildByName("GwtIncompatible");
		assertEquals("annotation", annotation.getType());
		assertEquals("0", annotation.getProperty("aa"));
		assertEquals("1", annotation.getProperty("locad"));
		assertEquals("0", annotation.getProperty("anl"));
		
		//second package
		Children package2 = childrens.get(3).getChildByName("com.github.phillima.asniffer.metric");
		List<Children> pkg1Children = package2.getChildrens();
		
		assertEquals(8, pkg1Children.size());
		
		//AC class
		Children acClass = package2.getChildByName("com.github.phillima.asniffer.metric.AC");
		assertEquals(6, acClass.getChildrens().size());

	}

	@Test
	public void testGenerateFullAVisuReportFile() {
		ASniffer aSniffer = new ASniffer(testFilePath, testFilePath, new JSONReportAvisuIMP());
		aSniffer.collectSingle();
		String pathSeparator = FileSystems.getDefault().getSeparator();

		String projectName = FileUtils.getProjectName(Path.of(testFilePath));
		String dirPathResult = testFilePath + pathSeparator + "asniffer_results";
		assertTrue(new File(dirPathResult + pathSeparator  + "asniffer-CV.json").exists());
		assertTrue(new File(dirPathResult + pathSeparator + "asniffer-SV.json").exists());
		assertTrue(new File(dirPathResult + pathSeparator  + "asniffer-PV.json").exists());

		//delete the reports
		assertTrue(new File(dirPathResult + pathSeparator  + "asniffer-CV.json").delete());
		assertTrue(new File(dirPathResult + pathSeparator  + "asniffer-SV.json").delete());
		assertTrue(new File(dirPathResult + pathSeparator  + "asniffer-PV.json").delete());
		assertTrue(new File(dirPathResult + pathSeparator ).delete());

	}

}