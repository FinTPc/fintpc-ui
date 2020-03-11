/*
* FinTP - Financial Transactions Processing Application
* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>
* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
*/

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
import ro.allevo.fintpui.model.RoutingSchema;
import ro.allevo.fintpui.service.RoutingRulesService;
import ro.allevo.fintpui.service.RoutingSchemaService;
import ro.allevo.fintpui.service.TimeLimitsService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/schemas")
public class RoutingSchemasController {

	@Autowired
	Config config;

	@Autowired
	private RoutingSchemaService routingSchemaService;

	@Autowired
	private RoutingRulesService routingRulesService;

	@Autowired
	private TimeLimitsService timeLimitsService;

	private static Logger logger = LogManager.getLogger(RoutingSchemasController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printSchemas(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/schemas requested");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROUTINGRULES_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROUTINGRULES_MODIFY)));

		model.addAttribute("limits", JSONHelper.toString(timeLimitsService.getAllTimeLimitNames()));
		return "schemas";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getRoutingSchemasJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/routing schemas required");
		PagedCollection<RoutingSchema> schemas = routingSchemaService.getPage();

		DataTables dt = new DataTables();
		if (null != schemas) {
			dt.setData(schemas.getItems());
			dt.setRecordsFiltered(schemas.getTotal());
			dt.setRecordsTotal(schemas.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addSchema(ModelMap model, @ModelAttribute("schema") RoutingSchema schema) {
		logger.info("/addSchema page requested");
		model.addAttribute("schemas", routingSchemaService.getAllRoutingSchemasNamesAndId());
		model.addAttribute("timelimits", timeLimitsService.getAllTimeLimits());
		model.addAttribute("formAction", "schemas/insert");
		return "schemas_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertRoutingSchema(ModelMap model, @Valid @ModelAttribute("schema") RoutingSchema routingSchema,
			BindingResult bindingResult) throws JsonProcessingException {
		logger.info("/insert routing schema requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		routingSchemaService.insertRoutingSchema(routingSchema);

//		if (null != routingSchema.getSchemaCopy()) {
//			routingRulesService.copyRules(routingSchema.getSchemaCopy(), routingSchema.getId());
//		}
		return "[]";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editSchema(ModelMap model, @ModelAttribute("schema") RoutingSchema schema, @PathVariable int id) {
		logger.info("/editSchema requested");
		schema = routingSchemaService.getRoutingSchema(id);
		model.addAttribute("schema", schema);
		model.addAttribute("formAction", "schemas/update");
		model.addAttribute("timelimits", timeLimitsService.getAllTimeLimits());
		model.addAttribute("init_id", id);
		return "schemas_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateSchema(ModelMap model, @ModelAttribute("schema") @Valid RoutingSchema schema,
			BindingResult bindingResult, @RequestParam("id") int id) throws JsonProcessingException {
		logger.info("/update routing schema requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		routingSchemaService.updateRoutingSchema(id, schema);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteQueue(@PathVariable int id) {
		logger.info("/delete schema requested");
		routingRulesService.deleteRulesFromSchema(id);
		routingSchemaService.deleteRoutingSchema(id);
		return "redirect:/schemas";
	}
}
