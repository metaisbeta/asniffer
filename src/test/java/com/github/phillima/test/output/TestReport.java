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

	static String testFilePath;
	
	PropertiesUtil props = new PropertiesUtil();
	
	@Test
	public void readReportType() {
		String actualClassName =  this.props.getReportType("json");
		String expected = "com.github.phillima.asniffer.output.json.JSONReport";
		
		assertEquals(expected, actualClassName);
		
	}
	
	@Test(expected = ReportTypeException.class)
	public void illegalReportType() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p any path -r other path -t wrong type for report".split(" ");
		mapper.map(args , Parameters.class);
		
		this.props.getReportType("wrong type");
	}
	
	public PropertiesUtil getProps() {
		return props;
	}

}
