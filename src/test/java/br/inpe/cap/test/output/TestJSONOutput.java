package br.inpe.cap.test.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;


import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.ASniffer;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.json.d3hierarchy.Children;
import br.inpe.cap.asniffer.output.json.d3hierarchy.FetchClassViewIMP;
import br.inpe.cap.asniffer.output.json.d3hierarchy.FetchPackageViewIMP;
import br.inpe.cap.asniffer.output.json.d3hierarchy.FetchSystemViewIMP;
import br.inpe.cap.asniffer.output.json.d3hierarchy.JSONReportAvisuIMP;
import br.inpe.cap.asniffer.utils.ReportTypeUtils;

public class TestJSONOutput {

	
	private static AMReport report;
	private static String testFilePath;
	
	//Fixture
	@BeforeClass
	public static void setUp() {
		//Collecting ASniffer
		testFilePath = System.getProperty("user.dir");
		report = new AM().calculate(testFilePath , "project");
	}
	
	@Test
	public void testJsonSV() {
		
		List<Children> packagesContentReport = ReportTypeUtils.fetchPackages(report.getPackages(), 
				new FetchSystemViewIMP());
		
		assertEquals(7, packagesContentReport.size());
		
		assertEquals("annotationtest", packagesContentReport.get(0).getName());
		assertEquals("br.inpe.cap.asniffer", packagesContentReport.get(1).getName());
		assertEquals("br.inpe.cap.test.asniffer", packagesContentReport.get(2).getName());
		assertEquals("br.inpe.cap.test.output", packagesContentReport.get(3).getName());
		assertEquals("br.inpe.cap.test.parameter", packagesContentReport.get(4).getName());
		assertEquals("br.inpe.climaespacial.tsi.business", packagesContentReport.get(5).getName());
		assertEquals("br.inpe.climaespacial.tsi.businesscli", packagesContentReport.get(6).getName());
		
		List<Children> packageChild1 = packagesContentReport.get(1).getChildrens();
		
		//8 root packages and java lang schema
		assertEquals(9, packageChild1.size());
		
		//the package br.inpe.cap.output
		Children childrens = packagesContentReport.get(1).getChildByName("br.inpe.cap.asniffer.output");
		assertEquals(1, childrens.getChildrens().size());
		
		//the package br.inpe.cap.output.json.d3hierarchy
		List<Children> childrens2 = childrens.getChildrens().
						get(0).getChildrens().get(2).getChildrens();
		//2 schemas and 3 inner packages
		assertEquals(5, childrens2.size());
		
	}
	
	@Test
	public void testJsonPV() {
		
		List<Children> childrens = ReportTypeUtils.fetchPackages(report.getPackages(), new FetchPackageViewIMP());
		
		assertEquals(7, childrens.size());
		
		//br.inpe.cap.asniffer packages
		Children packageRoot1 = childrens.get(1).getChildByName("br.inpe.cap.asniffer.model");
		
		//1 class with annotation
		assertEquals(1, packageRoot1.getChildrens().size());
		
		//br.inpe.cap packages
		Children packageRoot2 = childrens.get(1).getChildByName("br.inpe.cap.asniffer.parameters");
		
		//2 classes and 1 package
		assertEquals(3, packageRoot2.getChildrens().size());
	}
	
	@Test
	public void testJsonCV() {
		
		List<Children> childrens = ReportTypeUtils.fetchPackages(report.getPackages()
				, new FetchClassViewIMP());
		assertEquals(7, childrens.size());
		
		
		
		//first package annotationtest
		Children package1 = childrens.get(0);
		String classTestName = "annotationtest.AbstractService";
		Children classZ = package1.getChildByName(classTestName);
		
		assertEquals(classTestName,classZ.getName());
		assertEquals(37,classZ.getChildrens().size());
		
		//Get annotations on class
		Children annotation = classZ.getChildByName("GwtIncompatible");
		assertEquals("annotation", annotation.getType());
		assertEquals("0", annotation.getProperty("aa"));
		assertEquals("1", annotation.getProperty("locad"));
		assertEquals("0", annotation.getProperty("anl"));
		
		//second package
		Children package2 = childrens.get(1).getChildByName("br.inpe.cap.asniffer.metric");
		List<Children> pkg1Children = package2.getChildrens();
		
		assertEquals(8, pkg1Children.size());
		
		//AC class
		Children acClass = package2.getChildByName("br.inpe.cap.asniffer.metric.AC");
		assertEquals(6, acClass.getChildrens().size());
		
		
		
	}
	
	
	@Test
	public void testGenerateFullAVisuReportFile() {
		ASniffer aSniffer = new ASniffer(testFilePath, testFilePath, new JSONReportAvisuIMP());
		aSniffer.collectSingle();
		
		String dirPathResult = testFilePath + File.separator + "asniffer_results";
		
		assertTrue(new File(dirPathResult + File.separator + "asniffer-CV.json").exists());
		assertTrue(new File(dirPathResult + File.separator + "asniffer-SV.json").exists());
		assertTrue(new File(dirPathResult + File.separator + "asniffer-PV.json").exists());
		
		//delete the reports
		assertTrue(new File(dirPathResult + File.separator + "asniffer-CV.json").delete());
		assertTrue(new File(dirPathResult + File.separator + "asniffer-SV.json").delete());
		assertTrue(new File(dirPathResult + File.separator + "asniffer-PV.json").delete());
		assertTrue(new File(dirPathResult + File.separator).delete());
	}
	
	
}