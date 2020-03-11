package ro.allevo.connect.controller;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import ro.allevo.connect.model.Connect;
import ro.allevo.connect.model.Consent;

import ro.allevo.connect.model.Job;
import ro.allevo.connect.model.Trigger;
import ro.allevo.connect.service.ConnectService;
import ro.allevo.connect.service.ConsentService;
import ro.allevo.connect.service.JobService;
import ro.allevo.connect.service.TriggerService;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.controllers.TimeLimitsController;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.service.BankService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping("connects")
public class ConnectContorller {

	@Autowired
	Config config;

	@Autowired
	ConnectService connectService;
	
	@Autowired
	ConsentService consentService;
	
	@Autowired
	BankService bankService;
	
	@Autowired
	JobService jobService;
	
	@Autowired
	TriggerService triggerService;
	
	@Autowired
	private HttpServletRequest request;

	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());
	
	private static String jobGroup = "PSD2";

	@GetMapping(value = "connect")
	public String printBanks(OAuth2Authentication auth, ModelMap model) {
		logger.info("/connection required");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_MODIFY)));

		model.addAttribute("apiUri", config.getConnectUrl());
		return "connect/connect";
	}

	@GetMapping(value = "page")
	public @ResponseBody String getBanksJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/connection required");
		PagedCollection<Connect> connects = connectService.getPage();

		DataTables dt = new DataTables();
		if (null != connects) {
			dt.setData(connects.getItems());
			dt.setRecordsFiltered(connects.getTotal());
			dt.setRecordsTotal(connects.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "add")
	public String addConnect(ModelMap model, @ModelAttribute("connect") Connect connect, @ModelAttribute("consent") Consent consent) 
			throws JsonProcessingException {
		logger.info("/addConnection required");
		connect.setConsentEntity(consent);
		model.addAttribute("connect", connect);
//		model.addAttribute("consent", consent);
		model.addAttribute("formAction", "insert");
		model.addAttribute("id", 0);
		model.addAttribute("banks",bankService.getAllBanks());
		return "connect/connect_add";
	}

	@PostMapping(value = "insert")
	@ResponseBody
	public String insertConnect(ModelMap model, @Valid @ModelAttribute("connect") Connect connect,
			BindingResult bindingResult) throws JsonProcessingException {
		logger.info("insert connection requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
//		Consent consent = new Consent();
//		consent.setBic(connect.getBic());
		//connect.getConsentEntity().setBic(connect.getBic());
		connectService.insertConnect(connect);
		//Consent consent = connect.getConsent();
//		consent.setBic(connect.getBic());
//		consentService.insertConsent(consent);
		return "[]";
	}

	/*
	 * DELETE
	 */
	@GetMapping(value = "/{id}/delete")
	public String deleteConnect(@PathVariable Long id) {
		logger.info("delete connection required");
		connectService.deleteConnect(id);
		return "redirect:/connects/connect";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editConnect(ModelMap model, @ModelAttribute("connect") Connect connect, @PathVariable Long id) {
		logger.info("/editConnection requested");
		connect = connectService.getConnect(id);
		model.addAttribute("connect", connect);
		model.addAttribute("formAction", "update");
		model.addAttribute("banks",bankService.getAllBanks());
		return "connect/connect_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateConnect(ModelMap model, @ModelAttribute("connect") @Valid Connect connect,
			BindingResult bindingResult, @RequestParam("id") Long id) throws JsonProcessingException {
		logger.info("update connection requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		connectService.updateConnect(connect);
		return "[]";
	}

	/*
	 * @GetMapping(value = "/token/{id}/update") public String newToken(ModelMap
	 * model, @PathVariable long id) throws Exception {
	 */
		/*
		 * Connect connect = connectService.getConnect(id);
		 * ResourceOwnerPasswordResourceDetails resource = new
		 * ResourceOwnerPasswordResourceDetails();
		 * resource.setAccessTokenUri(connect.getAccessTokenUri());
		 * resource.setClientId(connect.getClientId());
		 * resource.setClientSecret(connect.getClientSecret());
		 * //resource.setScope(Arrays.asList("foo read write"));
		 * resource.setScope(Arrays.asList("trust"));
		 * resource.setUsername(connect.getUserId());
		 * resource.setPassword(connect.getUserSecret());
		 * resource.setId(String.valueOf(connect.getId()));
		 * 
		 * OAuth2RestTemplate template = new OAuth2RestTemplate(resource);
		 * OAuth2AccessToken existingToken = template.getAccessToken();
		 * //((DefaultOAuth2AccessToken) existingToken).setExpiration(new Date(0L));
		 * 
		 * 
		 * connectService.getToken(connect, id); return connect.getToken();
		 */
		
		/*
		 * ResponseEntity<String> connects = connectService.newToken(id);
		 * 
		 * return JSONHelper.toString(connects);
		 */
	/* } */
	
	@GetMapping(value = "/token/{id}/delete")
	@ResponseBody
	public ResponseEntity<Integer> deleteToken(ModelMap model, @PathVariable long id) throws Exception {
		Connect connect = connectService.getConnect(id);
		connect.setToken("");
		connect.setExpirationDate(null);
		ResponseEntity<String> resp = connectService.updateConnect(connect);
		model.addAttribute("code", resp.getStatusCodeValue());
		
		return ResponseEntity.ok(resp.getStatusCodeValue());//JSONHelper.toString();
	}
	
	@GetMapping(value = "/consent/{id}/update")
	@ResponseBody
	public String newConsent(ModelMap model, @PathVariable long id) throws Exception {
		Connect connect = connectService.getConnect(id);
		Consent consent = connect.getConsentEntity();
		consentService.insertConsent(consent);
		connect = connectService.getConnect(id);
		return JSONHelper.toString(connect.getConsentEntity());
	}
	
	@PostMapping(value = "/consent/{id}/delete")
	@ResponseBody
	public ResponseEntity<Integer> deleteConsent(ModelMap model, @PathVariable long id) throws Exception {
		Connect connect = connectService.getConnect(id);
		Consent consent = connect.getConsentEntity();
		ResponseEntity<String> resp = consentService.deleteConsent(consent.getBic(), consent.getBic());//(consent, consent.getBic());
		
		
		return  ResponseEntity.ok(resp.getStatusCodeValue());
	}

	@GetMapping(value = "/{id}/ViewToken")
	public String getViewToken(ModelMap model, @PathVariable long id) throws NoSuchAlgorithmException {
		Connect connect = connectService.getConnect(id);
		
		model.addAttribute("connect", connect);
		model.addAttribute("title", "Edit consent");

		return "connect/connect_upd";
	}
	
	@GetMapping(value = "/get_redirect")
	@ResponseBody
	public String getRedirectUrl(@QueryParam(value="{id}") Long id) {
		String code = "U1C0I6sHBtIHHFzZJmoHpeOfaiNoA5sIURzrnNspkfIhvmRKHfqXZiJcoxHcQvBturMupLBhu6Ym5x52EAFWbUsDg0a6hvhvUeNbf5KbhlJ4Wodm7iqVs6HMybYLapCm"/*UUID.randomUUID().toString()*/;

		String shaCode = "cKrQnB7uO_7axnHLZr84--CrfCRDEnK5naYaeGKONqw";// DigestUtils.sha256Hex(code);
		request.getSession().setAttribute("code_token", code);

		String redirect = "response_type=code&client_id=nTwmgAd5mgkiUorEziH5&";
		redirect +="code_challenge_method=S256&state=statetest&code_challenge=" + shaCode;
		redirect += "&redirect_uri=https://google.com";

		Connect connect = connectService.getConnect(id);
		String consentId = connect.getConsentEntity().getConsentId();
		String redirectUrl = connect.getUserAuthorizationUri().replace("://", "://"+connect.getUserId() + ":" +connect.getUserSecret() + "@");

		return redirectUrl +"?" + "scope=AIS:"+consentId + "&" +redirect;
	}

	private List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}
	
	@GetMapping(value="newtoken")
	@ResponseBody
	public String newToken(@DefaultValue("") @QueryParam("code") String code,
			@DefaultValue("") @QueryParam("bic") String bic) {
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
		filters.put("filter_bic_exact", getFiltersParams(bic.toUpperCase()));
		
		PagedCollection<Connect> connect = connectService.getPage(filters);
		Connect conn = connect.getItems()[0];
		
		String shaCode = "U1C0I6sHBtIHHFzZJmoHpeOfaiNoA5sIURzrnNspkfIhvmRKHfqXZiJcoxHcQvBturMupLBhu6Ym5x52EAFWbUsDg0a6hvhvUeNbf5KbhlJ4Wodm7iqVs6HMybYLapCm";// DigestUtils.sha256Hex(code_token);
		String response = connectService.newToken(new Long(conn.getId()), code, shaCode);
		
		return response;
	}
	
	@GetMapping(value="tokenformtest")
	public String getTokenFormTest() {
		return "tokenFormTest";
	}
	
	@GetMapping(value = "{rowid}/add-job")
	public String addJob(ModelMap model, @PathVariable Long rowid) 
			throws JsonProcessingException {
		
		Connect connect = connectService.getConnect(rowid);
		String bic = connect.getBic();

		String time = connect.getTimeTrigger();
		boolean isJobCreated = time != null && !time.equals("");
		model.addAttribute("isJobCreated", isJobCreated);
		model.addAttribute("rowid", rowid);
		model.addAttribute("bic", bic);

		model.addAttribute("formAction", "insert-trigger");
		return "connect/job_add";
	}

	@PostMapping(value = "insert-trigger")
	public String insertTrigger(Long rowid, boolean isJobCreated, String bic, String timePicker) {  // timePicker = 08:15			
						
		if(!timePicker.equals("")) {
			Connect connect = connectService.getConnect(rowid);
			String[] timeSplit = timePicker.split(":");
			String hour = timeSplit[0];
			String minute = timeSplit[1];
			
			if(!isJobCreated) {
				insertJob(bic);
				insertTrigger(minute, hour, bic);
			}else {
				updatetrigger(minute, hour, bic);
			}
			
			connect.setTimeTrigger(timePicker);
			connectService.updateConnect(connect);
			return "connect/connect";
		}		
		return "[]";	
	}
	
	private void insertJob(String bic) {
		Job job = new Job();
		job.setName(bic);
		job.setGroup(jobGroup);		
		job.setParams(new HashMap<String, String>());
		job.setAction("start");
		jobService.insertJob(job);
	}
	
	private void insertTrigger(String minute, String hour, String bic){
		
		Trigger trigger = new Trigger();
		trigger.setName(bic + "trigger");
		trigger.setGroup(bic + "groupTrigger");
		trigger.setJobName(bic);
		trigger.setCronExpression("0 " + minute +  " " + hour + " ? * MON,TUE,WED,THU,FRI *");
		trigger.setAction("start");
		
		URI uri = UriBuilder.fromUri(config.getConnectUrl()).path("jobs/"  + jobGroup + "/" + bic + "/triggers").build();
		triggerService.insertTrigger(uri, trigger);
	}
	
	private void updatetrigger(String minute, String hour, String bic) {
		String id = bic + "groupTrigger/" + bic + "trigger";
		Trigger trigger = triggerService.getTrigger(id);
		trigger.setCronExpression("0 " + minute +  " " + hour + " ? * MON,TUE,WED,THU,FRI *");
		triggerService.updateTrigger(trigger, id);
	}
	
	@PostMapping(value = "delete-trigger")
	public String deleteTrigger(Long rowid, String bic) {				
				
		Connect connect = connectService.getConnect(rowid);
		connect.setTimeTrigger("");
		connectService.updateConnect(connect);
		jobService.deleteJob(jobGroup + "/" + bic);
		return "connect/connect";		
	}
}
