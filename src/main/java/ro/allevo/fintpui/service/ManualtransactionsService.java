package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ManualtransactionsRestApiDao;
import ro.allevo.fintpui.model.Manualtransactions;

@Service
public class ManualtransactionsService {
	
	@Autowired
	ManualtransactionsRestApiDao manualtransactionsDao;
	
	public ResponseEntity<String> insertManualtransactions(Manualtransactions payment){
		return manualtransactionsDao.insertManualtransactions(payment);
	}

}
