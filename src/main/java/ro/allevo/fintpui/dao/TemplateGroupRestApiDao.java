package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.TemplateGroup;

@Service
public class TemplateGroupRestApiDao extends RestApiDao<TemplateGroup> implements TemplateGroupDao{

	@Autowired
	private Config config;
	
	public TemplateGroupRestApiDao() {
		super(TemplateGroup.class);
	}

	@Override
	public ResponseEntity<String> insertTemplateGroup(TemplateGroup templateGroup) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("1").path("groups").build();
		return post(uri, templateGroup);
	}

	@Override
	public ResponseEntity<String> updateTemplateGroup(TemplateGroup templateGroup) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateGroup.getTxtemplate().getId()).path("groups").path("" + templateGroup.getId()).build();
		return put(uri, templateGroup);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates").build();
	}

	public TemplateGroup[] getAllTemplateGroups(Integer templateId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(""+templateId).path("groups").build();
		return getAll(uri);
	}

	public ResponseEntity<String> deleteTemplateGroup(Integer id) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("1").path("groups").path("" + id).build();
		return delete(uri);
	}
}
