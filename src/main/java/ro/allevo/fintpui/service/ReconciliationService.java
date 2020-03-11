package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ReconciliationRestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.model.Reconciliation;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ReconciliationService {
	
	@Autowired
	private ReconciliationRestApiDao reconciliationDao;

	public Reconciliation[] getAllReconciliation() {
		return reconciliationDao.getAllReconciliation();
	}
	
	public PagedCollection<Reconciliation> getPage() {
		return reconciliationDao.getPage();
	}
	
	
	public PagedCollection<Reconciliation> getPage(LinkedHashMap<String, List<String>> params) {
		return reconciliationDao.getPage(params);
	}


	public Reconciliation getReconciliation(String id) {
		return reconciliationDao.getReconciliation(id);
	}
	
	
	public Set<String> getAllReconciliationStmtNumber() {
		Reconciliation[] reconciliations = getAllReconciliation();
		Set<String> result = new HashSet<String>();
 		for(Reconciliation reconciliation : reconciliations){
			result.add(reconciliation.getStmtstatementNumber());
		}
		return result;
	}
	


}
