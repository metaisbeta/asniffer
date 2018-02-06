package br.inpe.cap.asniffer;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class LOCADTest {

	private static AMReport report;
	private int entries = 0;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM(null).calculate(testFilePath, "project");
	}
	
	@Test
	public void testLOCAD() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		Map<String,Integer> locad = a.getElementMetric("LOCAD");
		locad.forEach((k,v)->{
			System.out.println("Annotation : " + k + " LOCAD : " + v);
			entries++;
		});
		assertEquals(17, entries);
	}

}
