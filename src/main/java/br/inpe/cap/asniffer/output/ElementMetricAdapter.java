package br.inpe.cap.asniffer.output;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.inpe.cap.asniffer.ElementMetric;


public class ElementMetricAdapter extends XmlAdapter<ElementMetricMapWrapper, Map<String, List<ElementMetric>>> {

	@Override
	public Map<String, List<ElementMetric>> unmarshal(ElementMetricMapWrapper v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementMetricMapWrapper marshal(Map<String, List<ElementMetric>> v) throws Exception {
		ElementMetricMapWrapper elementMetricMapWrapper = new ElementMetricMapWrapper();
        for (Entry<String, List<ElementMetric>> entry : v.entrySet()) {
        		ElementMetricMap myMap = new ElementMetricMap();
        		myMap.metricName = entry.getKey();
    			myMap.elements = entry.getValue();
    			elementMetricMapWrapper.elementMetricMaps.add(myMap);
        }
        return elementMetricMapWrapper;
	}
	
}