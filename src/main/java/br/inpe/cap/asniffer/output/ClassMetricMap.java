package br.inpe.cap.asniffer.output;

import java.util.List;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ClassMetricMap {
	
	@XmlElementWrapper
	List<ClassMetricColumn> metric;
	
}
