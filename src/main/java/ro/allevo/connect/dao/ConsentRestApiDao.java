package ro.allevo.connect.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Consent;
import ro.allevo.fintpui.config.Config;

@Service
public class ConsentRestApiDao extends ApiDao<Consent> implements ConsentDao {

	@Autowired
	Config config;
	
	public ConsentRestApiDao() {
		super(Consent.class);
	}

	@Override
	public Consent getConsent(String bic) {
		return get(bic);
	}

	@Override
	public ResponseEntity<String> insertConsent(Consent consent) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(consent.getBic()).path("consents").build();
		return post(uri, consent);
	}

	@Override
	public ResponseEntity<String> updateConsent(Consent consent, String bic) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(bic).path("consents").path(bic).build();
		return put(uri, consent);		
	}

	@Override
	public ResponseEntity<String> deleteConsent(String bic, String consentId) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(bic).path("consents").path(consentId).build();
		return delete(uri);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).build();
	}
}
