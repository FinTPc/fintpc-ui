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
import ro.allevo.fintpui.model.RoutingSchema;
@Service
public class RoutingSchemaRestApiDao extends RestApiDao<RoutingSchema> implements RoutingSchemaDao{

	@Autowired
	Config config;
	
	public RoutingSchemaRestApiDao() {
		super(RoutingSchema.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("routing-schemas").build();
	}
	
	@Override
	public RoutingSchema[] getAllRoutingSchemas() {
		return getAll();
	}

	@Override
	public void insertRoutingSchema(RoutingSchema routingSchema) {
		post(routingSchema);
	}

	@Override
	public void updateRoutingSchema(int schemaId, RoutingSchema routingSchema) {
		put(schemaId+"", routingSchema);
	}

	@Override
	public void deleteRoutingSchema(int schemaId) {
		delete(schemaId+"");
	}

	@Override
	public RoutingSchema getRoutingSchema(int schemaId) {
		return get(schemaId+"");
	}

}
