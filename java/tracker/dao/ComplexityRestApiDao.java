package ro.allevo.tracker.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class ComplexityRestApiDao extends RestApiDao<ObjectNode> {

	@Autowired
	private Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getTrackerUrl()).path("complexity").build();
	}
	
	public ComplexityRestApiDao() {
		super(ObjectNode.class);
	}
}
