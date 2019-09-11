package ro.allevo.tracker.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.tracker.dao.OverallRestApiDao;

@Service
public class OverallService {

	@Autowired
	private OverallRestApiDao overallDao;
	
	public ObjectNode[] getReport(String timestamp) {
		return overallDao.getList(timestamp);
	}
	
	public ObjectNode[] trace() {
		return overallDao.trace();
	}
	
	public ObjectNode[] getTimestamps(Date date) {
		return overallDao.findTimestamps(date);
	}
}
