package ro.allevo.connect.dao;

import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Job;

@Service
public interface JobDao {
	
	public Job[] getAllJobs();
	public Job getJob(String id);
	public void insertJob(Job job);
	public void updateJob(Job job, String id);
	public void deleteJob(String id);

}
