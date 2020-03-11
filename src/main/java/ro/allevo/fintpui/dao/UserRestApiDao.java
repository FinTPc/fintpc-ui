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
import ro.allevo.fintpui.model.User;
@Service
public class UserRestApiDao extends RestApiDao<User> implements UserDao {

	@Autowired
	Config config;

	public UserRestApiDao() {
		super(User.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("users").build();
	}
	
	@Override
	public User[] getAllUsers() {
		return getAll();
	}

	@Override
	public User getUser(String username) {
		return get(username);
	}

	@Override
	public User getUser(int userid) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-id")
				.path(userid+"").build();
		
		return get(uri);
	}

	@Override
	public void updateUser(int userid, User user) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-id")
				.path(userid+"").build();
		
		put(uri, user);
	}

	@Override
	public void deleteUser(int userid) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-id")
				.path(userid+"").build();
		
		delete(uri);
	}
	
	@Override
	public void deleteUsers(int[] userids) {
		delete(getBaseUrl(), userids);
	}

	@Override
	public void insertUsers(User[] users) {
		post(users);
	}

	@Override
	public ObjectNode[] getUsersFromProvider() {
		URI uri = UriBuilder.fromUri(config.getAPIAuthUrl()).path("users").build();
		
		return getList(uri, ObjectNode.class);
	}
}
