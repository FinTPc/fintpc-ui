package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Role;

@Service
public class RolesRestApiDao extends RestApiDao<Role> implements RolesDao {

	@Autowired
	Config config;
	
	public RolesRestApiDao() {
		super(Role.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("roles").build();
	}
	
	@Override
	public Role[] getAllRoles() {
		return getAll();
	}

	@Override
	public Role[] getUserDefinedRoles() {
		return getAll(new LinkedHashMap<String, List<String>>() {{
			put("filter_userDefined_exact", new ArrayList<String>() {{add("1");}});
			put("sort_field", new ArrayList<String>() {{add("name");}});
		}});
	}

	@Override
	public Role[] getApplicationRoles() {
		return getAll(new LinkedHashMap<String, List<String>>() {{
			put("filter_userDefined_exact", new ArrayList<String>() {{add("0");}});
			put("sort_field", new ArrayList<String>() {{add("name");}});
		}});
	}

	@Override
	public void insertRole(Role role) {
		post(role);
	}

	@Override
	public void updateRole(Role role, int id) {
		put(id+"", role);
	}

	@Override
	public void deleteRole(int id) {
		delete(id+"");
	}

	@Override
	public Role getRole(int id) {
		return get(id+"");
	}
}
