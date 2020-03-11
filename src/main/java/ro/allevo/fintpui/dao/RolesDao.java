package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.Role;

@Service
public interface RolesDao {
	public Role[] getAllRoles();
	public Role getRole(int id);
	public Role[] getUserDefinedRoles();
	public Role[] getApplicationRoles();
	public void insertRole(Role role);
	public void updateRole(Role role, int id);
	public void deleteRole(int id);
}
