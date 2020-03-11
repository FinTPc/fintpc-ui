package ro.allevo.connect.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Account;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class SynchronizeRestApiDao extends RestApiDao<Account> implements SynchronizeDao {

	@Autowired
	Config config;
	
	public SynchronizeRestApiDao() {
		super(Account.class);
	}

	@Override
	public Account[] getAllAccounts() {
		return getAll();
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getConnectUrl()).build();
	}

	@Override
	public Account[] getAccountsByBic(String bic) {
		URI uri = UriBuilder.fromUri(getBaseUrl()).path(bic).path("accounts").build();
		return getAll(uri);
	}

	public void updateAccountByIban(String... ibans) {
		
	}

}
