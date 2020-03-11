package ro.allevo.at.dao;

import org.springframework.stereotype.Service;

import ro.allevo.at.model.TxProcessingTest;

@Service
public interface TxProcessingTestDao {

	public TxProcessingTest[] getAllProcessingTests();
	public TxProcessingTest getProcessingTest(Long id);
}
