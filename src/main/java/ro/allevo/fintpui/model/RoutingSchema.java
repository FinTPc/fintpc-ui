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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutingSchema {
	
	private int id;
	
	@NotNull
    @Size(min=1,message="this field is required")
	private String name;
	@NotNull
    @Size(min=1,message="this field is required")
	private String description;
	private String sessionCode;
	@NotNull
	private Integer startLimit;
	@NotNull
	private Integer stopLimit;
	
	private TimeLimit startLimitEntity;
	
	private TimeLimit stopLimitEntity;
	
	//private Integer schemaCopy;
	
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
	public String getSessionCode() {
		return sessionCode;
	}
	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}
	public Integer getStartLimit() {
		return startLimit;
	}
	public void setStartLimit(Integer startLimit) {
		this.startLimit = startLimit;
	}
	public Integer getStopLimit() {
		return stopLimit;
	}
	public void setStopLimit(Integer stopLimit) {
		this.stopLimit = stopLimit;
	}
//	public Integer getSchemaCopy() {
//		return schemaCopy;
//	}
//	public void setSchemaCopy(Integer schemaCopy) {
//		this.schemaCopy = schemaCopy;
//	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TimeLimit getStartLimitEntity() {
		return startLimitEntity;
	}
	public TimeLimit getStopLimitEntity() {
		return stopLimitEntity;
	}
	
	@JsonGetter("rowid")
	public long getRowid() {
		return id;
	}
}
