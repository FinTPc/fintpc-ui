package ro.allevo.at.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.dao.TxProcessingTestLogRestApiDao;
import ro.allevo.at.model.TxProcessingTestLog;

@Service
public class TxProcessingTestLogService {

	@Autowired
	private TxProcessingTestLogRestApiDao txProcessingTestLogRestApiDao;
	
	public void insertTxProcessingTestLog(TxProcessingTestLog txProcessingTestLog) {
		txProcessingTestLogRestApiDao.insertTestLog(txProcessingTestLog);
	}
}
