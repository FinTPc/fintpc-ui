package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.UserAction;

@Service
public class UserActionRestApiDao extends RestApiDao<UserAction> implements UserActionDao {
	
	@Autowired
	Config config;
	
	public UserActionRestApiDao() {
		super(UserAction.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return null;
	}

	@Override
	public UserAction[] getSelectionUserActions(String queueName, String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues")
				.path(queueName).path("user-actions")
				.queryParam("area", "selection")
				.queryParam("messageType", messageType).build();
	
		return getList(uri);
	}
	
	@Override
	public UserAction[] getGroupUserActions(String queueName, String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues")
				.path(queueName).path("user-actions")
				.queryParam("area", "group")
				.queryParam("messageType", messageType).build();
		
		return getList(uri);
	}
}
