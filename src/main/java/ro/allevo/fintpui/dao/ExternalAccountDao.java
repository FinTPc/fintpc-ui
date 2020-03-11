package ro.allevo.fintpui.dao;



import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.ExternalAccount;

@Service
public interface ExternalAccountDao {

	public ExternalAccount[] getAllExternalAccounts();
	public ExternalAccount getExternalAccount(String id);
	public void insertExternalAccount(ExternalAccount externalAccount);
	public void updateExternalAccount(ExternalAccount externalAccount, String id);
	public void deleteExternalAccount(String id);

}
