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
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Batch {
	private String id;
	private String name;
	private Integer count;
	private String date;
	private String sender;
	private String receiver;
	private String username;
	
	public Batch(ResultSet resultSet) throws SQLException {
		//id = resultSet.getString("id");
		name = resultSet.getString("filename");
		count = resultSet.getInt("txno");
		date = resultSet.getString("bdate");
		sender = resultSet.getString("sender");
		receiver = resultSet.getString("receiver");
		username = resultSet.getString("username");
	}
	
	public String getId() {
		return id;
	}
	public String getFilename() {
		return name;
	}
	public Integer getTxno() {
		return count;
	}
	public String getBdate() {
		return date;
	}
	public String getSender() {
		return sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public String getUsername() {
		return username;
	}
}
