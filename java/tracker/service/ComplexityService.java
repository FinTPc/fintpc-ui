package ro.allevo.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.tracker.dao.ComplexityRestApiDao;

@Service
public class ComplexityService {

	@Autowired
	private ComplexityRestApiDao complexityDao;
	
	public ObjectNode[] getAll() {
		return complexityDao.getList();
	}
	
}
