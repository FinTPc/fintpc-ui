package ro.allevo.connect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.ConsentRestApiDao;
import ro.allevo.connect.model.Consent;

@Service
public class ConsentService {
	
	@Autowired
	private ConsentRestApiDao consentDao;

	public Consent getConsent(String id) {
		return consentDao.get(id);
	}
	
	public void insertConsent(Consent consent) {
		consentDao.insertConsent(consent);
	}
	
	public ResponseEntity<String> deleteConsent(String bic, String consentId) {
		return consentDao.deleteConsent(bic, consentId);
	}
	
	public ResponseEntity<String> updateConsent(Consent consent, String id) {
		return consentDao.updateConsent(consent, id);
	}

}
