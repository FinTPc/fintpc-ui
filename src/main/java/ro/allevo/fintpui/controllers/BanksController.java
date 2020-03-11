package ro.allevo.fintpui.controllers;

import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
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
import ro.allevo.fintpui.model.Bank;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.service.CountryService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/banks")
public class BanksController {

	@Autowired
	Config config;

	@Autowired
	private BankService bankService;

	@Autowired
	private CountryService countryService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * @ResponseStatus(value = HttpStatus.CONFLICT)
	 * 
	 * @ExceptionHandler(ConflictException.class) public ResponseEntity<String>
	 * handleNotAuthorized(ConflictException exception){ HttpHeaders responseHeaders
	 * = new HttpHeaders(); responseHeaders.add("message",
	 * config.getMessage("error.conflict")); return new
	 * ResponseEntity<String>("bic", responseHeaders, HttpStatus.CONFLICT); }
	 */

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printBanks(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/banks required");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_MODIFY)));

		model.addAttribute("countries", JSONHelper.toString(countryService.getAllCountries()));
		model.addAttribute("apiUri", config.getAPIUrl());
		return "banks";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getBanksJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/banks required");
		PagedCollection<Bank> banks = bankService.getPage();

		DataTables dt = new DataTables();
		if (null != banks) {
			dt.setData(banks.getItems());
			dt.setRecordsFiltered(banks.getTotal());
			dt.setRecordsTotal(banks.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addBank(ModelMap model, @ModelAttribute("bank") Bank bank) {
		logger.info("/addBank required");
		model.addAttribute("bank", bank);
		model.addAttribute("formAction", "banks/insert");
		model.addAttribute("countries", countryService.getAllCountries());
		return "bank_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertBank(ModelMap model, @Valid @ModelAttribute("bank") Bank bank, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert bank requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		bankService.insertBank(bank);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{bic}/delete")
	public String deleteBank(@PathVariable String bic) {
		logger.info("delete bank required");
		bankService.deleteBank(bic);
		return "redirect:/banks";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{bic}/edit")
	public String editBank(ModelMap model, @ModelAttribute("bank") Bank bank, @PathVariable String bic) {
		logger.info("/editBank requested");
		bank = bankService.getBank(bic);
		model.addAttribute("bank", bank);
		model.addAttribute("formAction", "banks/update");
		model.addAttribute("countries", countryService.getAllCountries());
		return "bank_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateBank(ModelMap model, @ModelAttribute("bank") @Valid Bank bank, BindingResult bindingResult,
			@RequestParam("bic") String bic) throws JsonProcessingException {
		logger.info("update bank requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		bankService.updateBank(bank, bic);
		return "[]";
	}

	@GetMapping(value = "/token")
	@ResponseBody
	public String token(OAuth2Authentication auth) {
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		System.out.println("fintp-token");
		System.err.println(details.getTokenValue());
		return details.getTokenValue();
	}

}
