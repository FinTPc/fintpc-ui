package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserActionCode {

	private String code;
	
	private String label;
	
	private String description;

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}
}
