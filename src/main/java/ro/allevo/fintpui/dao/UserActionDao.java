package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.UserAction;

@Service
public interface UserActionDao {
	public UserAction[] getGroupUserActions(String queueName, String messageType);
	public UserAction[] getSelectionUserActions(String queueName, String messageType);
}
