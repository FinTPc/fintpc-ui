package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.UserRole;

@Service
public interface UserRoleDao {
	public void addRoles(String username, UserRole[] roles);
	public void deleteRoles(String username);
	public UserRole[] getRoles(String username);
}
