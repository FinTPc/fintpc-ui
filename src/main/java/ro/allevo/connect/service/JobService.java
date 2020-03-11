package ro.allevo.connect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.JobRestApiDao;
import ro.allevo.connect.model.Job;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class JobService {
	
	@Autowired
	JobRestApiDao jobRestApiDao;
	
	public Job[] getAllJobs() {
		return jobRestApiDao.getAllJobs();
	}
	
	public PagedCollection<Job> getPage() {
		return jobRestApiDao.getPage();
	}

	public Job getJob(String id) {
		return jobRestApiDao.getJob(id);
	}
	
	public void insertJob(Job job) {
		jobRestApiDao.insertJob(job);
	}
	
	public void updateJob(Job job, String id) {
		jobRestApiDao.updateJob(job, id);
	}
	
	public void deleteJob(String id) {
		jobRestApiDao.deleteJob(id);
	}

}
