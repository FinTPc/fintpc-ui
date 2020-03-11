package ro.allevo.connect.dao;

import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Account;

@Service
public interface SynchronizeDao {

	public Account[] getAllAccounts();
	public Account[] getAccountsByBic(String bic);
	public void updateAccountByIban(String... ibans);
}
