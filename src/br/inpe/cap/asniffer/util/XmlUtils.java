package br.inpe.cap.asniffer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import br.inpe.cap.asniffer.output.MetricOutputRepresentation;

public class XmlUtils {

	public static void writeXml(List<MetricOutputRepresentation> metricValues, String projectName) {
		Writer out = null;
		try {
			String folderPath = new File("").getAbsolutePath() + File.separator + "asniffer";
			
			StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			sb.append("<?xml-stylesheet type=\"text/xsl\" href=\"metrics-results.xsl\"?>\n");
			sb.append("<AnnotationMetricsResults project-name=\"");
			sb.append(projectName);
			sb.append("\">\n");
			sb.append(XmlUtils.get(metricValues));
			//sb.append(XmlUtils.tabText(badSmells));
			sb.append("</AnnotationMetricsResults>");

			Date today = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

			String xmlSource = folderPath + "metrics-results_"
					+ projectName + "_" + sdf.format(today) + ".xml";
			String htmlResult = folderPath + "metrics-results_"
					+ projectName + "_" + sdf.format(today) + ".html";
			 
			String path = "plugins/metrics-results.xsl";
			StreamSource xslStreamSource = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(path));
			
			out = new OutputStreamWriter(new FileOutputStream(xmlSource));
			out.write(sb.toString());
			out.close();
			/*TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(xslStreamSource);
			transformer.transform(new StreamSource(new StringReader(sb.toString())), new StreamResult(new FileOutputStream(htmlResult)));
			
			PlatformUI.getWorkbench().getBrowserSupport().createBrowser("internalBrowser").openURL(new URL("file://" + htmlResult));			
			*/
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

	private static String get(List<MetricOutputRepresentation> metricValues) {
		
		StringBuilder xml = new StringBuilder();
		
		for(MetricOutputRepresentation metricValue : metricValues){
			xml.append("<package name=\"");
			xml.append(metricValue.getPackage_());
			xml.append("\">\n");
			xml.append("\t<class name=\"");
			xml.append(metricValue.getClassName());
			xml.append("\">\n");
			xml.append("\t\t<metric alias=\"");
			xml.append(metricValue.getAlias());
			xml.append("\" name=\"");
			xml.append(metricValue.getName());
			xml.append("\">");
			if(metricValue.isMultiMetric()){
				xml.append("\t\t\t<element name=\"");
				xml.append(metricValue.getElementName());
				xml.append("\" ");
				xml.append("type=\"");
				xml.append(metricValue.getType());
				xml.append("\">");
				xml.append(metricValue.getMetricValue());
				xml.append("</element>\n");
				xml.append("\t\t</metric>\n");
			}else{
				xml.append(metricValue.getMetricValue());
				xml.append("</metric>\n");
			}
			xml.append("\t</class>\n");
			xml.append("</package>\n");
			
			
		}
		
		return xml.toString();
	}
	
}
