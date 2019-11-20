package br.inpe.cap.asniffer.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapXMLAdapter extends XmlAdapter<MapXMLModel[], Map<String,Integer>> {

	@Override
	public MapXMLModel[] marshal(Map<String,Integer> map) throws Exception {
		
		MapXMLModel[] mappedElements = new MapXMLModel[map.size()];
		int i = 0;
		
		for (Map.Entry<String, Integer> classMetric : map.entrySet()) {
			mappedElements[i++] = new MapXMLModel(classMetric.getKey(),classMetric.getValue());
		}
		return mappedElements;
    }

	@Override
	public Map<String, Integer> unmarshal(MapXMLModel[] v) throws Exception {
		return null;
	}

}
