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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import ro.allevo.fintpui.model.TimeLimit;
import ro.allevo.fintpui.service.ServiceMapService;
import ro.allevo.fintpui.service.TimeLimitsService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/time-limits")
public class TimeLimitsController {

	@Autowired
	Config config;

	@Autowired
	private TimeLimitsService timeLimitsService;

	@SuppressWarnings("unused")
	@Autowired
	private ServiceMapService serviceMapService;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printTimelimits(OAuth2Authentication auth, ModelMap model) {
		logger.info("/timelimits requested");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROUTINGRULES_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ROUTINGRULES_MODIFY)));

		model.addAttribute("apiUri", config.getAPIUrl());
		return "timelimits";
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getTimeLimitsJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/time limits required");
		PagedCollection<TimeLimit> timeLimits = timeLimitsService.getPage();

		DataTables dt = new DataTables();
		if (null != timeLimits) {
			dt.setData(timeLimits.getItems());
			dt.setRecordsFiltered(timeLimits.getTotal());
			dt.setRecordsTotal(timeLimits.getTotal());
		}
		dt.setDraw(draw);

		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String printRules(ModelMap model, @ModelAttribute("timelimit") TimeLimit timelimit) {
		logger.info("/addTimelimits requested");
		model.addAttribute("timelimit", timelimit);
		model.addAttribute("formAction", "time-limits/insert");
		return "timelimit_add";
	}

	@PostMapping(value = "/insert")
	@ResponseBody
	public String insertRule(ModelMap model, @ModelAttribute("timelimit") @Valid TimeLimit timelimit,
			BindingResult bindingResult) throws JsonProcessingException {
		logger.info("insert time limit requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		Date currentDate = new Date(); 
		String date = dateFormat.format(currentDate) + "T" + timelimit.getCutoff();
		
		timelimit.setCutoff(date);
		timeLimitsService.insertTimeLimit(timelimit);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{limitid}/delete")
	public String deleteTimelimit(@PathVariable int limitid) {
		logger.info("/delete time limit requested");
		timeLimitsService.deleteTimeLimit(limitid);
		return "redirect:/time-limits";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{limitid}/edit")
	public String editTimelimit(ModelMap model, @ModelAttribute("timelimit") TimeLimit timelimit,
			@PathVariable int limitid) {
		logger.info("/editTimelimit requested");
		timelimit = timeLimitsService.getTimeLimit(limitid);
		model.addAttribute("timelimit", timelimit);
		model.addAttribute("formAction", "time-limits/update");
		System.out.println(timelimit.getCutoff());
		return "timelimit_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateTimelimit(ModelMap model, @ModelAttribute("timelimit") @Valid TimeLimit timelimit,
			BindingResult bindingResult, @RequestParam("id") int id) throws JsonProcessingException {
		logger.info("/update time limit requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		Date currentDate = new Date(); 
		String date = dateFormat.format(currentDate) + " " + timelimit.getCutoff();
		timelimit.setCutoff(date);
		timeLimitsService.updateTimeLimit(id, timelimit);
		return "[]";
	}
}
