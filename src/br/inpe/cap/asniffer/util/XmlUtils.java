package br.inpe.cap.asniffer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.output.ClassRepresentation;
import br.inpe.cap.asniffer.output.OutputRepresentation;
import br.inpe.cap.asniffer.output.PackageRepresentation;

public class XmlUtils {

	public static StringBuffer prepareCompilationUnitXML(List<ClassRepresentation> classes_){
		
		StringBuffer sb = new StringBuffer();
		
		for(ClassRepresentation class_ : classes_){//Class
			sb.append("\n\t\t\t<class name=\"" + class_.getClassName() + "\">");
			for(MetricRepresentation metric : class_.getMetricRepresentation()){
				sb.append("\n\t\t\t\t<metric alias=\"" + metric.getAlias());
				sb.append("\" name=\"" + metric.getName() + "\">");
				if(metric.isMultiMetric()){//Elements
					for(int i = 0; i < metric.getMultiMetricValue().size(); i++){
						sb.append("\n\t\t\t\t\t<element name=\"");
						sb.append(metric.getElementName().get(i));
						sb.append("\" ");
						sb.append("type=\"");
						sb.append(metric.getElementType().get(i));
						sb.append("\">");
						sb.append(metric.getMultiMetricValue().get(i));
						sb.append("</element>");
					}
					sb.append("\n\t\t\t\t</metric>");
				}else{
					sb.append(metric.getSingleMetricValue() + "</metric>");
				}
			}
			sb.append("\n\t\t\t</class>");
		}
		sb.append("\n");
		return sb;
		
	}

	public static StringBuffer preparePackage(PackageRepresentation package_) {

		StringBuffer sb = new StringBuffer();

		sb.append("\t\t<package name=\"" + package_.getName() + "\">");
		sb.append(prepareCompilationUnitXML(package_.getMetrics()));
		sb.append("\t\t</package>\n");
			
		return sb;
	}
	
	public static void writeXML(OutputRepresentation metricOutputRep){
		
		Writer out = null;
		StringBuffer sb = new StringBuffer();
		String cwd = System.getProperty("user.dir");

		try {
			File dir = new File(cwd + File.separator + metricOutputRep.getProjectName());
			dir.mkdir();
			out = new OutputStreamWriter(new FileOutputStream(dir.getAbsolutePath() + File.separator + 
								metricOutputRep.getProjectName() + "_asniffer_results.xml"));
			
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"metrics-results.xsl\"?>\n");
			sb.append("<AnnotationMetricsResults>\n");
			sb.append("\t<project name=\"" + metricOutputRep.getProjectName() + "\">\n");
			
			for(PackageRepresentation package_ : metricOutputRep.getPackages_()){//Package
				sb.append(preparePackage(package_));
			}
			sb.append("\t</project>\n");
			sb.append("</AnnotationMetricsResults>");
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
