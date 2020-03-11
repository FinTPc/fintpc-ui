package ro.allevo.connect.dao;

import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Scheduler;

@Service
public interface SchedulerDao {
	
	public Scheduler getScheduler(String id);
	public void updateScheduler(String id, Scheduler scheduler);
}
