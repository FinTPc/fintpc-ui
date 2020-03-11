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
import ro.allevo.fintpui.model.ServiceMap;
import ro.allevo.fintpui.service.QueueService;
import ro.allevo.fintpui.service.ServiceMapService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/services")
public class ServiceMapsController {

	@Autowired
	Config config;

	@Autowired
	private ServiceMapService serviceMapService;

	@Autowired
	private QueueService queueService;

	private static Logger logger = LogManager.getLogger(ServiceMapsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printTimelimits(OAuth2Authentication auth, ModelMap model) throws JsonProcessingException {
		logger.info("/connectors requested");
		model.addAttribute("connectors", JSONHelper.toString(serviceMapService.getServiceMapNamesList()));
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.QUEUES_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.QUEUES_MODIFY)));

		model.addAttribute("apiUri", config.getAPIUrl());
		return "servicemaps";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getServicesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/services required");
		PagedCollection<ServiceMap> services = serviceMapService.getPage();

		DataTables dt = new DataTables();
		if (null != services) {
			dt.setData(services.getItems());
			dt.setRecordsFiltered(services.getTotal());
			dt.setRecordsTotal(services.getTotal());
		}
		dt.setDraw(draw);

		return JSONHelper.toString(dt);
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editService(ModelMap model, @ModelAttribute("servicemap") ServiceMap serviceMap,
			@PathVariable int id) {
		logger.info("/editTimelimit requested");
		serviceMap = serviceMapService.getServiceMap(id);
		model.addAttribute("servicemap", serviceMap);
		model.addAttribute("formAction", "services/update");
		model.addAttribute("queues", queueService.getQueuesNames());
		model.addAttribute("apiUri", config.getAPIUrl());
		return "servicemap_edit";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateTimelimit(ModelMap model, @ModelAttribute("servicemap") @Valid ServiceMap serviceMap,
			BindingResult bindingResult, @RequestParam("id") int id) throws JsonProcessingException {
		logger.info("/update service requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}

		serviceMapService.updateServiceMap(id, serviceMap);
		return "[]";
	}
	/*
	 * //nu se modifica din interfata?
	 * 
	 * @RequestMapping(value = "/servicemaps/update", method = RequestMethod.POST)
	 * public String updateConnectors(@ModelAttribute("servicemap") ServiceMap
	 * serviceMap, @RequestParam("friendlyname") String friendlyName){
	 * logger.info("/update conectors requested");
	 * serviceMapService.updateServiceMap(friendlyName, serviceMap); return
	 * "redirect:/connectors.htm"; }
	 */
}
