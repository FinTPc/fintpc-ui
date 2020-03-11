package ro.allevo.connect.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Job;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class JobRestApiDao extends RestApiDao<Job> implements JobDao {

	@Autowired
	Config config;
	
	public JobRestApiDao() {
		super(Job.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).path("jobs").build();
	}

	@Override
	public Job[] getAllJobs() {
		return getAll();
	}

	@Override
	public Job getJob(String id) {
		return get(id);
	}

	@Override
	public void insertJob(Job job) {
		post(job);		
	}

	@Override
	public void updateJob(Job job, String id) {
		put(id, job);		
	}

	@Override
	public void deleteJob(String id) {
		delete(id);
	}
}
