package br.inpe.cap.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class AEDTest {

	private static AMReport report;
	private int entries = 0;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAED() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		//Assert.assertEquals(2, a.getElementMetric("AED"));
	
		Map<String,Integer> aed = a.getElementMetric("AED");
		
		
		aed.forEach((k,v)->{
			System.out.println("Element : " + k + " AED : " + v);
			entries++;
		});
		
		assertEquals(32, entries);
		
	}
	
}
