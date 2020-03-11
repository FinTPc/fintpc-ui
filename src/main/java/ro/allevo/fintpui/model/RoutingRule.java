/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpui.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutingRule {
	
	private int id;
	@NotNull(message="this field is required")
	private Integer queueId;	
	private String queueName;
	@NotNull
    @Size(min=1,max=70)
	private String description;	
    @Size(max=500)
	private String messageCondition;
    @Size(max=500)
	private String functionCondition;
    @Size(max=500)
	private String metadataCondition;
    @NotNull
    //@Size(min=1,message="this field is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+(\\([^)]+\\))?$", message = "this field is required")
	private String action;
    @NotNull(message="this field is required")
	private Integer routingSchemaId;
    @NotNull //nu cred ca se aplica.. int nu e nullable
    @Min(value = 1, message = "this field must be greater than 1")
	private int sequence;
    @NotNull
	private int ruleType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessageCondition() {
		return messageCondition;
	}

	public void setMessageCondition(String messageCondition) {
		this.messageCondition = messageCondition;
	}

	public String getFunctionCondition() {
		return functionCondition;
	}

	public void setFunctionCondition(String functionCondition) {
		this.functionCondition = functionCondition;
	}

	public String getMetadataCondition() {
		return metadataCondition;
	}

	public void setMetadataCondition(String metadataCondition) {
		this.metadataCondition = metadataCondition;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getRoutingSchemaId() {
		return routingSchemaId;
	}

	public void setRoutingSchemaId(Integer routingSchemaId) {
		this.routingSchemaId = routingSchemaId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getRuleType() {
		return ruleType;
	}

	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}

	public String getQueueName() {
		return queueName;
	}
	
	@JsonGetter("rowid")
	public long getRowid() {
		return id;
	}
	
}
