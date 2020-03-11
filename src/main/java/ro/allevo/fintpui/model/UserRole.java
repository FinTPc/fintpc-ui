package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRole {
	public int id;
	public int userId;
	public int roleId;
	public Role roleEntity;
	public String action;
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public int getId() {
		return id;
	}
	
	public Role getRoleEntity() {
		return roleEntity;
	}
	
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
