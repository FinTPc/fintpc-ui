package ro.allevo.fintpui.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.BankRestApiDao;
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class BankService {

	@Autowired
	private BankRestApiDao bankDao;

	public Bank[] getAllBanks() {
		return bankDao.getAllBanks();
	}
	
	public PagedCollection<Bank> getPage() {
		return bankDao.getPage();
	}

	public Bank getBank(String bic) {
		return bankDao.getBank(bic);
	}

	public void insertBank(Bank bank) {
		bankDao.insertBank(bank);
	}

	public void updateBank(Bank bank, String bic) {
		bankDao.updateBank(bank, bic);
	}

	public void deleteBank(String bic) {
		bankDao.deleteBank(bic);
	}

	public ArrayList<String> getAllBankBics() {
		Bank[] banks = getAllBanks();
		ArrayList<String> result = new ArrayList<>();
		for (Bank bank : banks) {
			result.add(bank.getBic());
		}
		return result;
	}

	
}
