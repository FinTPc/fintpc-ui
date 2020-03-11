package ro.allevo.connect.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Consent;

@Service
public interface ConsentDao {
	
	public Consent getConsent(String id);
	public ResponseEntity<String> insertConsent(Consent consect);
	public ResponseEntity<String> updateConsent(Consent consect, String id);
	public ResponseEntity<String> deleteConsent(String bic, String consentId);

}
