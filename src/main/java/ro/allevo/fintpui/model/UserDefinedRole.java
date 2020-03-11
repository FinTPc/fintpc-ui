package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDefinedRole {
	private String messageType;
	private String internalEntityName;
	
	public UserDefinedRole() {
		
	}
	
	public UserDefinedRole(String messageType, String internalEntityName) {
		this.messageType = messageType;
		this.internalEntityName = internalEntityName;
	}
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getInternalEntityName() {
		return internalEntityName;
	}
	public void setInternalEntityName(String internalEntityName) {
		this.internalEntityName = internalEntityName;
	}
}
