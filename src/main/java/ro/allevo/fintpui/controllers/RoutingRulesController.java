
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

import java.util.Optional;
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
import ro.allevo.fintpui.model.RoutingRule;
import ro.allevo.fintpui.service.QueueService;
import ro.allevo.fintpui.service.RoutingRulesService;
import ro.allevo.fintpui.service.RoutingSchemaService;
import ro.allevo.fintpui.utils.Invariants;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "routing-rules")
public class RoutingRulesController {

	@Autowired
	Config config;

	@Autowired
	private RoutingRulesService routingRulesService;

	@Autowired
	private RoutingSchemaService routingSchemaService;

	@Autowired
	private QueueService queueService;

	private static Logger logger = LogManager.getLogger(RoutingRulesController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping(value = { "", "/{schemaId}" })
	public String printRules(OAuth2Authentication auth, ModelMap model, @PathVariable Optional<Integer> schemaId) {
		logger.info("/routingrules requested with arguments [schema:" + schemaId + "]");

		if (schemaId.isPresent())
			model.addAttribute("schema", routingSchemaService.getRoutingSchema(schemaId.get()));
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROUTINGRULES_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROUTINGRULES_MODIFY)));

		return "routingrules";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getRoutingRulesJson(@RequestParam int draw,
			@RequestParam(name = "id", required = false) Integer schemaId) throws JsonProcessingException {
		logger.info("/routingRules required");

		PagedCollection<RoutingRule> rules = routingRulesService.getPage(schemaId);

		DataTables dt = new DataTables();
		if (null != rules) {
			dt.setData(rules.getItems());
			dt.setRecordsFiltered(rules.getTotal());
			dt.setRecordsTotal(rules.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */

	@GetMapping(value = "/add")
	public String printRules(ModelMap model, @ModelAttribute("rule") RoutingRule rule) throws JsonProcessingException {
		logger.info("/addRoutingrules requested");
		model.addAttribute("rule", rule);
		model.addAttribute("formAction", "routing-rules/insert");
		model.addAttribute("queues", queueService.getAllQueuesNamesAndId());
		model.addAttribute("schemas", routingSchemaService.getAllRoutingSchemasNamesAndId());
		model.addAttribute("types", Invariants.RULES_TYPES);
		model.addAttribute("actions", routingRulesService.getActions());
		model.addAttribute("actionsJSON", JSONHelper.toString(routingRulesService.getActions()));
		return "routingrules_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertRule(ModelMap model, @Valid @ModelAttribute("rule") RoutingRule routingRule,
			BindingResult bindingResult) throws JsonProcessingException {
		logger.info("insert routing rule requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		routingRulesService.insertRoutingRule(routingRule);
		return "[]";
	}

	/*
	 * EDIT
	 */

	@GetMapping(value = "/{ruleId}/edit")
	public String editRule(ModelMap model, @ModelAttribute("rule") RoutingRule rule, @PathVariable int ruleId)
			throws JsonProcessingException {
		logger.info("/editRule requested");
		rule = routingRulesService.getRoutingRule(ruleId);
		model.addAttribute("rule", rule);
		model.addAttribute("formAction", "routing-rules/update");
		model.addAttribute("queues", queueService.getAllQueuesNamesAndId());
		model.addAttribute("schemas", routingSchemaService.getAllRoutingSchemasNamesAndId());
		model.addAttribute("types", Invariants.RULES_TYPES);
		model.addAttribute("actions", routingRulesService.getActions());
		model.addAttribute("actionsJSON", JSONHelper.toString(routingRulesService.getActions()));
		if (rule.getAction().contains("(")) {
			int start = rule.getAction().indexOf("(");
			model.addAttribute("action", rule.getAction().substring(0, start));
			model.addAttribute("actionValue", rule.getAction().substring(start + 1, rule.getAction().length() - 1));
		} else
			model.addAttribute("action", rule.getAction());

		return "routingrules_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateRule(ModelMap model, @ModelAttribute("rule") @Valid RoutingRule rule,
			BindingResult bindingResult, @RequestParam("id") int id) throws JsonProcessingException {
		logger.info("update routing rule  requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		routingRulesService.updateRoutingRule(id, rule);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{ruleId}/delete")
	public String deleteRule(@PathVariable int ruleId) {
		logger.info("/delete rule requested");
		routingRulesService.deleteRoutingRule(ruleId);
		return "redirect:/routing-rules";
	}
}
