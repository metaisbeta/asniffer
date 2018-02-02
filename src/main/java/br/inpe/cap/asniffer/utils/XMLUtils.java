package br.inpe.cap.asniffer.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.MetricResult;

public class XMLUtils {

	public static void createXMLFile(AMReport report, String xmlPath) {
		 report.preapareXMLFiles();
		 try {
	        File file = new File(xmlPath + report.getProjectName() + ".xml");
	        JAXBContext jaxbContext = JAXBContext.newInstance(AMReport.class);
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

	        // output pretty printed
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        jaxbMarshaller.marshal(report, file);
	        jaxbMarshaller.marshal(report, System.out);

	      } catch (JAXBException e) {
	        e.printStackTrace();
	      }
	}
}