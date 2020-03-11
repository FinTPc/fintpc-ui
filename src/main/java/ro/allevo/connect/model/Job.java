package ro.allevo.connect.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
	
	private String name;
	private String group;
	private String details = "No details";
	private Map<String, String> params; 
	private String action;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		if(details != null && !"".equals(details)) {
			this.details = details;
		}	
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	@JsonGetter("rowid")
	public String getRowid() {
		return name;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
