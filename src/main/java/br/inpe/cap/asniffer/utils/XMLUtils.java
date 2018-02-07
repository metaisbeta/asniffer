package br.inpe.cap.asniffer.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.inpe.cap.asniffer.AMReport;

public class XMLUtils {

	public static void createXMLFile(AMReport report, String xmlPath) {
		 report.preapareXMLFiles();
		 try {
	        File file = new File(xmlPath + File.separator + report.getProjectName() + ".xml");
	        JAXBContext jaxbContext = JAXBContext.newInstance(AMReport.class);
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        jaxbMarshaller.marshal(report, file);
	        //Uncomment line below if you wish to see the generated XML on console output
	        //jaxbMarshaller.marshal(report, System.out);
	        System.out.println("XML file for project " + report.getProjectName() + " created on " + file.getAbsolutePath());

	      } catch (JAXBException e) {
	        e.printStackTrace();
	      }
	}
}