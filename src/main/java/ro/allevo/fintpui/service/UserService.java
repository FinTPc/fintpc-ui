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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.dao.UserDao;
import ro.allevo.fintpui.dao.UserRoleDao;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserRole;
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	public User[] getAllUsers() {
		return userDao.getAllUsers();
	}
	
	public void insertUser(User user) {
		userDao.insertUsers(new User[] {user});
	}
	
	public void insertUsers(User[] users) {
		userDao.insertUsers(users);
	}

	public void updateUser(int userid, User user){
		userDao.updateUser(userid, user);
	}
	
	public void deleteUser(int userid){
		userDao.deleteUser(userid);
	}
	
	public void deleteUsers(int[] userids){
		userDao.deleteUsers(userids);
	}
	
	public User getUser(String username){
		return userDao.getUser(username);
	}
	public User getUserById(int userid){
		return userDao.getUser(userid);
	}
	
	public UserRole[] getUserRoles(String username) {
		return userRoleDao.getRoles(username);
	}
	
	public void updateUserRoles(String username, UserRole[] userRoles) {
		userRoleDao.deleteRoles(username);
		
		if (userRoles.length > 0)
			userRoleDao.addRoles(username, userRoles);
	}
	
	public List<String> getUserList() {
		User[] users = getAllUsers();
		
		List<String> result = new ArrayList<String>();
		
		for (User user : users)
			result.add(user.getUsername());
		
		return result;
	}
	
	public ObjectNode[] getUsersFromProvider() {
		return userDao.getUsersFromProvider();
	}
}
