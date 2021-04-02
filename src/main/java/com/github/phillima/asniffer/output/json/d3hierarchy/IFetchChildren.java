package com.github.phillima.asniffer.output.json.d3hierarchy;

import java.util.List;

import com.github.phillima.asniffer.model.PackageModel;

public interface IFetchChildren {
	
	public List<Children> fetchChildren(PackageModel package_);

}
