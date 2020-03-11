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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceMap {

	private Integer id;
	private Integer ioIdentifier;
	private String name;
	
	private Integer serviceType;
	private Integer duplicateCheck;
	private String delayedNotificationQueue;
	private String duplicateMap;
	private String duplicateNotificationQueue;
	private String duplicateQueue;
	private String exitPoint;
	private String partner;

	
	public Integer getDuplicateCheck() {
		return duplicateCheck;
	}
	public void setDuplicateCheck(Integer duplicateCheck) {
		this.duplicateCheck = duplicateCheck;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIoIdentifier() {
		return ioIdentifier;
	}
	public void setIoIdentifier(Integer ioIdentifier) {
		this.ioIdentifier = ioIdentifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getServiceType() {
		return serviceType;
	}
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	public String getDelayedNotificationQueue() {
		return delayedNotificationQueue;
	}
	public void setDelayedNotificationQueue(String delayedNotificationQueue) {
		this.delayedNotificationQueue = delayedNotificationQueue;
	}
	public String getDuplicateMap() {
		return duplicateMap;
	}
	public void setDuplicateMap(String duplicateMap) {
		this.duplicateMap = duplicateMap;
	}
	public String getDuplicateNotificationQueue() {
		return duplicateNotificationQueue;
	}
	public void setDuplicateNotificationQueue(String duplicateNotificationQueue) {
		this.duplicateNotificationQueue = duplicateNotificationQueue;
	}
	public String getDuplicateQueue() {
		return duplicateQueue;
	}
	public void setDuplicateQueue(String duplicateQueue) {
		this.duplicateQueue = duplicateQueue;
	}
	public String getExitPoint() {
		if (null != exitPoint && !exitPoint.trim().isEmpty())
			return exitPoint;
		
		return null;
	}
	public void setExitPoint(String exitPoint) {
		this.exitPoint = exitPoint;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	@JsonGetter("rowid")
	public long getRowid() {
		return id;
	}
	
}
