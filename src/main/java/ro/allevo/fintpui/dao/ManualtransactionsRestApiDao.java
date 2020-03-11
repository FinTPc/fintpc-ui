package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Manualtransactions;

@Service
public class ManualtransactionsRestApiDao extends RestApiDao<Manualtransactions> implements ManualtransactionsDao {

	@Autowired
	private Config config;

	public ManualtransactionsRestApiDao() {
		super(Manualtransactions.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates-payment").build();
	}

	@Override
	public ResponseEntity<String> insertManualtransactions(Manualtransactions payment) {
		return post(payment);
	}

}
