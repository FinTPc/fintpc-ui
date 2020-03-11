package ro.allevo.at.dao;

import org.springframework.stereotype.Service;

import ro.allevo.at.model.TxProcessingTestLog;

@Service
public interface TxProcessingTestLogDao {

	public void insertTestLog(TxProcessingTestLog txProcessingTestLog);
}
