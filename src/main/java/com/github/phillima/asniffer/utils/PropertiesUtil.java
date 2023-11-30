package com.github.phillima.asniffer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.github.phillima.asniffer.exceptions.ReportTypeException;

public class PropertiesUtil {
		
	//private String repType;
	
	String getReportType(String reportType) {
	//public static String getReportType(String reportType) {
		
		Properties prop = new Properties();
		String reportClassName = "";
		InputStream inputStream = null;
		
		try {
			inputStream = PropertiesUtil.class.getResourceAsStream("/report.properties");
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		reportClassName = prop.getProperty(reportType);
		if(reportClassName != null)
			return reportClassName;
		else
			throw new ReportTypeException("Wrong Report Type. Available options are json or xml");
	}	

//	private String getReportType( String reportType ) {
//		
//		Properties prop = new Properties();
//		//String reportClassName = "";
//		this.repType = "";
//		InputStream inputStream = null;
//		
//		try {
//			inputStream = PropertiesUtil.class.getResourceAsStream("/report.properties");
//			prop.load(inputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(inputStream != null)
//					inputStream.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		repType = prop.getProperty( reportType );
//		if(repType != null)
//			return repType;
//			//return reportClassName;
//		else
//			throw new ReportTypeException("Wrong Report Type. Available options are json or xml");
//		
//	}
	
}