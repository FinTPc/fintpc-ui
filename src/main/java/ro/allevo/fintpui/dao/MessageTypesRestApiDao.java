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

package ro.allevo.fintpui.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.MessageType;

@Service
public class MessageTypesRestApiDao extends RestApiDao<MessageType> implements MessageTypesDao {
	@Autowired
	Config config;
	
	public MessageTypesRestApiDao() {
		super(MessageType.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("message-types").build();
	}
	
	@Override
	public MessageType[] getMessageTypesInQueue(String queue) {
		URI url = UriBuilder.fromUri(config.getAPIUrl()).path("queues")
				.path("by-name").path(queue).path("message-types").build();
		return getAll(url);
		
		//return new ArrayList<MessageType>(Arrays.asList(messageTypes));
	}

	@Override
	public MessageType[] getMessageTypes() {
		return getAll();
	}

	@Override
	public MessageType[] getMessageTypes(String businessArea) {
		URI url = UriBuilder.fromUri(config.getAPIUrl()).path("business-areas")
				.path(businessArea).path("message-types").build();
		
		return getAll(url);
	}

	@Override
	public String[] getBusinessAreas() {
		URI url = UriBuilder.fromUri(config.getAPIUrl()).path("business-areas").build();
				
		return getList(url, String.class);
	}

	@Override
	public MessageType getMessageType(String messageType) {
		return get(messageType);
	}
}
	