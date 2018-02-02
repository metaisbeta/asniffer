package br.inpe.cap.asniffer.output;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/*public class ElementMetricAdapter extends XmlAdapter<List<ElementMetricMap>, Map<String, Map<String,Integer>>> {

	@Override
	public Map<String, Map<String, Integer>> unmarshal(List<ElementMetricMap> v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementMetricMap> marshal(Map<String, Map<String, Integer>> v) throws Exception {
		
		List<ElementMetricMap> myMaps = new ArrayList<>();
        for (Entry<String, Map<String, Integer>> entry : v.entrySet()) {
        		ElementMetricMap myMap = new ElementMetricMap();
        		myMap.metricName = entry.getKey();
        		for (Entry<String, Integer> elements : entry.getValue().entrySet()) {
        			ElementMetricColumn element = new ElementMetricColumn();
        			element.element = elements.getKey();
        			element.value = elements.getValue();
        			myMap.ele.add(element);
        		}
        		myMaps.add(myMap);
        }
        return myMaps;
	}
	
}*/

public class ElementMetricAdapter extends XmlAdapter<ElementMetricMap, Map<String, Map<String,Integer>>> {

	@Override
	public Map<String, Map<String, Integer>> unmarshal(ElementMetricMap v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementMetricMap marshal(Map<String, Map<String, Integer>> v) throws Exception {
		
		ElementMetricMap myMaps = new ElementMetricMap();
        for (Entry<String, Map<String, Integer>> entry : v.entrySet()) {
        		ElementMetricColumn element = new ElementMetricColumn();
        		element.element = entry.getKey();
        		for (Entry<String, Integer> elements : entry.getValue().entrySet()) {
        			ElementMetricValues metricValues = new ElementMetricValues();
        			metricValues.element = elements.getKey();
        			metricValues.value = elements.getValue();
        			element.metricValues.add(metricValues);
        		}
        		myMaps.elements.add(element);
        }
        return myMaps;
	}
	
}