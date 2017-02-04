package br.inpe.cap.asniffer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.eclipse.jface.dialogs.MessageDialog;

import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.output.ClassRepresentation;
import br.inpe.cap.asniffer.output.MetricOutputRepresentation;
import br.inpe.cap.asniffer.output.PackageRepresentation;

public class XmlUtils {

	public static void writeXMLBeginning(String projectName, String metricAlias){
		Writer out = null;
		try {
			String rootPath = new File("").getAbsolutePath();
			File f = new File(rootPath + File.separator + projectName);
			f.mkdir();
			
			String folderPath = f.getAbsolutePath() + File.separator + "asniffer";
			
			StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"metrics-results.xsl\"?>\n");
			sb.append("<AnnotationMetricsResults project-name=\"");
			sb.append(projectName);
			sb.append("\">\n");
			Date today = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + metricAlias + ".xml";
			out = new OutputStreamWriter(new FileOutputStream(xmlSource));
			out.write(sb.toString());
			out.close();
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void writeXMLEnd(String projectName, String metricAlias){
		Writer out = null;
		try {
			String folderPath = new File("").getAbsolutePath() + File.separator + projectName + File.separator + "asniffer";
			StringBuffer sb = new StringBuffer();
			sb.append("</AnnotationMetricsResults>");
			
			//Date today = new Date(System.currentTimeMillis());
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

			//String xmlSource = folderPath + "metrics-results_"
			//		+ projectName + "_" + metricAlias + "_" + sdf.format(today) + ".xml";
			//String htmlResult = folderPath + "metrics-results_"
			//		+ projectName + "_" + sdf.format(today) + ".html";
			
			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + metricAlias + ".xml";
			
			out = new OutputStreamWriter(new FileOutputStream(xmlSource, true));
			out.append(sb.toString());
			out.close();
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void writeXml(List<MetricRepresentation> metricValues, String projectName, String metricAlias) {
		Writer out = null;
		try {
			String folderPath = new File("").getAbsolutePath() + File.separator + projectName + File.separator + "asniffer";
			StringBuffer sb = new StringBuffer();
			sb.append(XmlUtils.get(metricValues));
			sb.append("\t</class>\n");

			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + metricAlias + ".xml";
			out = new OutputStreamWriter(new FileOutputStream(xmlSource, true));
			out.append(sb.toString());
			out.close();
			
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static String get(List<MetricRepresentation> metricValues) {
		
		StringBuilder xml = new StringBuilder();
		boolean flag = false;
		
		for(MetricRepresentation metricValue : metricValues){
			
			if(!flag){
				xml.append("\t\t<metric alias=\"");
				xml.append(metricValue.getAlias());
				xml.append("\" name=\"");
				xml.append(metricValue.getName());
				xml.append("\">");
			}
			
			if(metricValue.isMultiMetric()){
				xml.append("\n\t\t\t<element name=\"");
				xml.append(metricValue.getElementName());
				xml.append("\" ");
				xml.append("type=\"");
				xml.append(metricValue.getElementType());
				xml.append("\">");
				xml.append(metricValue.getSingleMetricValue());
				xml.append("</element>");
				//xml.append("\t\t</metric>\n");
				flag = true;
			}else{
				xml.append(metricValue.getSingleMetricValue());
				xml.append("</metric>\n");
			}
		}
		
		if(flag)
			xml.append("\n\t\t</metric>\n");
		
		
		return xml.toString();
	}

	public static void writePackage(String packageName, String projectName, String metricAlias) {

		Writer out = null;
		try {
			String folderPath = new File("").getAbsolutePath() + File.separator + projectName + File.separator + "asniffer";
			StringBuffer sb = new StringBuffer();
			
			sb.append("<package name=\"");
			sb.append(packageName);
			sb.append("\">\n");
			
			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + metricAlias + ".xml";
			out = new OutputStreamWriter(new FileOutputStream(xmlSource, true));
			out.append(sb.toString());
			out.close();
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}

	public static void writeClass(String className, String projectName, String metricAlias) {
		
		Writer out = null;
		try {
			String folderPath = new File("").getAbsolutePath() + File.separator + projectName + File.separator + "asniffer";
			
			StringBuffer sb = new StringBuffer();
			sb.append("\t<class name=\"");
			sb.append(className);
			sb.append("\">\n");
			
			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + metricAlias + ".xml";
			out = new OutputStreamWriter(new FileOutputStream(xmlSource, true));
			out.append(sb.toString());
			out.close();
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void finishPackage(String projectName, String metricAlias) {
		
		Writer out = null;
		try {
			String folderPath = new File("").getAbsolutePath() + File.separator + projectName + File.separator + "asniffer";
			StringBuffer sb = new StringBuffer();
			
			sb.append("</package>\n");
			
			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + metricAlias + ".xml";
			out = new OutputStreamWriter(new FileOutputStream(xmlSource, true));
			out.append(sb.toString());
			out.close();
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public static StringBuffer prepareCompilationUnitXML(List<ClassRepresentation> classes_){
		
		StringBuffer sb = new StringBuffer();
		
		for(ClassRepresentation class_ : classes_){//Class
			sb.append("\n\t<class name=\"" + class_.getClassName() + "\">");
			for(MetricRepresentation metric : class_.getMetricRepresentation()){
				sb.append("\n\t\t<metric alias=\"" + metric.getAlias());
				sb.append("\" name=\"" + metric.getName() + "\">");
				if(metric.isMultiMetric()){//Elements
					for(int i = 0; i < metric.getMultiMetricValue().size(); i++){
						sb.append("\n\t\t\t<element name=\"");
						sb.append(metric.getElementName().get(i));
						sb.append("\" ");
						sb.append("type=\"");
						sb.append(metric.getElementType().get(i));
						sb.append("\">");
						sb.append(metric.getMultiMetricValue().get(i));
						sb.append("</element>");
					}
					sb.append("\n\t\t</metric>");
				}else{
					sb.append(metric.getSingleMetricValue() + "</metric>");
				}
			}
			sb.append("\n\t</class>");
		}
		sb.append("\n");
		return sb;
		
	}

	public static StringBuffer preparePackage(PackageRepresentation package_) {

		Writer out = null;
		StringBuffer sb = new StringBuffer();

		sb.append("<package name=\"" + package_.getName() + "\">");
		sb.append(prepareCompilationUnitXML(package_.getMetrics()));
		sb.append("</package>\n");
			
		return sb;
	}
	
	public static void writeXML2(MetricOutputRepresentation metricOutputRep){
		
		Writer out = null;
		StringBuffer sb = new StringBuffer();
		String cwd = new File("").getAbsolutePath();

		try {
			File dir = new File(cwd + File.separator + metricOutputRep.getProjectName());
			dir.mkdir();
			out = new OutputStreamWriter(new FileOutputStream(dir.getAbsolutePath() + File.separator + 
								metricOutputRep.getProjectName() + ".xml"));
			
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"metrics-results.xsl\"?>\n");
			sb.append("<AnnotationMetricsResults/>\n");
			sb.append("<project name=\"" + metricOutputRep.getProjectName() + "\"/>\n");
			
			for(PackageRepresentation package_ : metricOutputRep.getPackages_()){//Package
				sb.append(preparePackage(package_));
			}
			
			out.append(sb.toString());
			out.close();
		} catch (Exception ex) {
			MessageDialog.openInformation(null, "Error", ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		}
}
