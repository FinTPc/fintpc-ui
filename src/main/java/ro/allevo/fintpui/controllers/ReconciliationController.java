package ro.allevo.fintpui.controllers;


import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.Reconciliation;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.CountryService;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.ReconciliationService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;



@Controller
@RequestMapping(value = "/reconciliation")
public class ReconciliationController {
	
	@Autowired
	Config config;

	@Autowired
	private ReconciliationService reconciliationService;
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	@Autowired
	InternalAccountService internalAccountService;
	
	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printReconciliation(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/reconciliation required");
	//	if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.RECONCILIATION_VIEW)))
		//	throw new NotAuthorizedException();
		//model.addAttribute("hasModifyRole",
		//		auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.RECONCILIATION_MODIFY)));

		model.addAttribute("reconciliation", JSONHelper.toString(reconciliationService.getAllReconciliation()));
		model.addAttribute("internalEntitiesJson", JSONHelper.toString(internalEntitiesService.getAllInternalEntityNames()));
		model.addAttribute("internalAccounts", JSONHelper.toString(internalAccountService.getAllInternalAccountsCurrency()));
		model.addAttribute("stmtNumber", JSONHelper.toString(reconciliationService.getAllReconciliationStmtNumber()));
		model.addAttribute("apiUri", config.getAPIUrl());
		return "reconciliation";
	}
	

	
	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getBanksJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/reconciliation required");
		PagedCollection<Reconciliation> reconciliation = reconciliationService.getPage();

		DataTables dt = new DataTables();
		if (null != reconciliation) {
			dt.setData(reconciliation.getItems());
			dt.setRecordsFiltered(reconciliation.getTotal());
			dt.setRecordsTotal(reconciliation.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	

	
	
}
