package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.RolesDao;
import ro.allevo.fintpui.model.Role;

@Service
public class RoleService {
	
	@Autowired
	private RolesDao rolesDao;
	
	public Role[] getAllRoles() {
		return rolesDao.getAllRoles();
	}
	
	public Role getRole(int id) {
		return rolesDao.getRole(id);
	}
	
	public Role[] getUserDefinedRoles() {
		return rolesDao.getUserDefinedRoles();
	}
	
	public Role[] getApplicationRoles() {
		return rolesDao.getApplicationRoles();
	}
	
	public void insertRole(Role role) {
		rolesDao.insertRole(role);
	}
	
	public void updateRole(Role role, int id) {
		rolesDao.updateRole(role, id);
	}
	
	public void deleteRole(int id) {
		rolesDao.deleteRole(id);
	}
}
