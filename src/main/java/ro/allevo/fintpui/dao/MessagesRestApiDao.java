package ro.allevo.fintpui.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class MessagesRestApiDao extends RestApiDao<ObjectNode> implements MessagesDao {

	@Autowired
	private Config config;

	public MessagesRestApiDao() {
		super(ObjectNode.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public URI getBaseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectNode[] getAllMessages(String businessArea, LinkedHashMap<String, List<String>> params) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("business-areas").path(businessArea).path("messages").build();
		return getAll(uri, params);
	}

	@Override
	public PagedCollection<ObjectNode> getMessages(String businessArea, LinkedHashMap<String, List<String>> params) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("business-areas").path(businessArea).path("messages").build();
		return getPage(uri, params);
	}

	@Override
	public ObjectNode getMessagesStatus(String bic, String paymentId) {
		bic= "CECEROBU";
		URI uri = UriBuilder.fromUri(config.getConnectUrl()).path(bic).path("payments").path("pain.001-sepa-credit-transfers").path(paymentId).path("status").build();
		return get(uri);
	}

	
	@Override
	public PagedCollection<ObjectNode> getMessages(String businessArea) {
		return getMessages(businessArea, new LinkedHashMap<String, List<String>>());
	}
	
	public ObjectNode getMessageStatus(String id) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("messages").build();
		return get(uri);
	}
	
	public PagedCollection<ObjectNode> getMessagesInQueue(String queueName, String messageType) {
		URI uri = UriBuilder.fromPath(config.getAPIUrl()).path("queues").path("by-name").path(queueName)
				.path("messages").path("by-message-type").path(messageType).build();

		return getPage(uri);
	}

	public PagedCollection<ObjectNode> getMessageType(LinkedHashMap<String, List<String>> params) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("messages").build();

		return getPage(uri, params);
	}

	public Long getMessageCountStatements(LinkedHashMap<String, List<String>> params) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("reports").path("message-statement").build();
		return getCount(uri, params);
	}

	@Override
	public ObjectNode getMessage(String businessArea, String id) {
		PagedCollection<ObjectNode> nodes = getMessages(businessArea, new LinkedHashMap<String, List<String>>() {
			{
				put("filter_correlationid_exact", new ArrayList<String>() {
					{
						add(id);
					}
				});
				put("total", Arrays.asList(""));
			}
		});

		if (null != nodes && nodes.getTotal() > 0)
			return nodes.getItems()[0];

		return null;
	}

	@Override
	public ObjectNode getEntryQueueMessage(String id) {
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("messages").path(id).path("entry-queue").build();

		return get(uri);
	}

	@Override
	public ObjectNode getFeedbackMessage(String id) {// correlation-id
		URI uri = UriBuilder.fromUri(config.getAPIUrl()).path("messages").path("by-correlation-id").path(id)
				.path("feedback").build();

		return get(uri);
	}
}
