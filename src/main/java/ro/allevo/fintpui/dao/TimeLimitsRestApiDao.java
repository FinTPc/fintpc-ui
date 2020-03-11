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
import ro.allevo.fintpui.model.TimeLimit;
@Service
public class TimeLimitsRestApiDao extends RestApiDao<TimeLimit> implements TimeLimitsDao {

	@Autowired
	Config config;
	
	public TimeLimitsRestApiDao() {
		super(TimeLimit.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("time-limits").build();
	}
	
	@Override
	public TimeLimit[] getAllTimeLimits() {
		return getAll();
	}

	@Override
	public void insertTimeLimit(TimeLimit timelimit) {
		post(timelimit);
	}

	@Override
	public void updateTimeLimit(int limitId, TimeLimit timelimit) {
		put(limitId+"", timelimit);
	}

	@Override
	public void deleteTimeLimit(int limitId) {
		delete(limitId+"");
	}

	@Override
	public TimeLimit getTimeLimit(int limitId) {
		return get(limitId+"");
	}
}
