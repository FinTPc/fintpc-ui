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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.dao.RoutingRuleRestApiDao;
import ro.allevo.fintpui.model.RoutingRule;
import ro.allevo.fintpui.utils.PagedCollection;
@Service
public class RoutingRulesService {
	
	@Autowired
	private RoutingRuleRestApiDao routingRuleDao;
	
	public RoutingRule[] getAllRoutingRules(){
		return routingRuleDao.getAllRoutingRules();
	}
	
	public PagedCollection<RoutingRule> getPage(final Integer schemaId) {
		LinkedHashMap<String, List<String>> params = new LinkedHashMap<String, List<String>>(){{
			if (null != schemaId)
				put("filter_routingSchemaId_exact", Arrays.asList(schemaId.toString()));
		}};

		return routingRuleDao.getPage(params);
	}
	
	public  RoutingRule[] getRoutingRulesBySchema(int schemaId) {
		return routingRuleDao.getRoutingRulesBySchema(schemaId);
	}
	
	public ArrayList<String> getAllRoutingRuleIds(){
		ArrayList<String> result = new ArrayList<>();
		for(RoutingRule routingRule : getAllRoutingRules()){
			result.add(""+routingRule.getId());
		}
		return result;
	}
	
	public ArrayList<String> getRoutingRulesIdsBySchema(int schemaId) {
		ArrayList<String> ruleIds = new ArrayList<>();
		for(RoutingRule routingRule : routingRuleDao.getRoutingRulesBySchema(schemaId)){
			ruleIds.add(""+routingRule.getId());
		}
		return ruleIds;
	}

	public void insertRoutingRule(RoutingRule routingRule){
		routingRuleDao.insertRoutingRule(routingRule);
	}
	
	public void updateRoutingRule(int ruleId, RoutingRule routingRule){
		routingRuleDao.updateRoutingRule(ruleId, routingRule);
	}
	
	public void deleteRoutingRule(int ruleId){
		routingRuleDao.deleteRoutingRule(ruleId);
	}
	
	public RoutingRule getRoutingRule(int ruleId){
		return routingRuleDao.getRoutingRule(ruleId);
	}
	
	public void copyRules(int sourceSchema, int destSchema){
		//get rules from source
		 RoutingRule[] rulesToBeCopied = routingRuleDao.getRoutingRulesBySchema(sourceSchema);
		
		//set them the new schema, then post
		for(RoutingRule rule : rulesToBeCopied){
			rule.setRoutingSchemaId(destSchema);
			routingRuleDao.insertRoutingRule(rule);
		}
	}
	
	public void deleteRulesFromSchema(int sourceSchema){
		
		RoutingRule[] rulesToBeCopied = routingRuleDao.getRoutingRulesBySchema(sourceSchema);
		
		//set them the new schema, then post
		for(RoutingRule rule : rulesToBeCopied){
			routingRuleDao.deleteRoutingRule(rule.getId());
		}
	}
	
	
	public Map<Integer, ArrayList<RoutingRule>> getRulesGroupedByQueues(RoutingRule[] rules){
		HashMap<Integer, ArrayList<RoutingRule>> map = new HashMap<>();
		for(RoutingRule rule : rules){
			int queueId = rule.getQueueId();
			if(!map.containsKey(queueId)){
				ArrayList<RoutingRule> routingRules = new ArrayList<>();
				routingRules.add(rule);
				map.put(queueId, routingRules);
			}else{
				map.get(queueId).add(rule);
			}
		}
		Map<Integer, ArrayList<RoutingRule>> sortedMap = new TreeMap<Integer, ArrayList<RoutingRule>>(map);
		return sortedMap;
	}
	
	
	
	public ObjectNode[] getActions() {
		return routingRuleDao.getActions();	
	}
}
	
