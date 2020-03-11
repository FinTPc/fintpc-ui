package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Template;
@Service
public class TemplatesRestApiDao extends RestApiDao<Template> implements TemplatesDao{

	@Autowired
	Config config;
	
	public TemplatesRestApiDao() {
		super(Template.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates").build();
	}

	public Template[] getAllTemplates() {
		return getAll();
	}

	public Template getTemplate(Integer id) {
		return get(String.valueOf(id));
	}
	
	public Template[] getTemplateConfigDetails(String id) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("by-id").path("").build();
		return getObject(uri, Template[].class);
	}
	
	public ResponseEntity<String> insertTemplate(Template template) {
		return post(template);
	}

	public void updateTemplate(Template template, Integer id) {
		put("" + id, template);
	}

	public void deleteTemplate(Integer id) {
		delete("" + id);
	}

	@Override
	public Template[] getAllTemplates(LinkedHashMap<String, List<String>> params) {
		return getAll(params);
	}

}
