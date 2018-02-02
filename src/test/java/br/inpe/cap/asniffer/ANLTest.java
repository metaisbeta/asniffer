package br.inpe.cap.asniffer;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class ANLTest {

	private static AMReport report;
	private int entries = 0;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testANL() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		Map<String,Integer> anl = a.getElementMetric("ANL");
		
		anl.forEach((k,v)->{
			System.out.println("Annotation : " + k + " ANL : " + v);
			entries++;
		});
		
		assertEquals(28, entries);
		
	}

}
