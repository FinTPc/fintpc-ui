package ro.allevo.connect.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.connect.model.Account;
import ro.allevo.connect.service.SynchronizeService;
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;

@Controller
@RequestMapping("synchronized")
public class SynchronizeController {

	@Autowired
	BankService bankService;

	@Autowired
	InternalAccountService internalAccountService;

	@Autowired
	SynchronizeService synchronizeService;

	@GetMapping(value = "synchronize")
	public String getSynchronize(ModelMap model) {
		model.addAttribute("banks", bankService.getAllBanks());
		model.addAttribute("title", "Synchronize banks accounts");
		return "connect/synchronize";
	}

	private List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}

	@GetMapping(value = "page/{bic}")
	@ResponseBody
	public String getInternalEntity(ModelMap model, @PathVariable String bic) throws JsonProcessingException {

		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
		filters.put("filter_bic_exact", getFiltersParams(bic.toUpperCase()));
		
		PagedCollection<InternalAccount> internalAccount = internalAccountService.getPage(filters);
		Map<String, Map<String, String>> listInternal = formatInternalAccounts(internalAccount);
		
		Account[] externalAccount = synchronizeService.getAllAccountByAccounts(bic);
		Map<String, Map<String, String>> listExtern = formatExternalAccounts(externalAccount, listInternal);
		
		model.addAttribute("internal", listInternal);
		model.addAttribute("external", listExtern);
		return JSONHelper.toString(model);
	}
	
	private Map<String, Map<String, String>> formatInternalAccounts(PagedCollection<InternalAccount> pageInternal) {
		Map<String, Map<String, String>> listInternal = new HashMap<String, Map<String, String>>();
		for (InternalAccount account : pageInternal.getItems()) {
			Map<String, String> internal = new HashMap<String, String>();
			internal.put("iban", account.getAccountNumber());
			internal.put("resource", account.getResourceId());
			internal.put("checked", account.getResourceId() == null ? "false" : "true");
			listInternal.put(account.getAccountNumber(), internal);
		}
		return listInternal;
	}
	
	private Map<String, Map<String, String>> formatExternalAccounts(Account[] pageExternal, Map<String, Map<String, String>> listInternal){
		Map<String, Map<String, String>> listExtern = new HashMap<String, Map<String, String>>();
		for (Account account : pageExternal) {
			Map<String, String> external = new HashMap<String, String>();
			external.put("iban", account.getIban());
			external.put("resource", account.getResourceId());
			if (listInternal.containsKey(account.getIban())) {
				if (account.getResourceId().equals(listInternal.get(account.getIban()).get("resource"))) {
					external.put("checked", listInternal.get(account.getIban()).get("checked"));
				} else {
					external.put("checked", "false");
					listInternal.get(account.getIban()).put("checked", "false");
					listInternal.get(account.getIban()).put("resource", account.getResourceId());
				}
			}
			listExtern.put(account.getIban(), external);
		}
		return listExtern;
	}

	@GetMapping(value = "sync")
	@ResponseBody
	public String saveSyncAccounts(ModelMap model, @QueryParam(value = "bic") String bic, @QueryParam(value = "intern") String[] intern, @QueryParam(value = "resource") String[] resource) throws JsonProcessingException {

		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
		filters.put("filter_bic_exact", getFiltersParams(bic.toUpperCase()));

		PagedCollection<InternalAccount> internalAccount = internalAccountService.getPage(filters);
		List<InternalAccount> listInternalAccount = Arrays.asList(internalAccount.getItems());
		next:
		for(InternalAccount acount : listInternalAccount) {
			for(int i = 0; i < intern.length; i++) {
				if (acount.getAccountNumber().equals(intern[i])) {
					if (acount.getResourceId() != resource[i]) {
						acount.setResourceId(resource[i]);
						internalAccountService.updateInternalAccount(acount, String.valueOf(acount.getId()));
						continue next;
					}else {
						continue next;
					}
				}
			}
			acount.setResourceId(null);
			internalAccountService.updateInternalAccount(acount, String.valueOf(acount.getId()));
		}
		
		Map<String, Map<String, String>> listInternal = formatInternalAccounts(internalAccount);
		
		Account[] externalAccount = synchronizeService.getAllAccountByAccounts(bic);
		Map<String, Map<String, String>> listExtern = formatExternalAccounts(externalAccount, listInternal);
		
		model.addAttribute("internal", listInternal);
		model.addAttribute("external", listExtern);
		return JSONHelper.toString(model);
	}
}
