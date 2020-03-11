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

package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.RoutingSchemaRestApiDao;
import ro.allevo.fintpui.model.RoutingSchema;
import ro.allevo.fintpui.utils.PagedCollection;
@Service
public class RoutingSchemaService {

	@Autowired
	private RoutingSchemaRestApiDao routingSchemaDao;
	
	public RoutingSchema[] getAllRoutingSchemas() {
		return routingSchemaDao.getAllRoutingSchemas();
	}
	
	public PagedCollection<RoutingSchema> getPage() {
		return routingSchemaDao.getPage();
	}
	
	public ArrayList<String> getAllRoutingSchemaNames(){
		ArrayList<String> result = new ArrayList<>();
		for(RoutingSchema routingSchema : getAllRoutingSchemas()){
			result.add(routingSchema.getName());
		}
		return result;
	}
	
	public void insertRoutingSchema(RoutingSchema routingSchema) {
		routingSchemaDao.insertRoutingSchema(routingSchema);
	}

	public void updateRoutingSchema(int schemaId, RoutingSchema routingSchema){
		routingSchemaDao.updateRoutingSchema(schemaId, routingSchema);
	}
	
	public void deleteRoutingSchema(int schemaId){
		routingSchemaDao.deleteRoutingSchema(schemaId);
	}
	
	public RoutingSchema getRoutingSchema(int schemaId){
		return routingSchemaDao.getRoutingSchema(schemaId);
	}
	

	public Map<Integer,String> getAllRoutingSchemasNamesAndId() {
		Map<Integer,String> list = new HashMap<>();
		RoutingSchema[] routingSchemas = getAllRoutingSchemas();
		for(RoutingSchema routingSchema : routingSchemas){
			list.put(routingSchema.getId(), routingSchema.getName());
	        }
		
		return list;
	}
	
}
