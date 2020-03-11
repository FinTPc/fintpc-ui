package ro.allevo.fintpui.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.ExternalAccount;
@Service
public class ExternalAccountRestApiDao extends RestApiDao<ExternalAccount> implements ExternalAccountDao{

	@Autowired
	Config config;
	
	public ExternalAccountRestApiDao() {
		super(ExternalAccount.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("external-accounts").build();
	}
	
	@Override
	public ExternalAccount[] getAllExternalAccounts() {
		return getAll();
	}

	@Override
	public ExternalAccount getExternalAccount(String id) {
		return get(id);
	}

	@Override
	public void insertExternalAccount(ExternalAccount externalAccount) {
		post(externalAccount);
	}

	@Override
	public void updateExternalAccount(ExternalAccount externalAccount, String id) {
		put(id, externalAccount);
	}

	@Override
	public void deleteExternalAccount(String id) {
		delete(id);
	}

}
