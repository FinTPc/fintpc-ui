package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import ro.allevo.fintpui.dao.MessageTypesDao;
import ro.allevo.fintpui.model.MessageType;

@Service
public class MessageTypeService {

	@Autowired
	MessageTypesDao messageTypeDao;
	
	public MessageType[] getMessageTypes() {
		return messageTypeDao.getMessageTypes();
	}
	
	public String[] getBusinessAreas() {
		return messageTypeDao.getBusinessAreas();
	}
	
	public MessageType[] getMessageTypes(String businessArea) {
		return messageTypeDao.getMessageTypes(businessArea);
	}
	
	public MessageType getMessageType(String messageType) {
		return messageTypeDao.getMessageType(messageType);
	}
	
}
