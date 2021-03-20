package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.PackageModel;

public class FetchSystemViewIMP implements IFetchChildren {

	@Override
	public List<Children> fetchChildren(PackageModel package_) {
		
		List<Children> annotationSV = new ArrayList<Children>();
		
		Map<String, Integer> schemaMap = new HashedMap<String, Integer>();
		
		for (ClassModel classReport : package_.getResults()) {
			if(classReport.getClassMetric("AC")==0)//Eliminate classes without annotation
				continue;
			
			classReport.getAnnotationSchemasMap().forEach((name,schema) -> {
				schemaMap.compute(schema, (k,v) -> (v == null ? 0 : v) + 1);
			});
		}
		schemaMap.forEach((k,v) -> {
			Children annotaSV = 
					new Children(k,"schema",v);
			annotationSV.add(annotaSV);
		});
		return annotationSV;
	}
	
	
}
