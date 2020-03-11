package ro.allevo.fintpui.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.InternalAccountRestApiDao;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class InternalAccountService {
	
	@Autowired
	private InternalAccountRestApiDao intAccountDao;
	
	public InternalAccount[] getAllInternalAccounts() {
		return intAccountDao.getAllInternalAccounts();
	}
	
	public PagedCollection<InternalAccount> getPage() {
		return intAccountDao.getPage();
	}
	
	public PagedCollection<InternalAccount> getPage(LinkedHashMap<String, List<String>> param){
		return intAccountDao.getPage(param);
	}
		
	public InternalAccount getInternalAccount(String id) {
		return intAccountDao.getInternalAccount(id);
	}

	public void insertInternalAccount(InternalAccount internalAccount) {
		intAccountDao.insertInternalAccount(internalAccount);
	}

	public void updateInternalAccount(InternalAccount internalAccount, String id) {
		intAccountDao.updateInternalAccount(internalAccount, id);
	}

	public void deleteInternalAccount(String id) {
		intAccountDao.deleteInternalAccount(id);
	}
	
	
	public Set<String> getAllInternalAccountsCurrency() {
		InternalAccount[] internalAccounts = getAllInternalAccounts();
		Set<String> result = new HashSet<String>();
 		for(InternalAccount internalAccount : internalAccounts){
			result.add(internalAccount.getCurrency());
		}
		return result;
	}
	

}
