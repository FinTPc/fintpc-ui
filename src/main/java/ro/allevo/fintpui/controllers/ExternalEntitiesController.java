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
import ro.allevo.fintpui.model.ExternalEntity;
import ro.allevo.fintpui.service.CountryService;
import ro.allevo.fintpui.service.ExternalEntitiesService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/external-entities")
public class ExternalEntitiesController {

	@Autowired
	Config config;

	@Autowired
	private ExternalEntitiesService externalEntitiesService;
	@Autowired
	private CountryService countryService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printExternalEntities(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/externalEntities required");
		model.addAttribute("apiUri", config.getAPIUrl());
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.EXTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.EXTERNAL_ENTITIES_LIST_MODIFY)));

		model.addAttribute("countries", JSONHelper.toString(countryService.getAllCountries()));
		return "externalEntities";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getExternalEntitiesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/externalEntities required");
		PagedCollection<ExternalEntity> externalEntities = externalEntitiesService.getPage();

		DataTables dt = new DataTables();
		if (null != externalEntities) {
			dt.setData(externalEntities.getItems());
			dt.setRecordsFiltered(externalEntities.getTotal());
			dt.setRecordsTotal(externalEntities.getTotal());
		}
		dt.setDraw(draw);

		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addExternalEntity(ModelMap model, @ModelAttribute("externalEntity") ExternalEntity externalEntity) {
		logger.info("/addExternalEntity required");
		model.addAttribute("externalEntity", externalEntity);
		model.addAttribute("formAction", "external-entities/insert");
		model.addAttribute("countries", countryService.getAllCountries());
		return "externalEntity_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertExternalEntity(@Valid @ModelAttribute("externalEntity") ExternalEntity externalEntity,
			BindingResult bindingResult) throws JsonProcessingException {
		logger.info("insert external entity requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		externalEntitiesService.insertExternalEntity(externalEntity);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteExternalEntity(@PathVariable String id) {
		logger.info("/delete external entity required");
		externalEntitiesService.deleteExternalEntity(id);
		return "redirect:/external-entities";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editExternalEntity(ModelMap model, @ModelAttribute("externalEntity") ExternalEntity externalEntity,
			@PathVariable String id) {
		logger.info("/editExternalEntities requested");
		externalEntity = externalEntitiesService.getExternalEntity(id);
		model.addAttribute("externalEntity", externalEntity);
		model.addAttribute("init_id", id);
		model.addAttribute("formAction", "external-entities/update");
		model.addAttribute("countries", countryService.getAllCountries());
		return "externalEntity_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateExternalEntity(@ModelAttribute("externalEntity") @Valid ExternalEntity externalEntity,
			BindingResult bindingResult, @RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update external entity requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		} else {
			externalEntitiesService.updateExternalEntity(externalEntity, id);
			return "[]";
		}
	}

}
