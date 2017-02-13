package br.inpe.cap.asniffer.test;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.junit.Test;

import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.output.PackageRepresentation;
import br.inpe.cap.asniffer.util.XmlUtils;

public class XMLTest {

	private List<String> elementNames = new ArrayList<>();
	private List<Integer> elementType = new ArrayList<>();
	private List<Integer> elementValues = new ArrayList<>(); 
	
	private String XMLac="";
	private String XMLlocad="";
	
	
	//Basic XML
	@Test
	public void testACXML(){
		int values[] = {0,0,0,3,8,5,0};
		
		List<MetricRepresentation> metricsOut = new ArrayList<>();
		List<PackageRepresentation> packages_ = new ArrayList<>();
		PackageRepresentation package_;
		
		
		//for(int i = 0; i < values.length; i++)//Fill class
			//metricsOut.add(new MetricRepresentation("Class"+i, "AC", "Annotations in Class", values[i]));
		
		//for(int i = 0; i < 3; i++){//3 is a random number for packages
			//package_ = new PackageRepresentation("Package"+i, metricsOut);
			//packages_.add(package_);
		
		//XmlUtils.writeXML2(packages_, "AC", "Project");
	}
	@Test
	public void testLOCADXML(){
		elementNames.clear();
		elementValues.clear();
		int values[] = {0,0,0,3,8,5,0,12,13,56};
		
		List<MetricRepresentation> metricsOut = new ArrayList<>();
		List<PackageRepresentation> packages_ = new ArrayList<>();
		PackageRepresentation package_;
		
		for(int i = 0; i < values.length; i++){//Elements
			elementType.add(IType.ANNOTATION);
			elementValues.add(values[i]);
			elementNames.add("Element"+i);
		}
		
		/*for(int i = 0; i < values.length; i++)//Fill class
			metricsOut.add(new MetricRepresentation("ClassName"+i, "LOCAD", "Lines of Code in Annotation Declaration", 
							elementValues, elementNames, elementType));
		
		for(int i = 0; i < 3; i++){//3 is a random number for packages
			//package_ = new PackageRepresentation("Package"+i, metricsOut);
			//packages_.add(package_);
		}*/
		
		//XmlUtils.writeXML2(packages_, "LOCAD", "Project");
	}
	
	
	
}
