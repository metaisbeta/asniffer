package com.github.phillima.asniffer.output.json.d3hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.phillima.asniffer.model.CodeElementType;
import com.google.gson.annotations.SerializedName;

public class Children {

	@SerializedName("name")
	private String name;
	
	private CodeElementType type;
	
	private Integer value = null;
	
	@SerializedName("properties")
	private Map<String, String> properties;
	
	@SerializedName("children")
	private List<Children> childrens;
	
	public Children(String name, CodeElementType type, Integer value) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.childrens = new ArrayList<Children>();
		this.properties = new HashMap<String, String>();
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
		this.properties.put(property, value);
	}
	
	public String getProperty(String property) {
		if(this.properties.containsKey(property))
			return this.properties.get(property);
		return null;
	}
	
	public CodeElementType getType() {
		return type;
	}
}