package br.inpe.cap.test.ouput;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.json.d3hierarchy.packageview.Children;
import br.inpe.cap.asniffer.output.json.d3hierarchy.packageview.JSONReportPV;
import br.inpe.cap.asniffer.output.json.d3hierarchy.systemview.JSONReportSV;
import br.inpe.cap.asniffer.output.json.d3hierarchy.systemview.PackageContentSV;

public class TestJSONOutput {

	
	private static AMReport report;
	private static String testFilePath;
	
	//Fixture
	@BeforeClass
	public static void setUp() {
		//Collecting ASniffer
		testFilePath = System.getProperty("user.dir");
		report = new AM().calculate(testFilePath, "project");
		
	}
	
	@Test
	public void testJsonSystemView() {
		
		JSONReportSV reportJsonSV = new JSONReportSV();
		reportJsonSV.generateReport(report, testFilePath);
		
		List<PackageContentSV> packagesContentReport = reportJsonSV.fetchPackages(report.getPackages());
		
		assertEquals("annotationtest", packagesContentReport.get(0).getName());
		assertEquals("br.inpe.cap", packagesContentReport.get(1).getName());
		
		assertEquals(2, packagesContentReport.size());
		
		PackageContentSV packageChild1 = packagesContentReport.get(1);
		
		assertEquals(2, packageChild1.getPackageChildrens().size());
		assertEquals("br.inpe.cap.asniffer", packageChild1.getPackageChildrens().get(0).getName());
		assertEquals("br.inpe.cap.test", packageChild1.getPackageChildrens().get(1).getName());
		
		PackageContentSV packageChild2 = packageChild1.getPackageChildrens().get(0);
		
		assertEquals(9, packageChild2.getPackageChildrens().size());
		
		
		
		//ProjectReportSystemView projectReport = rep
		
	}
	
	@Test
	public void testJSONPV() {
		
		JSONReportPV jsonReport = new JSONReportPV();
		jsonReport.generateReport(report, testFilePath);
		
		List<Children> childrens = jsonReport.fetchPackages(report.getPackages());
		
		assertEquals(2, childrens.size());
		
		//br.inpe.cap packages
		Children packageRoot1 = childrens.get(1).getChildByName("br.inpe.cap.asniffer");
		
		//2 classes with annotations and 8 packages
		assertEquals(10, packageRoot1.getChildrens().size());
		
		
		//br.inpe.cap packages
		Children packageRoot2 = childrens.get(1).getChildByName("br.inpe.cap.test");
		
		//3 classes with junit annotations
		assertEquals(3, packageRoot2.getChildrens().size());
		
	}
	
	
	
}