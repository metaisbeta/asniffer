package br.inpe.cap.asniffer.output;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ClassMetricAdapter extends XmlAdapter<ClassMetricMapWrapper, Map<String,Integer>> {

	@Override
	public Map<String, Integer> unmarshal(ClassMetricMapWrapper v) throws Exception {
		return null;
	}

	@Override
	public ClassMetricMapWrapper marshal(Map<String,Integer> map) throws Exception {
		ClassMetricMapWrapper myWrapper = new ClassMetricMapWrapper();
        for (Entry<String,Integer> entry : map.entrySet()) {
        		ClassMetricMap myMap = new ClassMetricMap();
            myMap.setValue(entry.getValue());
            myMap.setMetricName(entry.getKey());
            myWrapper.addClassMetric(myMap);
        }
        return myWrapper;
    }
}
