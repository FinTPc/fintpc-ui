package ro.allevo.tracker.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.tracker.dao.LiveRestApiDao;

@Service
public class LiveService {
	
	@Autowired
	private LiveRestApiDao liveDao;
	
	public ObjectNode getReport(int id) {
		return liveDao.get(id + "");
	}
	
	public ObjectNode trace(int time) {
		return liveDao.trace(time);
	}
	
	public ObjectNode[] getIntervals(Date date) {
		return liveDao.findIntervals(date);
	}
}
