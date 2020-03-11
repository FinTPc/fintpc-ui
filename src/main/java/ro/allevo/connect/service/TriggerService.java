package ro.allevo.connect.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.TriggerRestApiDao;
import ro.allevo.connect.model.Trigger;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TriggerService {
	
	@Autowired
	private TriggerRestApiDao triggerDao;
	
	public Trigger[] getAllTriggers() {
		return triggerDao.getAllTriggers();
	}
	
	public PagedCollection<Trigger> getPage() {
		return triggerDao.getPage();
	}

	public Trigger getTrigger(String id) {
		return triggerDao.getTrigger(id);
	}
	
	public void insertTrigger(URI uri, Trigger trigger) {
		triggerDao.insertTrigger(uri, trigger);
	}
	
	public void updateTrigger(Trigger trigger, String id) {
		triggerDao.updateTrigger(trigger, id);
	}
	
	public void deleteTrigger(String id) {
		triggerDao.deleteTrigger(id);
	}
}
