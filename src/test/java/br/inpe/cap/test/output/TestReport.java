package br.inpe.cap.test.output;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.exceptions.ReportTypeException;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.parameters.ParamMapper;
import br.inpe.cap.asniffer.parameters.ParameterReadingException;
import br.inpe.cap.asniffer.parameters.Parameters;
import br.inpe.cap.asniffer.utils.FileUtils;
import br.inpe.cap.asniffer.utils.PropertiesUtil;

public class TestReport {

	private static AMReport report;
	private static IReport outputProcessor;
	static String testFilePath;
	
	@Test
	public void readReportType() {
		
		String actualClassName =  PropertiesUtil.getReportType("json");
		String expected = "br.inpe.cap.asniffer.output.json.JSONReport";
		
		assertEquals(expected, actualClassName);
		
		assertEquals(expected, actualClassName);
		
	}
	
	@Test(expected = ReportTypeException.class)
	public void illegalReportType() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p any path -r other path -t wrong type for report".split(" ");
		mapper.map(args , Parameters.class);
		
		PropertiesUtil.getReportType("wrong type");
	}
	
}
