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
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.service.CountryService;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/internal-entities")
public class InternalEntitiesController {

	@Autowired
	Config config;

	@Autowired
	private InternalEntitiesService internalEntitiesService;
	@Autowired
	private CountryService countryService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printInternalEntities(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/internalEntities required");
		model.addAttribute("apiUri", config.getAPIUrl());
		System.out.println("internal Entities" + auth.getAuthorities());
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.INTERNAL_ENTITIES_LIST_MODIFY)));

		model.addAttribute("countries", JSONHelper.toString(countryService.getAllCountries()));
		return "internalEntities";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getInternalEntitiesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/internalEntities required");
		PagedCollection<InternalEntity> internalEntities = internalEntitiesService.getPage();

		DataTables dt = new DataTables();
		if (null != internalEntities) {
			dt.setData(internalEntities.getItems());
			dt.setRecordsFiltered(internalEntities.getTotal());
			dt.setRecordsTotal(internalEntities.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addInternalEntity(ModelMap model, @ModelAttribute("internalEntity") InternalEntity internalEntity) {
		logger.info("/addInternalEntity required");
		model.addAttribute("internalEntity", internalEntity);
		model.addAttribute("formAction", "internal-entities/insert");
		model.addAttribute("countries", countryService.getAllCountries());
		return "internalEntity_add";
	}

	@PostMapping(value = "insert")
	@ResponseBody
	public String insertInternalEntity(ModelMap model,
			@Valid @ModelAttribute("internalEntity") InternalEntity internalEntity, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert internal entity requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		internalEntitiesService.insertInternalEntity(internalEntity);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteInternalEntity(@PathVariable String id) {
		logger.info("/delete internal entity required");
		System.out.println(id);
		internalEntitiesService.deleteInternalEntity(id);
		return "redirect:/internal-entities";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editInternalEntity(ModelMap model, @ModelAttribute("internalEntity") InternalEntity internalEntity,
			@PathVariable String id) {
		logger.info("/editInternalEntities requested");
		internalEntity = internalEntitiesService.getInternalEntity(id);
		model.addAttribute("internalEntity", internalEntity);
		model.addAttribute("init_id", id);
		model.addAttribute("formAction", "internal-entities/update");
		model.addAttribute("countries", countryService.getAllCountries());
		return "internalEntity_add";

	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateInternalEntity(ModelMap model,
			@ModelAttribute("internalEntity") @Valid InternalEntity internalEntity, BindingResult bindingResult,
			@RequestParam("id") String id) throws JsonProcessingException {
		logger.info("update internal entity requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		} else {
			internalEntitiesService.updateInternalEntity(internalEntity, id);
			return "[]";
		}
	}
}
