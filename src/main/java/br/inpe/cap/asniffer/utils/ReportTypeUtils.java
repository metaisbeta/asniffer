package br.inpe.cap.asniffer.utils;

import br.inpe.cap.asniffer.exceptions.ReportTypeException;
import br.inpe.cap.asniffer.output.IReport;

public class ReportTypeUtils {
	
	public static IReport getReportInstance(String reportType) {
		Object reportInstance = null;
		String classReport = "";
		try {
			classReport = PropertiesUtil.getReportType(reportType);
			Class<?> reportClazz = Class.forName(classReport);
			reportInstance = reportClazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if(reportInstance instanceof IReport)
			return (IReport) reportInstance;
		else
			throw new ReportTypeException("Report Type class" + classReport + " does not implement IReport interface");
	}

}
