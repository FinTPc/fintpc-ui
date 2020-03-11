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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Queue {
	private int id;
	@NotNull
    @Size(min=1,max=50)
	private String name;
	 @Size(min=0,max=100)
	private String description;
	@NotNull
    @Size(min=1,max=50)
	private String label;
	@NotNull
    @Size(min=1,message="this field is required")
	private String holdStatus;
	private Integer exitPoint;
	private String exitPointName;
	@NotNull
	@Size(min=1,message="this field is required")
	private String queueTypeId;
	private Integer priority;
	 @Min(0)
	 @Max(1500)
	private Integer maxTrxOnBatch;

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getHoldStatus() {
		return holdStatus;
	}
	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}	
	public Integer getExitPoint() {
		return exitPoint;
	}
	public void setExitPoint(Integer exitPoint) {
		this.exitPoint = exitPoint;
	}
	@JsonGetter("exitPointName")
	public String getExitPointName() {
		return exitPointName;
	}
	public String getQueueTypeId() {
		return queueTypeId;
	}
	public void setQueueTypeId(String queueTypeId) {
		this.queueTypeId = queueTypeId;
	}
	public Integer getMaxTrxOnBatch() {
		return maxTrxOnBatch;
	}
	public void setMaxTrxOnBatch(Integer maxTrxOnBatch) {
		this.maxTrxOnBatch = maxTrxOnBatch;
	}
	@JsonGetter("rowid")
	public String getRowid() {
		return name;
	}
	
}
