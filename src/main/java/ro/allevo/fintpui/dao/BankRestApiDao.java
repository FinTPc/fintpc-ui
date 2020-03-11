package ro.allevo.fintpui.dao;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Bank;

@Service
public class BankRestApiDao extends RestApiDao<Bank> implements BankDao{

	@Autowired
	Config config;
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("banks").build();
	}
	
	public BankRestApiDao() {
		super(Bank.class);
	}
	
	@Override
	public Bank[] getAllBanks() {
		return getAll();
	}

	@Override
	public Bank getBank(String bic) {
		return get(bic);
	}

	@Override
	public void insertBank(Bank bank) {
		post(bank);
	}

	@Override
	public void updateBank(Bank bank, String bic) {
		put(bic, bank);
	}

	@Override
	public void deleteBank(String bic) {
		delete(bic);
	}
}
