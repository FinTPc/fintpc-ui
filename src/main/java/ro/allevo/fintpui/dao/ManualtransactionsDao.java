package ro.allevo.fintpui.dao;

import org.springframework.http.ResponseEntity;

import ro.allevo.fintpui.model.Manualtransactions;

public interface ManualtransactionsDao {
	
	public ResponseEntity<String> insertManualtransactions(Manualtransactions payment);

}
