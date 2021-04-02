package com.github.phillima.test.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.phillima.asniffer.parameters.ParamMapper;
import com.github.phillima.asniffer.parameters.ParameterReadingException;
import com.github.phillima.asniffer.parameters.Parameters;

public class TestParameters {
	
	@Test
	public void simpleTextParam() {
		ParamMapper mapper = new ParamMapper();
		String[] args = "-p /src/text".split(" ");
		Parameters param = mapper.map(args, Parameters.class);
		
		assertTrue(param.isProjPathPresent());
		assertEquals("/src/text", param.getProjectPath());
		assertFalse(param.isReportPathPresent());
		assertEquals("/src/text", param.getReportPath());
		assertFalse(param.isReportTypePresent());
		assertEquals("json", param.getReportType());
		assertFalse(param.isMultiProjectPresent());
		assertEquals("single", param.getMultiProject());
	}
	
	@Test
	public void compositeTextParam() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p composite text -r other text -t xml".split(" ");
		Parameters param = mapper.map(args , Parameters.class);
		
		assertEquals("composite text", param.getProjectPath());
		assertEquals("other text", param.getReportPath());
		assertTrue(param.isReportTypePresent());
		assertEquals("xml", param.getReportType());
		assertFalse(param.isMultiProjectPresent());
		assertEquals("single", param.getMultiProject());
	}
	
	@Test
	public void mandatoryParameterPresent() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p mandatory text -r project path".split(" ");
		Parameters param = mapper.map(args , Parameters.class);
		
		assertTrue(param.isProjPathPresent());
		assertEquals("mandatory text", param.getProjectPath());
		assertTrue(param.isReportPathPresent());
		assertEquals("project path", param.getReportPath());
		assertFalse(param.isReportTypePresent());
		assertEquals("json", param.getReportType());
		assertFalse(param.isMultiProjectPresent());
		assertEquals("single", param.getMultiProject());
		
	}
	
	@Test(expected=ParameterReadingException.class)
	public void mandatoryParamenterMissing() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-r other text".split(" ");
		mapper.map(args , Parameters.class);
	}
	
	@Test(expected = ParameterReadingException.class)
	public void mandatoryParamenterEmpty() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p -r other text".split(" ");
		mapper.map(args , Parameters.class);
	}
	
	@Test
	public void emptyReportPath() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p any path -r -t wrong type for report".split(" ");
		Parameters param = mapper.map(args , Parameters.class);
		
		assertTrue(param.isProjPathPresent());
		assertEquals("any path", param.getProjectPath());
		assertTrue(param.isReportPathPresent());
		assertEquals("any path", param.getReportPath());
	}
	
	@Test
	public void emptyReportType() {
		ParamMapper mapper = new ParamMapper();
		String[] args ="-p any path -r other path -t".split(" ");
		Parameters param = mapper.map(args , Parameters.class);
		
		assertTrue(param.isProjPathPresent());
		assertEquals("any path", param.getProjectPath());
		assertTrue(param.isReportPathPresent());
		assertEquals("other path", param.getReportPath());
		assertTrue(param.isReportTypePresent());
		assertEquals("json", param.getReportType());
		assertFalse(param.isMultiProjectPresent());
		assertEquals("single", param.getMultiProject());
	}
	
}
