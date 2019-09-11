package ro.allevo.tracker.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.tracker.dao.ComponentRestApiDao;

@Service
public class ComponentService {

	@Autowired
	private ComponentRestApiDao componentDao;
	
	public ObjectNode[] getReport(String timestamp, String name, String thread) {
		return componentDao.getReport(timestamp, name, thread);
	}
	
	public ObjectNode[] trace() {
		return componentDao.trace();
	}
	
	public ObjectNode[] getTimestamps(Date date) {
		return componentDao.findTimestamps(date);
	}
}
