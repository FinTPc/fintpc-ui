package ro.allevo.fintpui.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ExternalAccountRestApiDao;
import ro.allevo.fintpui.model.ExternalAccount;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ExternalAccountService {
	
	@Autowired
	private ExternalAccountRestApiDao extAccountDao;
	
	public ExternalAccount[] getAllExternalAccounts() {
		return extAccountDao.getAllExternalAccounts();
	}
	
	public PagedCollection<ExternalAccount> getPage() {
		return extAccountDao.getPage();
	}
	
	public ExternalAccount getExternalAccount(String id) {
		return extAccountDao.getExternalAccount(id);
	}

	public void insertExternalAccount(ExternalAccount externalAccount) {
		extAccountDao.insertExternalAccount(externalAccount);
	}

	public void updateExternalAccount(ExternalAccount externalAccount, String id) {
		extAccountDao.updateExternalAccount(externalAccount, id);
	}

	public void deleteExternalAccount(String id) {
		extAccountDao.deleteExternalAccount(id);
	}

}
