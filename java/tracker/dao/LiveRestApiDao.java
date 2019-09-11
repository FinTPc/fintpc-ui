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
public class LiveRestApiDao extends RestApiDao<ObjectNode> implements LiveDao {

	@Autowired
	private Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getTrackerUrl()).path("live").build();
	}
	
	public LiveRestApiDao() {
		super(ObjectNode.class);
	}
	
	@Override
	public ObjectNode trace(int time) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("trace").queryParam("traceTime", time).build();
		
		return get(uri);
	}

	@Override
	public ObjectNode[] findIntervals(Date date) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path("intervals").queryParam("date", date).build();
		
		return getList(uri);
	}
}
