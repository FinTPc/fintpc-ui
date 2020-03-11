package ro.allevo.fintpui.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.ExternalAccount;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.ExternalAccountService;
import ro.allevo.fintpui.service.ExternalEntitiesService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/external-accounts")
public class ExternalAccountController {

	@Autowired
	Config config;

	@Autowired
	private ExternalAccountService externalAccountService;

	@Autowired
	private BankService banksService;

	@Autowired
	private ExternalEntitiesService externalEntitiesService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printExternalAccounts(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/externalAccounts required");
		model.addAttribute("apiUri", config.getAPIUrl());

		model.addAttribute("bics", JSONHelper.toString(banksService.getAllBankBics()));
		model.addAttribute("entityNames", JSONHelper.toString(externalEntitiesService.getAllExternalEntityNames()));

		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.EXTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();

		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.EXTERNAL_ENTITIES_LIST_MODIFY)));

		return "externalAccounts";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getExternalAccountsJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/externalAccounts required");
		PagedCollection<ExternalAccount> externalAccounts = externalAccountService.getPage();
		DataTables dt = new DataTables();
		if (null != externalAccounts) {
			dt.setData(externalAccounts.getItems());
			dt.setRecordsFiltered(externalAccounts.getTotal());
			dt.setRecordsTotal(externalAccounts.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addexternalAccount(ModelMap model, @ModelAttribute("externalAccount") ExternalAccount externalAccount)
			throws JsonProcessingException {
		logger.info("/addexternalAccount required");
		model.addAttribute("externalAccount", externalAccount);
		model.addAttribute("bics", banksService.getAllBanks());
		model.addAttribute("entityNames", externalEntitiesService.getAllExternalEntityNames());
		model.addAttribute("formAction", "external-accounts/insert");
		return "externalAccount_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertExternalAccount(ModelMap model,
			@ModelAttribute("externalAccount") @Valid ExternalAccount externalAccount, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert externalAccount requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		} else {
			externalAccountService.insertExternalAccount(externalAccount);
			return "[]";
		}
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteExternalAccountk(@PathVariable String id) {
		logger.info("delete externalAccounts required");
		externalAccountService.deleteExternalAccount(id);
		return "redirect:/external-accounts";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editExternalAccount(ModelMap model,
			@ModelAttribute("externalAccount") ExternalAccount externalAccount, @PathVariable String id)
			throws JsonProcessingException {
		logger.info("/editExternalAccount requested");
		externalAccount = externalAccountService.getExternalAccount(id);
		model.addAttribute("externalAccount", externalAccount);
		model.addAttribute("bics", banksService.getAllBanks());
		model.addAttribute("entityNames", externalEntitiesService.getAllExternalEntityNames());
		model.addAttribute("init_id", id);
		model.addAttribute("formAction", "external-accounts/update");
		return "externalAccount_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateExternalAccount(ModelMap model,
			@ModelAttribute("externalAccount") @Valid ExternalAccount externalAccount, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update externalAccount requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		externalAccountService.updateExternalAccount(externalAccount, id);
		return "[]";
	}

}
