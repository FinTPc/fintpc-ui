/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

package ro.allevo.fintpui.service;

import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ro.allevo.fintpui.dao.MessagesRestApiDao;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class MessageService {

	@Autowired
	private MessagesRestApiDao messagesDao;

//	public String getPayload(String correlid){
//		String payload = messagesDao.getPayload(correlid);
//		String path = getClass().getClassLoader()
//				.getResource(MessageController.NESTED_TABLES_XSLT).getPath();
//		String friendlyPayload = MessageController.applyXSLT(payload, path);
//		return friendlyPayload;
//	}
//
//	public String getImage(String correlid) {
//		String base64TiffImage = messagesDao.getImage(correlid);
//		
//		return base64TiffImage;
//	}
//	
//	public ArrayList<Object> getMessagesInReport(Map<String,String> requestParameters, StringBuilder total){
//		return messagesDao.getMeseagesInReport(requestParameters, total);
//	}
//	
//	/**
//	 * Factory method that returns a report message depending on its businessArea
//	 * @param id : the correlation id of the message
//	 * @param businessArea
//	 * @return
//	 */
//	public Object getMessageInReports(String id, String businessArea){
//		switch (businessArea) {
//		case MessageTypes.FT:
//			return messagesDao.getFundsTransferMessage(id);
//		case MessageTypes.DI:
//			return messagesDao.getDebitInstrumentsMessage(id);
//		case MessageTypes.DD:
//			return messagesDao.getDirectDebitMessage(id);
//		case MessageTypes.MFI:
//			return messagesDao.getMFIMessage(id);
//		default:
//			throw new RuntimeException("Requested unexpected type of business area");
//		}
//	}
//	
//	public ArrayList<MessageDuplicate> getDuplicateMessageDetails(
//			Map<String, String> allRequestParams) {
//		return messagesDao.getDuplicatesMessageDetails(allRequestParams);
//	}

	public ObjectNode[] getAllMessages(String businessArea, LinkedHashMap<String, List<String>> params) {
		if (null == params)
			params = new LinkedHashMap<String, List<String>>();

		return messagesDao.getAllMessages(businessArea, params);
	}

	public PagedCollection<ObjectNode> getMessages(String businessArea, LinkedHashMap<String, List<String>> params) {
		if (null == params)
			params = new LinkedHashMap<String, List<String>>();

		return messagesDao.getMessages(businessArea, params);
	}

	public ObjectNode getMessage(String businessArea, String id) {
		return messagesDao.getMessage(businessArea, id);
	}

	public ObjectNode getEntryQueueMessage(String id) {
		return messagesDao.getEntryQueueMessage(id);
	}

	public String getEntryQueuePayload(String id) {
		ObjectNode message = messagesDao.getEntryQueueMessage(id);

		return message.get("payload").asText();
	}

	public String getFeedbackPayload(String id) {
		ObjectNode message = messagesDao.getFeedbackMessage(id);

		return message.get("payload").asText();
	}

	public PagedCollection<ObjectNode> getMessagesInQueue(String queueName, String messageType) {
		return messagesDao.getMessagesInQueue(queueName, messageType);
	}

	public PagedCollection<ObjectNode> getMessagesType(LinkedHashMap<String, List<String>> filters) {
		return messagesDao.getMessageType(filters);
	}

	public Long getMessagesCountStatement(LinkedHashMap<String, List<String>> filters) {
		return messagesDao.getMessageCountStatements(filters);
	}
	
	public ObjectNode getMessageStatus(String bic, String paymentId) {
		return messagesDao.getMessagesStatus(bic, paymentId);
	}

}
