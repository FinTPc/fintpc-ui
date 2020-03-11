package ro.allevo.connect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.SchedulerRestApiDao;
import ro.allevo.connect.model.Scheduler;

@Service
public class SchedulerService {

	@Autowired
	private SchedulerRestApiDao schedulerRestApiDao;
	
	public boolean getSchedulerState(String id) {
		return getScheduler(id).isInStandbyMode();
	}
	
	public void updateScheduler(String id, Scheduler scheduler) {
		schedulerRestApiDao.updateScheduler(id, scheduler);
	}
	
	public Scheduler getScheduler(String id) {
		return schedulerRestApiDao.get(id);
	}
}
