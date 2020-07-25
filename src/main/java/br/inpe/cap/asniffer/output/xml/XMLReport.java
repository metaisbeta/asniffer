package br.inpe.cap.asniffer.output.xml;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;

public class XMLReport implements IReport {

	ProjectReportXML projectReport;
	private static final Logger logger = 
		      LogManager.getLogger(XMLReport.class);
	
	@Override
	public void generateReport(AMReport report, String path) {
		
		projectReport = prepareXML(report);
		
		Path xmlFilePath = Paths.get(path + File.separator + report.getProjectName() + ".xml").normalize();
		
		try {
	        File file = xmlFilePath.toFile();
	        JAXBContext jaxbContext = JAXBContext.newInstance(ProjectReportXML.class);
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        jaxbMarshaller.marshal(projectReport, file);
	        //Uncomment line below if you wish to see the generated XML on console output
	        //jaxbMarshaller.marshal(report, System.out);
	        logger.info("XML file for project " + report.getProjectName() + " created on " + file.getAbsolutePath());
	      } catch (JAXBException e) {
	        e.printStackTrace();
	      }
		
	}
	
	private ProjectReportXML prepareXML(AMReport report) {
		
		ProjectReportXML projectXMLReport = new ProjectReportXML(report.getProjectName());
		
		for (PackageModel package_ : report.getPackages()) {
			PackageReportXML packageXML = new PackageReportXML(package_.getPackageName());
			packageXML.setClassReportXML(fetchClassReport(package_));
			projectXMLReport.addPackageXML(packageXML);
		}
				
		return projectXMLReport;
		
	}

	private List<ClassReportXML> fetchClassReport(PackageModel package_) {

		List<ClassReportXML> classesReportXML = new ArrayList<ClassReportXML>();
		
		for (ClassModel classReport : package_.all()) {
			ClassReportXML classReportXML = new ClassReportXML(classReport.getClassName(), classReport.getType());
			classReportXML.setClassMetricsXML(fetchClassMetrics(classReport, classReportXML));
			classReportXML.setCodeElementXMLReport(fetchCodeElementReport(classReport, classReportXML));
			classReportXML.setAnnotationSchemas(classReport.getAnnotationSchemas());
			classesReportXML.add(classReportXML);
		}
		
		return classesReportXML;
	}

	private List<CodeElementXMLReport> fetchCodeElementReport(ClassModel classReport, ClassReportXML classReportXML) {
		List<CodeElementXMLReport> codeElementReportsXML = new ArrayList<>();
		
		for (CodeElementModel codeElementXMLReport : classReport.getElementsReport()) {
			CodeElementXMLReport codeElementReport = new CodeElementXMLReport
								(codeElementXMLReport.getElementName(), 
								 codeElementXMLReport.getType(), 
								 codeElementXMLReport.getLine(),
								 codeElementXMLReport.getAed());
			codeElementReport.setAnnotationXMlReport(fetchAnnotationMetricReport(codeElementXMLReport, codeElementReport));
			codeElementReportsXML.add(codeElementReport);
		}
		
		return codeElementReportsXML;
		
	}

	private List<AnnotationXMLReport> fetchAnnotationMetricReport(CodeElementModel codeElementXMLReport,
			CodeElementXMLReport codeElementReport) {

		List<AnnotationXMLReport> annotationMetricsXMLReport = new ArrayList<>();
		
		for (AnnotationMetricModel annotationMetricModel : codeElementXMLReport.getAnnotationMetrics()) {
			AnnotationXMLReport annotationXMLReport = new AnnotationXMLReport(
							annotationMetricModel.getName(), 
							annotationMetricModel.getLine(),
							annotationMetricModel.getSchema());
			annotationXMLReport.setAnnotationMetrics(annotationMetricModel.getAnnotationMetrics());
			annotationMetricsXMLReport.add(annotationXMLReport);
		}
		
		return annotationMetricsXMLReport;
	}

	private List<ClassMetricXML> fetchClassMetrics(ClassModel classReport, ClassReportXML classReportXML) {
		List<ClassMetricXML> classMetricsXML = new ArrayList<>();
		
		classReport.getAllClassMetrics().forEach((metricName, metricValue) -> {
			ClassMetricXML classMetricXML = new ClassMetricXML(metricName, metricValue);
			classMetricsXML.add(classMetricXML);
		});
		
		return classMetricsXML;
	}
}
