package ro.allevo.fintpui.model;

public class RoutingJobParameters {
	
	private String action;
	
	private String reason;
	
	private String actionDetails;
	
	private String messageType;
	
	
	private String[] messageIds;
	
	
	private String[] groupKeys;
	
	private String[] timeKeys;
	
	private String[][] fieldValues;
	
	
	private RoutingJobParameters(String action, String reason, String actionDetails, 
			String messageType) {
		this.action = action;
		this.reason = reason;
		this.actionDetails = actionDetails;
		this.messageType = messageType;
	}
	
	public RoutingJobParameters(String action, String reason, String actionDetails, 
			String messageType, String[] messageIds) {
		this(action, reason, actionDetails, messageType);
		
		this.messageIds = messageIds;
	}
	
	public RoutingJobParameters(String action, String reason, String actionDetails, 
			String messageType,
			String[] groupKeys, String[] timeKeys, String[][] fieldValues) {
		this(action, reason, actionDetails, messageType);
		
		this.groupKeys = groupKeys;
		this.timeKeys = timeKeys;
		this.fieldValues = fieldValues;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getActionDetails() {
		return actionDetails;
	}

	public void setActionDetails(String actionDetails) {
		this.actionDetails = actionDetails;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String[] getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(String[] messageIds) {
		this.messageIds = messageIds;
	}

	public String[] getGroupKeys() {
		return groupKeys;
	}

	public void setGroupKeys(String[] groupKeys) {
		this.groupKeys = groupKeys;
	}

	public String[] getTimeKeys() {
		return timeKeys;
	}

	public void setTimeKeys(String[] timeKeys) {
		this.timeKeys = timeKeys;
	}

	public String[][] getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(String[][] fieldValues) {
		this.fieldValues = fieldValues;
	}

}
