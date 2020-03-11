package ro.allevo.out.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "out")
public class OutController {
	
	@GetMapping(value = "page")
	public String showLogin() /* throws JsonProcessingException */ {
//		logger.info("/connection required");
//		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_VIEW)))
//			throw new NotAuthorizedException();
//		model.addAttribute("hasModifyRole",
//				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.BANKS_LIST_MODIFY)));

		// model.addAttribute("countries",
		// JSONHelper.toString(countryService.getAllCountries()));
//		model.addAttribute("apiUri", config.getConnectUrl());
		return "out/page";
	}
	
	@GetMapping(value = "gencode")
	public String genCode(@RequestParam(value="redirect") String redirect) {
		return "out/gencode";
	}

}
