package ro.allevo.connect.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import ro.allevo.connect.model.Connect;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class ConnectRestApiDao extends RestApiDao<Connect> implements ConnectDao {

	@Autowired
	Config config;

	@Autowired
	private RestOperations client;
	
	public ConnectRestApiDao() {
		super(Connect.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).path("connects").build();
	}

	@Override
	public Connect[] getAllConnects() {
		return getAll();
	}

	@Override
	public Connect getConnect(Long id) {
		return get(String.valueOf(id));
	}

	@Override
	public void insertConnect(Connect connect) {
		post(connect);
	}

	@Override
	public ResponseEntity<String> updateConnect(Connect connect) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(String.valueOf(connect.getId())).build();
		return put(uri, connect);
	}

	public String newTokenConnect(Long id, String code, String sha) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(String.valueOf(id)).path("token").queryParam("code", code)
				.queryParam("sha", sha).build();
		return client.getForObject(uri, String.class);
		// return getAll();
	}

	@Override
	public void deleteConnect(Long id) {
		delete(String.valueOf(id));
	}
}
