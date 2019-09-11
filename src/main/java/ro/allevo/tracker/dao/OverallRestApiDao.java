package ro.allevo.tracker.dao;

import java.net.URI;
import java.sql.Date;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class OverallRestApiDao extends RestApiDao<ObjectNode> {

	@Autowired
	private Config config;
	
	public OverallRestApiDao() {
		super(ObjectNode.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getTrackerUrl()).path("overall").build();
	}
	
	public ObjectNode[] trace() {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("trace").build();
		
		return getList(uri);
	}
	
	public ObjectNode[] findTimestamps(Date date) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("timestamps").queryParam("date", date).build();
		
		return getList(uri);
	}

}
