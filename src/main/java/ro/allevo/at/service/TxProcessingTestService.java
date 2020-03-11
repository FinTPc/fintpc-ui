package ro.allevo.at.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.dao.TxProcessingTestRestApiDao;
import ro.allevo.at.model.TxProcessingTest;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TxProcessingTestService {
	
	@Autowired
	private TxProcessingTestRestApiDao txProcessingTestRestApiDao;
	
	public TxProcessingTest[] getAllProcessingTests() {
		return txProcessingTestRestApiDao.getAllProcessingTests();
	}
	
	public TxProcessingTest getProcessingTest(Long id) {
		return txProcessingTestRestApiDao.getProcessingTest(id);
	}
	
	public PagedCollection<TxProcessingTest> getPage(){
		return txProcessingTestRestApiDao.getPage();
	}
	
	public PagedCollection<TxProcessingTest> getPage(LinkedHashMap<String, List<String>> param){
		return txProcessingTestRestApiDao.getPage(param);
	}

}
