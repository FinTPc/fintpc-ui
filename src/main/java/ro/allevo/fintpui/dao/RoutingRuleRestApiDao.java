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

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.RoutingRule;
import ro.allevo.fintpui.utils.PagedCollection;
@Service
public class RoutingRuleRestApiDao extends RestApiDao<RoutingRule> implements RoutingRuleDao {
	@Autowired
	Config config;

	public RoutingRuleRestApiDao() {
		super(RoutingRule.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("routing-rules").build();
	}
	
	@Override
	public RoutingRule[] getAllRoutingRules() {
		return getAll();
	}

	@Override
	public RoutingRule[] getRoutingRulesBySchema(int schemaId) {
		URI url = UriBuilder.fromUri(config.getAPIUrl()).path("routing-schemas")
				.path(schemaId+"").path("routing-rules").build();		
		return getAll(url);
	}
	
	public PagedCollection<RoutingRule> getPagedRoutingRulesBySchema(int schemaId) {
		URI url = UriBuilder.fromUri(config.getAPIUrl()).path("routing-schemas")
				.path(schemaId+"").path("routing-rules").build();	
		return getPage(url);
	}

	@Override
	public RoutingRule getRoutingRule(int id) {
		return get(id+"");
	}

	@Override
	public void insertRoutingRule(RoutingRule rule) {
		post(rule);
	}

	@Override
	public void updateRoutingRule(int id, RoutingRule rule) {
		put(id+"", rule);
	}

	@Override
	public void deleteRoutingRule(int id) {
		delete(id+"");
	}

	@Override
	public ObjectNode[] getActions() {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("routing-rules")
				.path("actions").build();
		
		return getList(uri, ObjectNode.class);
	}

}
