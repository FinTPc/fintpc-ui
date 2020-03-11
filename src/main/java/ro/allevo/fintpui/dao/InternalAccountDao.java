package ro.allevo.fintpui.dao;



import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.InternalAccount;

@Service
public interface InternalAccountDao {

	public InternalAccount[] getAllInternalAccounts();
	public InternalAccount getInternalAccount(String id);
	public void insertInternalAccount(InternalAccount internalAccount);
	public void updateInternalAccount(InternalAccount internalAccount, String id);
	public void deleteInternalAccount(String id);

}
