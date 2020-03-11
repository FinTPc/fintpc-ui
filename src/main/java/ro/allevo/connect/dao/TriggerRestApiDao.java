package ro.allevo.connect.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Trigger;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class TriggerRestApiDao extends RestApiDao<Trigger> implements TriggerDao{

	@Autowired
	Config config;
	
	public TriggerRestApiDao() {
		super(Trigger.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).path("triggers").build();
	}
	
	@Override
	public Trigger[] getAllTriggers() {
		return getAll();
	}

	@Override
	public Trigger getTrigger(String id) {
		return get(id);
	}

	@Override
	public void insertTrigger(URI uri, Trigger trigger) {
		post(uri, trigger);	
	}

	@Override
	public void updateTrigger(Trigger trigger, String id) {
		put(id, trigger);		
	}

	@Override
	public void deleteTrigger(String id) {
		delete(id);
	}
}
