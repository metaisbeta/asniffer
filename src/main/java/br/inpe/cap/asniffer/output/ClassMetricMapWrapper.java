package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ClassMetricMapWrapper {
	
	@XmlElementWrapper
	private List<ClassMetricMap> metric;
	
	public ClassMetricMapWrapper() {this.metric = new ArrayList<>();}
	
	public void addClassMetric(ClassMetricMap classMetricMap) {
		metric.add(classMetricMap);
	}
}
