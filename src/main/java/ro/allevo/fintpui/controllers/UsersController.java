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

package ro.allevo.fintpui.controllers;

import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Role;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.model.UserRole;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.RoleService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.editors.UserEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MessageTypeService messageTypeService;
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	
	
	private static Logger logger = LogManager.getLogger(UsersController.class.getName());

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//custom user from json converter
	    binder.registerCustomEditor(User.class, new UserEditor());
	}
	
	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printUsers(ModelMap model, OAuth2Authentication auth){
		logger.info("/users requested");
		
		if(!auth.getAuthorities().contains(
				new SimpleGrantedAuthority(Roles.USERS_MODIFY))) {
			throw new NotAuthorizedException();
		}
		User[] users = userService.getAllUsers();
		Role[] userDefinedRoles = roleService.getUserDefinedRoles();
		Role[] applicationRoles = roleService.getApplicationRoles();
		
		model.addAttribute("users", users);
		model.addAttribute("userDefinedRoles", userDefinedRoles);
		model.addAttribute("applicationRoles", applicationRoles);
		
		return "users";
	}
	
	@RequestMapping(value="/sync/provider", method = RequestMethod.GET)
	@ResponseBody
	public String getProviderUsers() throws JsonProcessingException {
		return JSONHelper.toString(getNotMyUsers());
	}
	
	private List<ObjectNode> getNotMyUsers() {
		ObjectNode[] providerUsers = userService.getUsersFromProvider();
		User[] users = userService.getAllUsers();
		
		List<String> myUsers = new ArrayList<String>();
		List<ObjectNode> notMyUsers = new ArrayList<ObjectNode>();
		
		for (User user : users)
			myUsers.add(user.getUsername());
		
		for (ObjectNode user : providerUsers)
			if (!myUsers.contains(user.get("username").asText()))
				notMyUsers.add(user);
		
		Collections.sort(notMyUsers, (a, b) -> a.get("username").asText().compareTo(b.get("username").asText()));
		
		return notMyUsers;
	}
	
	@RequestMapping(value="/sync/deleted", method = RequestMethod.GET)
	@ResponseBody
	public String getDeleteUsers() throws JsonProcessingException {
		return JSONHelper.toString(getNotProviderUsers());
	}
	
	private List<User> getNotProviderUsers() {
		ObjectNode[] providerUsers = userService.getUsersFromProvider();
		User[] users = userService.getAllUsers();
		
		List<String> proUsers = new ArrayList<String>();
		List<User> notProviderUsers = new ArrayList<User>();
		
		for (ObjectNode user : providerUsers)
			proUsers.add(user.get("username").asText());
		
		for (User user : users)
			if (!proUsers.contains(user.getUsername()))
				notProviderUsers.add(user);
		
		Collections.sort(notProviderUsers, (a, b) -> a.getUsername().compareTo(b.getUsername()));
		
		return notProviderUsers;
	}
	
	@RequestMapping(value="/sync", method = RequestMethod.POST)
	@ResponseBody
	public String syncUsers() {
		List<ObjectNode> notMyUsers = getNotMyUsers();
		List<User> notProviderUsers = getNotProviderUsers();
		
		User[] importingUsers = new User[notMyUsers.size()];
		int[] removingUsers = new int[notProviderUsers.size()];
		
		int i=0;
		for (ObjectNode u : notMyUsers)
			importingUsers[i++] = new User() {{
				setUsername(u.get("username").asText());
				setEmail(u.get("email").asText());
			}};
		
		i=0;
		for (User u : notProviderUsers)
			removingUsers[i++] = u.getId();
		
		if (importingUsers.length > 0)
			userService.insertUsers(importingUsers);
		
		if (removingUsers.length > 0)
			userService.deleteUsers(removingUsers);
		
		return "[]";
	}
	
	/*
	 * INSERT
	 */
	@RequestMapping(value = "/add", method=RequestMethod.GET)
	public String addUser(ModelMap model, @ModelAttribute("user") User user){
		logger.info("/addUser page requested");
		model.addAttribute("formAction", "users/insert");
		return "users_add";
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	public String insertUser(@Valid @ModelAttribute("user") User user, 
			BindingResult bindingResult) throws JsonProcessingException{
		logger.info("/insert user requested");
		
		if (bindingResult.hasErrors())
            return JSONHelper.toString(bindingResult.getAllErrors());
		
		userService.insertUser(user);
		//if the user chose to initialize schema with copies from other schema, do so
		return "[]";
	}
	
	/*
	 * EDIT
	 */
	@RequestMapping(value = "/{username}/edit")
	public String editUser(ModelMap model, @PathVariable String username){
		logger.info("/editUser requested");
		User user = userService.getUser(username);
		model.addAttribute("user",user);
		model.addAttribute("formAction", "users/update");
		return "users_add";
	}
	
	//!!!EXACT PARAM ORDER !!!
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateUser(@Valid @ModelAttribute("user") User user, 
			BindingResult bindingResult) throws JsonProcessingException{
		logger.info("/update user requested");
		
		if (bindingResult.hasErrors())
            return JSONHelper.toString(bindingResult.getAllErrors());
		
		userService.updateUser(user.getId(), user);
		return "[]";
	}
	
	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{userid}/delete")
	public String deleteUser(@PathVariable int userid){
		logger.info("/delete user requested");
		userService.deleteUser(userid);
		return "redirect:/users";
	}
	
	@RequestMapping(value = "/roles/add")
	public String roles(ModelMap model, @ModelAttribute("role") Role role) {
		MessageType[] messageTypes = messageTypeService.getMessageTypes();
		InternalEntity[] internalEntities = internalEntitiesService.getAllInternalEntities();
		
		model.addAttribute("userDefined", 1);
		model.addAttribute("messageTypes", messageTypes);
		model.addAttribute("internalEntities", internalEntities);
		
		model.addAttribute("formAction", "users/roles/insert");
		
		return "roles_add";
	}
	
	@RequestMapping(value = "/roles/insert", method = RequestMethod.POST)
	@ResponseBody
	public String insertRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult) throws JsonProcessingException{
		logger.info("/insert role requested");
		
		if (bindingResult.hasErrors())
            return JSONHelper.toString(bindingResult.getAllErrors());
		
		roleService.insertRole(role);
		
		return "[]";
	}
	
	@RequestMapping(value = "/roles/{roleid}/delete", method = RequestMethod.GET)
	public String deleteRole(@PathVariable int roleid){
		roleService.deleteRole(roleid);
		
		return "redirect:/users";
	}
	
	@RequestMapping(value = "/roles/{roleid}/edit")
	public String editRole(ModelMap model, @PathVariable int roleid){
		logger.info("/editRole requested");
		Role role = roleService.getRole(roleid);
		
		MessageType[] messageTypes = messageTypeService.getMessageTypes();
		InternalEntity[] internalEntities = internalEntitiesService.getAllInternalEntities();
		
		model.addAttribute("role", role);
		model.addAttribute("messageTypes", messageTypes);
		model.addAttribute("internalEntities", internalEntities);
		
		model.addAttribute("formAction", "users/roles/update");
		return "roles_add";
	}
	
	@RequestMapping(value = "/roles/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateRole(@Valid @ModelAttribute("role") Role role, 
			BindingResult bindingResult,
			@RequestParam("id") int roleid) throws JsonProcessingException{
		if (bindingResult.hasErrors())
            return JSONHelper.toString(bindingResult.getAllErrors());
		
		roleService.updateRole(role, roleid);
		return "[]";
	}
	
	@RequestMapping(value = "/{username}/roles")
	@ResponseBody
	public String getUserRoles(@PathVariable String username) throws JsonProcessingException {
		UserRole[] userRoles = userService.getUserRoles(username);
		
		return JSONHelper.toString(userRoles); 
	}
	
	@RequestMapping(value = "/{username}/update-roles", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void updateUserRoles(@PathVariable String username, @RequestBody UserRole[] userRoles) {
		userService.updateUserRoles(username, userRoles);
	}

}