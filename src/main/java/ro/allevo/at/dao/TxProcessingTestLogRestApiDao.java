package ro.allevo.at.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.model.TxProcessingTestLog;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class TxProcessingTestLogRestApiDao extends RestApiDao<TxProcessingTestLog> implements TxProcessingTestLogDao {
	
	@Autowired
	Config config;

	public TxProcessingTestLogRestApiDao() {
		super(TxProcessingTestLog.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAtURL()).path("tests").path("processing-log").build();
	}

	@Override
	public void insertTestLog(TxProcessingTestLog txProcessingTestLog) {
		post(txProcessingTestLog);
		
	}
}
