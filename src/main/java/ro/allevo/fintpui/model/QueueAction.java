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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueAction {
	private String href;
	@JsonProperty("_type")
	private String type;
	private String action;
	private String description;
	private Integer currmsg;
	private Integer selmsg;
	private Integer groupmsg;
	private Integer addoptions;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCurrmsg() {
		return currmsg;
	}
	public void setCurrmsg(Integer currmsg) {
		this.currmsg = currmsg;
	}
	public Integer getSelmsg() {
		return selmsg;
	}
	public void setSelmsg(Integer selmsg) {
		this.selmsg = selmsg;
	}
	public Integer getGroupmsg() {
		return groupmsg;
	}
	public void setGroupmsg(Integer groupmsg) {
		this.groupmsg = groupmsg;
	}
	public Integer getAddoptions() {
		return addoptions;
	}
	public void setAddoptions(Integer addoptions) {
		this.addoptions = addoptions;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}