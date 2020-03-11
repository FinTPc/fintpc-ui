package ro.allevo.connect.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Scheduler;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class SchedulerRestApiDao extends RestApiDao<Scheduler> implements SchedulerDao{
	
	@Autowired
	Config config;
	
	public SchedulerRestApiDao() {
		super(Scheduler.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).path("scheduler").build();
	}

	@Override
	public Scheduler getScheduler(String id) {
		return get(id);
	}

	@Override
	public void updateScheduler(String id, Scheduler scheduler) {
		put(id, scheduler);		
	}
}
