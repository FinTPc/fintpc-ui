package ro.allevo.connect.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.connect.model.Job;
import ro.allevo.connect.model.Scheduler;
import ro.allevo.connect.model.Trigger;
import ro.allevo.connect.service.JobService;
import ro.allevo.connect.service.SchedulerService;
import ro.allevo.connect.service.TriggerService;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.controllers.TimeLimitsController;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "connects/monitoring")
public class SchedulerController {

	@Autowired
	Config config;
	
	@Autowired
	TriggerService triggerService;
	
	@Autowired
	JobService jobService;
	
	@Autowired
	SchedulerService schedulerService;
	
	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());
	private static final String START = "start";
	private static final String STOP = "stop";
	private static final String HOME_PAGE_REDIRECT = "redirect:/connects/monitoring";
	
	/*
	 * DISPLAY
	 */
	@GetMapping
	public String print(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		
//		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.TRIGGERS_LIST_VIEW)))
//			throw new NotAuthorizedException();
		
//		model.addAttribute("hasModifyRole",
//				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.TRIGGERS_LIST_MODIFY)));
		model.addAttribute("hasModifyRole",true);
		model.addAttribute("apiUri", config.getConnectUrl());
		model.addAttribute("isToggled", schedulerService.getSchedulerState(""));
		
		return "connect/monitoring";
	}
	
	@GetMapping(value = "page-triggers")
	public @ResponseBody String getTriggersJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/triggers required");
		
		PagedCollection<Trigger> triggers = triggerService.getPage();
	
		DataTables dt = new DataTables();
		if (null != triggers) {
			dt.setData(triggers.getItems());
			dt.setRecordsFiltered(triggers.getTotal());
			dt.setRecordsTotal(triggers.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value = "page-jobs")
	public @ResponseBody String getJobsJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/jobs required");
		
		PagedCollection<Job> jobs = jobService.getPage();

		DataTables dt = new DataTables();
		if (null != jobs) {
			dt.setData(jobs.getItems());
			dt.setRecordsFiltered(jobs.getTotal());
			dt.setRecordsTotal(jobs.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value = "{group}/{rowid}/start-trigger")
	public String startTrigger(@PathVariable String rowid, @PathVariable String group) throws JsonProcessingException {
		
		updateTriggerState(group + '/' + rowid, START);

		return HOME_PAGE_REDIRECT;
	}
	
	@GetMapping(value = "{group}/{rowid}/stop-trigger")
	public String stopTrigger(@PathVariable String rowid, @PathVariable String group) throws JsonProcessingException {
		
		updateTriggerState(group + '/' + rowid, STOP);

		return HOME_PAGE_REDIRECT;
	}
	
	private void updateTriggerState(String path, String action) {
		Trigger trigger = triggerService.getTrigger(path);
		trigger.setAction(action);
		triggerService.updateTrigger(trigger, path);
	}
	
	@GetMapping(value = "{group}/{rowid}/start-job")
	public String startJobTriggers(@PathVariable String rowid, @PathVariable String group) throws JsonProcessingException {
		
		updateJobTriggersState(group + '/' + rowid, START);

		return HOME_PAGE_REDIRECT;
	}
	
	@GetMapping(value = "{group}/{rowid}/stop-job")
	public String stopJobTriggers(@PathVariable String rowid, @PathVariable String group) throws JsonProcessingException {
		
		updateJobTriggersState(group + '/' + rowid, STOP);

		return HOME_PAGE_REDIRECT;
	}
	
	private void updateJobTriggersState(String path, String action) {
		Job job = jobService.getJob(path);
		job.setAction(action);
		jobService.updateJob(job, path);
	}
	
	@PostMapping(value = "toggle")
	public String toggleScheduler(boolean getToggleCheck) {

		// cand e afisat STOP pe btn de toggle (adica sched e pornit) - dau click - intoarce true
		// in functie de toggle - dau start sau pause scheduler
		Scheduler scheduler = schedulerService.getScheduler("");
		
		if(getToggleCheck) {
			scheduler.setInStandbyMode(true);
		}else {
			scheduler.setInStandbyMode(false);
		}
		
		schedulerService.updateScheduler("", scheduler);
		return HOME_PAGE_REDIRECT;
	}
}
