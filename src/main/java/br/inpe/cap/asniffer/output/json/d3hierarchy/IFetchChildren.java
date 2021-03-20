package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.util.List;

import br.inpe.cap.asniffer.model.PackageModel;

public interface IFetchChildren {
	
	public List<Children> fetchChildren(PackageModel package_);

}
