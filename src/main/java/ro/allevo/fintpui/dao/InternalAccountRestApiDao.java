package ro.allevo.fintpui.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.InternalAccount;

@Service
public class InternalAccountRestApiDao extends RestApiDao<InternalAccount> implements InternalAccountDao {

	@Autowired
	Config config;

	public InternalAccountRestApiDao() {
		super(InternalAccount.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("internal-accounts").build();
	}

	@Override
	public InternalAccount[] getAllInternalAccounts() {
		return getAll();

	}

	@Override
	public InternalAccount getInternalAccount(String id) {
		return get(id);
	}

	@Override
	public void insertInternalAccount(InternalAccount internalAccount) {
		post(internalAccount);
	}

	@Override
	public void updateInternalAccount(InternalAccount internalAccount, String id) {
		put(id, internalAccount);
	}

	@Override
	public void deleteInternalAccount(String id) {
		delete(id);
	}

}
