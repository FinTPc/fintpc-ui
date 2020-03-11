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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestOperations;

@Controller
public class LoginController {
	
	@Value("${fintpui.api.auth-url}")
	private String url;
	
	@Autowired
	private RestOperations restOperations;
	
	@Autowired
	private HttpServletRequest request;
		
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		return "redirect:/home?fp=Home";
	}
 
	@RequestMapping(value="/accessdenied", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "redirect:/home?fp=Home";
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(OAuth2Authentication auth, Authentication  authentication, HttpServletRequest request, HttpServletResponse response) {
		
		restOperations.delete(url+"/token");
		HttpSession session = request.getSession(false);
		SecurityContextHolder.clearContext();
		session = request.getSession(false);
		//if (session != null) {
			session.invalidate();
		//}
		for (Cookie cookie : request.getCookies()) {
			cookie.setMaxAge(-1);
		}
    	return "";
	}
	
	@RequestMapping(value="/")
	public String index(){
		return "redirect:/home?fp=Home";
	}
	
	@RequestMapping(value = "/pin", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void pin(@RequestParam(value = "pin", required = true) String pin) {
		request.getSession().setAttribute("banner", pin);
	}
}
