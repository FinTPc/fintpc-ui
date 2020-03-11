package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ExternalEntity;

@Service
public class ExternalEntityRestApiDao extends RestApiDao<ExternalEntity> implements ExternalEntityDao{

	@Autowired
	Config config;
	
	public ExternalEntityRestApiDao() {
		super(ExternalEntity.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("external-entities").build();
	}
	
	@Override
	public ExternalEntity[] getAllExternalEntities() {
		return getAll();
	}

	@Override
	public ExternalEntity getExternalEntity(String id) {
		return get(id);
	}

	@Override
	public void insertExternalEntity(ExternalEntity externalEntity) {
		post(externalEntity);
	}

	@Override
	public void updateExternalEntity(ExternalEntity externalEntity, String id) {
		put(id, externalEntity);
	}

	@Override
	public void deleteExternalEntity(String id) {
		delete(id);
	}



}
