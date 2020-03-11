package ro.allevo.fintpui.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAction {
	
	private String name;
	
	private String label;
	
	private int selectedMessage;
	
	private int groupMessage;
	
	private List<UserActionCode> userActionCodeEntity;

	private int detailsInput;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDetailsInput() {
		return detailsInput;
	}

	public void setDetailsInput(int detailsInput) {
		this.detailsInput = detailsInput;
	}

	@JsonIgnore
	public int getSelectedMessage() {
		return selectedMessage;
	}

	@JsonProperty
	public void setSelectedMessage(int selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	@JsonIgnore
	public int getGroupMessage() {
		return groupMessage;
	}

	@JsonProperty
	public void setGroupMessage(int groupMessage) {
		this.groupMessage = groupMessage;
	}

	public List<UserActionCode> getUserActionCodeEntity() {
		return userActionCodeEntity;
	}

	public void setUserActionCodeEntity(List<UserActionCode> userActionCodeEntity) {
		this.userActionCodeEntity = userActionCodeEntity;
	}
}
