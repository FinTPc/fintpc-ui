package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.dao.ReportsDao;
import ro.allevo.fintpui.model.MessagesGroup;

@Service
public class ReportService {

	@Autowired
	ReportsDao reportsDao;

	public ObjectNode[] getTableHeaders(String messageType) {
		return reportsDao.getTransactionsTableHeaders(messageType);
	}

	public ObjectNode[] getGroupHeaders(String messageType) {
		return reportsDao.getTransactionsGroupHeaders(messageType);
	}

	public List<MessagesGroup> getTransactionsInGroup(String queueName, String messageType) {
		List<MessagesGroup> transactions = new ArrayList<MessagesGroup>();

		ArrayNode result = reportsDao.getTransasctionsInGroup(queueName, messageType);

		result.forEach((node) -> transactions.add(new MessagesGroup((ArrayNode) node)));

		return transactions;
	}

	public List<String> getTransactionStatuses() {
		ObjectNode[] nodes = reportsDao.getTransactionStates();

		List<String> statuses = new ArrayList<String>();

		for (ObjectNode node : nodes)
			statuses.add(node.get("status").asText());

		return statuses;
	}

	public ObjectNode[] getMessageCriteria(String businessArea) {
		return reportsDao.getMessageCriteria(businessArea);
	}

	public ObjectNode[] getMessageResults(String businessArea) {
		return reportsDao.getMessageResults(businessArea);
	}
	
	public ObjectNode[] getFilterResults(String businessArea, String userName) {
		return reportsDao.getFilterResults(businessArea, userName);
	}
}
