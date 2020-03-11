package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.TemplateDetailed;

@Service
public class TemplateDetailedRestApiDao extends RestApiDao<TemplateDetailed> implements TemplateDetailedDao{

	@Autowired
	Config config;

	public TemplateDetailedRestApiDao() {
		super(TemplateDetailed.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates").build();
	}

	@Override
	public TemplateDetailed[] getAllTemplateDetailed(Integer configId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + configId).path("fields").build();
		return getAll(uri);
	}

	@Override
	public TemplateDetailed getTemplateDetailed(Integer configId) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResponseEntity<String> insertTemplateDetailed(TemplateDetailed templateDetailed) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateDetailed.getTxtemplate().getId()).path("fields").build();
		return post(uri, templateDetailed);
	}

	public ResponseEntity<String> updateTemplateDetailed(TemplateDetailed templateDetailed) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateDetailed.getTxtemplate().getId()).path("fields").path("" + templateDetailed.getId()).build();
		return put(uri, templateDetailed);
	}

	@Override
	public ResponseEntity<String> deleteTemplateDetailedByTemplateId(Integer templateId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateId).path("fields").build();
		return delete(uri);
	}

}
