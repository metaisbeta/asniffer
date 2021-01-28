package br.inpe.cap.asniffer.output.json.d3hierarchy.packageview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Children {

	@SerializedName("name")
	private String name;
	
	private String type;
	
	private Integer value = null;
	
	private Map<String, String> data;
	
	@SerializedName("children")
	private List<Children> childrens;
	
	public Children(String name, String type, Integer value) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.childrens = new ArrayList<Children>();
		this.data = new HashMap<String, String>();
	}
	
	public void addAllChidren(List<Children> childrens) {
		this.childrens.addAll(childrens);
	}
	
	public void addChildren(Children children) {
		this.childrens.add(children);
	}
	
	public List<Children> getChildrens(){
		return this.childrens;
	}
	
	public Children getChildByName(String childName) {
		for (Children children : childrens) {
			if(children.getName().equals(childName)) 
				return children;
		}
		return null;
	}

	public String getName() {
		return this.name;
	}
	
	public void addProperty(String property, String value) {
		this.data.put(property, value);
	}
}