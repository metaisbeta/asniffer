package br.inpe.cap.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class AATest {

	private static AMReport report;
	private int entries = 0;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAA() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		Map<String,Integer> aa = a.getElementMetric("AA");
		aa.forEach((k,v)->{
			System.out.println("Annotation : " + k + " AA : " + v);
			entries++;
		});
		assertEquals(17, entries);
	}
	
}
