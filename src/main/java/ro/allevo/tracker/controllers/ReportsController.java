package ro.allevo.tracker.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import ro.allevo.fintpui.service.ServiceMapService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.tracker.service.ComplexityService;
import ro.allevo.tracker.service.ComponentService;
import ro.allevo.tracker.service.LiveService;
import ro.allevo.tracker.service.OverallService;

@Controller("TrackerReportsController")
@RequestMapping("tracker")
public class ReportsController {

	private static Logger logger = LogManager.getLogger(ReportsController.class.getName());
	
	@Autowired
	private LiveService liveService;
	
	@Autowired
	private ComplexityService complexityService;
	
	@Autowired
	private OverallService overallService;
	
	@Autowired
	private ComponentService componentService;
	
	@Autowired
	private ServiceMapService servicemapService;
	
	@GetMapping(value = "overall")
	public String overallReport() {
		return "tracker/overall";
	}
	
	@GetMapping(value="overall-trace")
	@ResponseBody
	public String overallTrace() throws JsonProcessingException{
		logger.info("overall trace requested");
		
		return JSONHelper.toString(overallService.trace());
	}
	
	@GetMapping(value="overall-timestamps")
	@ResponseBody
	public String getTimestamps(@RequestParam("date") Date date) throws JsonProcessingException{
		logger.info("live get intervals requested");
		
		return JSONHelper.toString(overallService.getTimestamps(date));
	}
	
	@GetMapping(value="overall-report")
	@ResponseBody
	public String getOverallReport(@RequestParam("timestamp") String timestamp) throws JsonProcessingException{
		logger.info("overall report requested");
		
		return JSONHelper.toString(overallService.getReport(timestamp));
	}
	
	@GetMapping(value = "component")
	public String componentReport(ModelMap model) {
		model.addAttribute("servicemaps", servicemapService.getServiceMapsList(new LinkedHashMap<String, List<String>>(){{
			put("filter_serviceType_exact", new ArrayList<String>() {{add("1"); add("2");}});
		}}));
		
		return "tracker/component";
	}
	
	@GetMapping(value="component-trace")
	@ResponseBody
	public String componentTrace() throws JsonProcessingException{
		logger.info("component trace requested");
		
		return JSONHelper.toString(componentService.trace());
	}
	
	@GetMapping(value="component-timestamps")
	@ResponseBody
	public String getComponentTimestamps(@RequestParam("date") Date date) throws JsonProcessingException{
		logger.info("component get timestamps requested");
		
		return JSONHelper.toString(componentService.getTimestamps(date));
	}
	
	@GetMapping(value="component-report")
	@ResponseBody
	public String getComponentReport(@RequestParam("timestamp") String timestamp, @RequestParam("name") String name, @RequestParam("thread") String thread) throws JsonProcessingException{
		logger.info("component report requested");
		
		return JSONHelper.toString(componentService.getReport(timestamp, name, thread));
	}
	
	@RequestMapping(value="live", method=RequestMethod.GET)
	public String liveReport(ModelMap model){
		logger.info("/tracker/live requested");
		
		return "tracker/live";
	}
	
	@GetMapping(value="live-trace")
	@ResponseBody
	public String liveTrace(@RequestParam("time") int time) throws JsonProcessingException{
		logger.info("live trace requested");
		
		return JSONHelper.toString(liveService.trace(time));
	}
	
	@GetMapping(value="live-intervals")
	@ResponseBody
	public String getIntervals(@RequestParam("date") Date date) throws JsonProcessingException{
		logger.info("live get intervals requested");
		
		return JSONHelper.toString(liveService.getIntervals(date));
	}
	
	@GetMapping(value="live-report")
	@ResponseBody
	public String getReport(@RequestParam("id") int id) throws JsonProcessingException{
		logger.info("live report requested");
		
		return JSONHelper.toString(liveService.getReport(id));
	}
	
	@RequestMapping(value="complexity", method=RequestMethod.GET)
	public String complexityReport(ModelMap model){
		logger.info("/tracker/complexity requested");
		
		model.addAttribute("values", complexityService.getAll());
		
		return "tracker/complexity";
	}
	
}
