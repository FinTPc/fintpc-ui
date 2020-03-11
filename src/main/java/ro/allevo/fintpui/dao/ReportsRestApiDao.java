package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;

@Service
public class ReportsRestApiDao extends RestApiDao<ObjectNode> implements ReportsDao {

	@Autowired
	Config config;

	public ReportsRestApiDao() {
		super(ObjectNode.class);
	}

	@Override
	public URI getBaseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectNode[] getTransactionsTableHeaders(String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues").path("table-headers")
				.queryParam("messageType", messageType).build();

		return getList(uri, ObjectNode.class);
	}

	@Override
	public ObjectNode[] getTransactionsGroupHeaders(String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues").path("group-headers")
				.queryParam("messageType", messageType).build();

		return getList(uri, ObjectNode.class);
	}

	@Override
	public ArrayNode getTransasctionsInGroup(String queueName, String messageType) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("queues").path("transactions-in-group")
				.queryParam("queueName", queueName).queryParam("messageType", messageType).build();

		return getObject(uri, ArrayNode.class);
	}

	@Override
	public ObjectNode[] getTransactionStates() {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("reports").path("transaction-states").build();

		return getList(uri);
	}

	@Override
	public ObjectNode[] getMessageCriteria(String businessArea) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("reports").path("message-criteria")
				.queryParam("businessArea", businessArea).build();

		return getList(uri);
	}

	@Override
	public ObjectNode[] getMessageResults(String businessArea) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("reports").path("message-results")
				.queryParam("businessArea", businessArea).build();

		return getList(uri);
	}

	@Override
	public ObjectNode[] getFilterResults(String businessArea, String userName) {
		URI uri = UriBuilder.fromUri(config.getUiUrl()).path("reports").path("filters")
				.queryParam("businessArea", businessArea).queryParam("user", userName).build();

		return getList(uri);
	}
}
