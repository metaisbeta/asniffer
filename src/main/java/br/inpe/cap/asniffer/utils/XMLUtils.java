package br.inpe.cap.asniffer.utils;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.inpe.cap.asniffer.output.ProjectPojo;

public class XMLUtils {

	public static void createXMLFile(ProjectPojo projects) {
		
		 try {
	        File file = new File(projects.getProjectName()+".xml");
	        JAXBContext jaxbContext = JAXBContext.newInstance(ProjectPojo.class);
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

	        // output pretty printed
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        jaxbMarshaller.marshal(projects, file);
	        jaxbMarshaller.marshal(projects, System.out);

	      } catch (JAXBException e) {
	        e.printStackTrace();
	      }
	}

}