package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.QueueType;

@Service
public class QueueTypeRestApiDao extends RestApiDao<QueueType> implements QueueTypeDao {

	@Autowired
	Config config;
	
	public QueueTypeRestApiDao() {
		super(QueueType.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("queue-types").build();
	}
	
	@Override
	public QueueType[] getQueueTypes() {
		return getAll();
	}
}
