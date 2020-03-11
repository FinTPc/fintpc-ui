package ro.allevo.at.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.model.TxProcessingTest;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class TxProcessingTestRestApiDao extends RestApiDao<TxProcessingTest> implements TxProcessingTestDao {
	
	@Autowired
	Config config;

	public TxProcessingTestRestApiDao() {
		super(TxProcessingTest.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAtURL()).path("tests").build();
	}
	
	@Override
	public TxProcessingTest[] getAllProcessingTests() {
		return getAll();
	}

	@Override
	public TxProcessingTest getProcessingTest(Long id) {
		//de implementat - mom nu functioneaza
		return get(String.valueOf(id));
	}



}
