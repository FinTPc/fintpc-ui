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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TimeLimitsRestApiDao;
import ro.allevo.fintpui.model.TimeLimit;
import ro.allevo.fintpui.utils.PagedCollection;
@Service
public class TimeLimitsService {

	@Autowired
	private TimeLimitsRestApiDao timeLimitsDao;
	
	public TimeLimit[] getAllTimeLimits(){
		return timeLimitsDao.getAllTimeLimits();
	}
	
	public PagedCollection<TimeLimit> getPage() {
		return timeLimitsDao.getPage();
	}
	
	public TimeLimit getTimeLimit(int limitId){
		return timeLimitsDao.getTimeLimit(limitId);
	}
	public void insertTimeLimit(TimeLimit timelimit){
		timeLimitsDao.insertTimeLimit(timelimit);
	}
	
	public void updateTimeLimit(int limitId, TimeLimit timelimit){
		timeLimitsDao.updateTimeLimit(limitId, timelimit);
	}
	public void deleteTimeLimit(int limitId){
		timeLimitsDao.deleteTimeLimit(limitId);
	}
	
	public ArrayList<String> getAllTimeLimitNames() {
		TimeLimit[] timeLimits = getAllTimeLimits();
		ArrayList<String> result = new ArrayList<>();
 		for(TimeLimit timeLimit : timeLimits){
			result.add(timeLimit.getName());
		}
		return result;
	}
	
}
