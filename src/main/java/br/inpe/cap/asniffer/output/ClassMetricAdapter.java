package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ClassMetricAdapter extends XmlAdapter<ClassMetricMap, Map<String,Integer>> {

	@Override
	public Map<String, Integer> unmarshal(ClassMetricMap v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassMetricMap marshal(Map<String,Integer> map) throws Exception {
		ClassMetricMap myMap = new ClassMetricMap();
        myMap.metric = new ArrayList<ClassMetricColumn>();
        for (Entry<String,Integer> entry : map.entrySet()) {
        		ClassMetricColumn col = new ClassMetricColumn();
            col.value = entry.getValue();
            col.metricName = entry.getKey();
            myMap.metric.add(col);
        }
        return myMap;
    }
}
