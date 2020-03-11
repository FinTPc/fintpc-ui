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

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	
		private String guid;
		private String additionalinfo;
		private String correlationid;
		private String innerexception;
		private String machine;
		private String message;
		private String type;
		private Timestamp eventdate;
		private Timestamp insertdate;
		private BigDecimal service;	
		private String clasS;
		private ServiceMap serviceMap;
		
		public String getAdditionalinfo() {
			return additionalinfo;
		}
		public void setAdditionalinfo(String additionalinfo) {
			this.additionalinfo = additionalinfo;
		}
		public String getCorrelationid() {
			return correlationid;
		}
		public void setCorrelationid(String correlationid) {
			this.correlationid = correlationid;
		}
		public String getGuid() {
			return guid;
		}
		public void setGuid(String guid) {
			this.guid = guid;
		}
		public String getInnerexception() {
			return innerexception;
		}
		public void setInnerexception(String innerexception) {
			this.innerexception = innerexception;
		}
		public String getMachine() {
			return machine;
		}
		public void setMachine(String machine) {
			this.machine = machine;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Timestamp getEventdate() {
			return eventdate;
		}
		public void setEventdate(Timestamp eventdate) {
			this.eventdate = eventdate;
		}
		public Timestamp getInsertdate() {
			return insertdate;
		}
		public void setInsertdate(Timestamp insertdate) {
			this.insertdate = insertdate;
		}
		public BigDecimal getService() {
			return service;
		}
		public void setService(BigDecimal service) {
			this.service = service;
		}
		public String getClasS() {
			return clasS;
		}
		public void setClasS(String clasS) {
			this.clasS = clasS;
		}
		public ServiceMap getServiceMap() {
			return serviceMap;
		}
		public void setServiceMap(ServiceMap serviceMap) {
			this.serviceMap = serviceMap;
		}		
}
