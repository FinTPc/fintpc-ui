package ro.allevo.connect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.SynchronizeRestApiDao;
import ro.allevo.connect.model.Account;

@Service
public class SynchronizeService {
	
	@Autowired
	private SynchronizeRestApiDao syncDao;
	
	public Account[] getAllAccountByAccounts(String bic) {
		return syncDao.getAccountsByBic(bic);
	}

	public void updateAccountByIban(String... ibans) {
		syncDao.updateAccountByIban(ibans);
	}

}
