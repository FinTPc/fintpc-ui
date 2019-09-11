package ro.allevo.tracker.dao;

import java.sql.Date;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public interface LiveDao {

	public ObjectNode trace(int time);
	
	public ObjectNode[] findIntervals(Date date);
}
