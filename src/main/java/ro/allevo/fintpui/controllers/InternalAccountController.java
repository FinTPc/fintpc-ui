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
import ro.allevo.fintpui.model.InternalAccount;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.InternalAccountService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/internal-accounts")
public class InternalAccountController {
	@Autowired
	Config config;

	@Autowired
	private InternalAccountService intAccountService;

	@Autowired
	private BankService banksService;

	@Autowired
	private InternalEntitiesService internalEntitiesService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printInternalAccounts(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/internalAccounts required");
		// model.addAttribute("apiUri", config.getAPIUrl());
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("bics", JSONHelper.toString(banksService.getAllBankBics()));
		model.addAttribute("entityNames", JSONHelper.toString(internalEntitiesService.getAllInternalEntityNames()));
		System.out.println("internal accounts" + auth.getAuthorities());

		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_MODIFY)));

		return "internalAccounts";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getInternalAccountsJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/internalAccounts required");
		PagedCollection<InternalAccount> internalAccounts = intAccountService.getPage();

		DataTables dt = new DataTables();
		if (null != internalAccounts) {
			dt.setData(internalAccounts.getItems());
			dt.setRecordsFiltered(internalAccounts.getTotal());
			dt.setRecordsTotal(internalAccounts.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addinternalAccount(ModelMap model, @ModelAttribute("internalAccount") InternalAccount internalAccount)
			throws JsonProcessingException {
		logger.info("/addinternalAccount required");
		model.addAttribute("internalAccount", internalAccount);
		model.addAttribute("bics", banksService.getAllBanks());
		model.addAttribute("entityNames", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("formAction", "internal-accounts/insert");
		return "internalAccount_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertInternalAccount(ModelMap model,
			@ModelAttribute("internalAccount") @Valid InternalAccount internalAccount, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert internalAccount requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		} else {
			intAccountService.insertInternalAccount(internalAccount);
			return "[]";
		}
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editInternalAccount(ModelMap model,
			@ModelAttribute("internalAccount") InternalAccount internalAccount, @PathVariable String id)
			throws JsonProcessingException {
		logger.info("/editInternalAccount requested");
		internalAccount = intAccountService.getInternalAccount(id);
		model.addAttribute("internalAccount", internalAccount);
		model.addAttribute("bics", banksService.getAllBanks());
		model.addAttribute("entityNames", internalEntitiesService.getAllInternalEntityNames());
		model.addAttribute("init_id", id);
		model.addAttribute("formAction", "internal-accounts/update");
		return "internalAccount_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateInternalAccount(ModelMap model,
			@ModelAttribute("internalAccount") @Valid InternalAccount internalAccount, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update internalAccount requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}

		intAccountService.updateInternalAccount(internalAccount, id);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteInternalAccountk(@PathVariable String id) {
		logger.info("delete internalAccounts required");
		intAccountService.deleteInternalAccount(id);
		return "redirect:/internal-accounts";
	}

}
