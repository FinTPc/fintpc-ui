package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.TemplateConfigDetailed;

@Service
public class TemplateConfigDetailedRestApiDao extends RestApiDao<TemplateConfigDetailed> implements TemplateConfigDetailedDao{

	@Autowired
	Config config;

	public TemplateConfigDetailedRestApiDao() {
		super(TemplateConfigDetailed.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates-config").build();
	}

	@Override
	public TemplateConfigDetailed[] getAllTemplateConfigDetailed(Integer configId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + configId).path("fields").build();
		return getAll(uri);
	}

	@Override
	public TemplateConfigDetailed getTemplateConfigDetailedById(Integer configId, Long fieldId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + configId).path("fields").path("" + fieldId).build();
		return get(uri);
	}

	@Override
	public ResponseEntity<String> insertTemplatesConfigDetailed(TemplateConfigDetailed templateConfigDetailed) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateConfigDetailed.getTxtemplatesconfig().getId()).path("fields").build();
		return post(uri, templateConfigDetailed);
	}

	@Override
	public Response deleteTemplatesConfigDetailedsByConfigId(Integer configId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + configId).path("fields").build();
		ResponseEntity<String> del = delete(uri); 
		return Response.ok(del).build();
	}

	public ResponseEntity<String> updateTempatesConfigDetailed(TemplateConfigDetailed templateConfigDetailed) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("" + templateConfigDetailed.getTxtemplatesconfig().getId()).path("fields").path(""+templateConfigDetailed.getId()).build();
		return put(uri, templateConfigDetailed);
	}

	@Override
	public Response deleteTemplatesConfigDetailedById(Integer id) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("fields").path("" + id).build();
		delete(uri);
		String del = delete(uri).getBody(); 
		return Response.ok(del).build();
	}

}
