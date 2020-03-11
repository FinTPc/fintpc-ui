package ro.allevo.connect.dao;

import java.net.URI;

import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Trigger;

@Service
public interface TriggerDao {
	
	public Trigger[] getAllTriggers();
	public Trigger getTrigger(String id);
	public void insertTrigger(URI uri, Trigger trigger);
	public void updateTrigger(Trigger trigger, String id);
	public void deleteTrigger(String id);
}
