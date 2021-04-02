package com.github.phillima.test.output;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.phillima.asniffer.exceptions.ReportTypeException;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.parameters.ParamMapper;
import com.github.phillima.asniffer.parameters.Parameters;
import com.github.phillima.asniffer.utils.PropertiesUtil;

public class TestReport {

	private static AMReport report;
	private static IReport outputProcessor;
	static String testFilePath;
	
	@Test
	public void readReportType() {
		
		String actualClassName =  PropertiesUtil.getReportType("json");
		String expected = "com.github.phillima.asniffer.output.json.JSONReport";
		
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
