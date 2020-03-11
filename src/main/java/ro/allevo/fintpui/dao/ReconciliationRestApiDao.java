package ro.allevo.fintpui.dao;


import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Reconciliation;

@Service
public class ReconciliationRestApiDao extends RestApiDao<Reconciliation> implements ReconciliationDao{
	
	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("reconciliation").build();
	}
	
	public ReconciliationRestApiDao() {
		super(Reconciliation.class);
	}
	
	@Override
	public Reconciliation[] getAllReconciliation() {
		return getAll();
	}

	@Override
	public Reconciliation getReconciliation(String id) {
		return get(id);
	}

	
}
