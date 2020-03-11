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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ServiceMap;
@Service
public class ServiceMapRestApiDao extends RestApiDao<ServiceMap> implements ServiceMapDao{

	@Autowired
	private Config config;
	
	public ServiceMapRestApiDao() {
		super(ServiceMap.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("service-maps").build();
	}
	
	@Override
	public ServiceMap[] getServiceMapList() {
		return getAll();
	}
	
	public ServiceMap[] getServiceMapList(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

	@Override
	public ArrayList<String> getServiceMapNamesList() {
		ArrayList<String> friendlyNames = new ArrayList<>();
		for(ServiceMap map : getServiceMapList()){
			if (map.getServiceType() == 1)
				friendlyNames.add(map.getName());
		}
		return friendlyNames;
	}
	
	@Override
	public ServiceMap getServiceMap(int id) {
		return get(id +"");
	}

	@Override
	public void updateServiceMap(int id, ServiceMap serviceMap) {
		put(id+"", serviceMap);
		
	}
}
