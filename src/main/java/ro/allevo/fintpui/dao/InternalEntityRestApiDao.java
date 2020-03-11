package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.InternalEntity;

@Service
public class InternalEntityRestApiDao extends RestApiDao<InternalEntity> implements InternalEntityDao{

	@Autowired
	Config config;
	
	public InternalEntityRestApiDao() {
		super(InternalEntity.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("internal-entities").build();
	}
	
	@Override
	public InternalEntity[] getAllInternalEntities() {
		return getAll();
	}

	@Override
	public InternalEntity getInternalEntity(String id) {
		return get(id);
	}

	@Override
	public void insertInternalEntity(InternalEntity internalEntity) {
		post(internalEntity);
	}

	@Override
	public void updateInternalEntity(InternalEntity internalEntity, String id) {
		put(id, internalEntity);
	}

	@Override
	public void deleteInternalEntity(String id) {
		delete(id);
	}

	@Override
	public String[] getInternalsByRights(String messagetype) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("byRights").queryParam("messageType", messagetype).build();
		return getList(uri, String.class);
	}



}
