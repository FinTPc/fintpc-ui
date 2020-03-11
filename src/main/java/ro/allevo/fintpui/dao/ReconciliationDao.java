package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.Reconciliation;

@Service
public interface ReconciliationDao {
	
	public Reconciliation[] getAllReconciliation();
	public Reconciliation getReconciliation(String id);

}
