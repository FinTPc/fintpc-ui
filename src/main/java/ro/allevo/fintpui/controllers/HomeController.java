package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.Roles;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

	@Autowired
	MessageService messageService;
	
	@Autowired
	Config config;

	@GetMapping
	public String viewHome(OAuth2Authentication auth, ModelMap model) throws JsonParseException, JsonMappingException, IOException {
//		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_VIEW)))
//			throw new NotAuthorizedException();
//		model.addAttribute("hasModifyRole",
//				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_MODIFY)));

		model.addAttribute("title", "home");
		model.addAttribute("apiUri", config.getAPIUrl());
		return "home";
	}

	private List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}
	
	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public @ResponseBody String getMessagesType(ModelMap model, @RequestParam(name = "msgType") String param) throws JsonProcessingException {
		
		SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
		SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
		Calendar cal = Calendar.getInstance();
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
		filters.put("filter_insertdate_ldate", getFiltersParams(sdfStart.format(cal.getTime())));
		filters.put("filter_insertdate_udate", getFiltersParams(sdfEnd.format(cal.getTime())));
		filters.put("filter_messageType_exact", getFiltersParams(param));
		filters.put("total", getFiltersParams(""));
		
		return JSONHelper.toString(messageService.getMessagesType(filters).getTotal());
		
	}
	
	@RequestMapping(value = "/transactions/statements", method = RequestMethod.GET)
	public @ResponseBody String getMessagesStatement(ModelMap model, @RequestParam(name = "msgType") String param) throws JsonProcessingException {
		
		SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
		SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
		Calendar cal = Calendar.getInstance();
		
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>();
		filters.put("filter_insertdate_ldate", getFiltersParams(sdfStart.format(cal.getTime())));
		filters.put("filter_insertdate_udate", getFiltersParams(sdfEnd.format(cal.getTime())));
		filters.put("filter_messageType_exact", getFiltersParams(param));
		filters.put("total", getFiltersParams(""));
		return JSONHelper.toString(messageService.getMessagesType(filters).getTotal());
		
	}
}
