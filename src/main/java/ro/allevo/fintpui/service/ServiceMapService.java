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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ServiceMapRestApiDao;
import ro.allevo.fintpui.model.ServiceMap;
import ro.allevo.fintpui.utils.PagedCollection;
@Service
public class ServiceMapService {

	@Autowired
	private ServiceMapRestApiDao serviceMapDao;
	
	public ServiceMap[] getServiceMapsList(){
		return serviceMapDao.getServiceMapList();
	}
	
	public ServiceMap[] getServiceMapsList(LinkedHashMap<String, List<String>> params){
		return serviceMapDao.getServiceMapList(params);
	}
	
	public PagedCollection<ServiceMap> getPage() {
		return serviceMapDao.getPage();
	}
	
	public ArrayList<String> getServiceMapNamesList(){
		ServiceMap[] services = getServiceMapsList();
		ArrayList<String> result = new ArrayList<>();
 		for(ServiceMap service : services){
			result.add(service.getName());
		}
		return result;
	}
	
	public void updateServiceMap(int id, ServiceMap serviceMap) {
		serviceMapDao.updateServiceMap(id, serviceMap);		
	}
	
	public ServiceMap getServiceMap(int id){
		return serviceMapDao.getServiceMap(id);
	}
	
	public Map<Integer,String> getAllServiceNamesAndId() {
		Map<Integer,String> list = new HashMap<>();
		ServiceMap[] services = getServiceMapsList();
		for(ServiceMap service : services){
			if(service.getServiceType()==1)
				list.put(service.getId(), service.getName());
	    }
		
		return list;
	}

	
}
