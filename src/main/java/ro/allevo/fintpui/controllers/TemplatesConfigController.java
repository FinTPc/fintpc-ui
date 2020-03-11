package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.allevo.fintpui.model.Template;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.service.TemplateConfigDetailedService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.service.TemplatesService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "templates-config")
public class TemplatesConfigController {
	
	@Autowired
	TemplatesService templatesService;
	
	@Autowired
	TemplateConfigService templateConfigService;
	
	@Autowired
	TemplateConfigOptionsService templateConfigOptionsService;
	
	@Autowired
	TemplateConfigDetailedService templateConfigDetailedService;
	
	private static Logger logger = LogManager.getLogger(TimeLimitsController.class.getName());
	
	@GetMapping
	public String getTemplates(ModelMap model) {
		model.addAttribute("templates", templateConfigService.getAllTemplateConfig());
		model.addAttribute("title", "Template Config");
		model.addAttribute("templateId", 0);
		return "templateOpen";
	}
	
	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getBanksJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/templates-config required");
		PagedCollection<TemplateConfig> templatesConfig = templateConfigService.getPage();

		DataTables dt = new DataTables();
		if (null != templatesConfig) {
			dt.setData(templatesConfig.getItems());
			dt.setRecordsFiltered(templatesConfig.getTotal());
			dt.setRecordsTotal(templatesConfig.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	@GetMapping(value = "/{id}/open")
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseBody TemplateConfig openTemplateConfig(ModelMap model, @PathVariable String id) throws NumberFormatException{
		TemplateConfig templateConfig = templateConfigService.getTemplateConfig(Integer.parseInt(id));
		//model.addAttribute("templateConfig", templateConfigService.getTemplateConfig(Long.parseLong(id)));
		return templateConfig;
	}
	
	@GetMapping(value = "{id}")
	public String getTemplateConfigById(ModelMap model, @PathVariable String id) throws NumberFormatException{
		int configId = Integer.parseInt(id);
		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig();
		model.addAttribute("templateId", id);
		model.addAttribute("templates", templateConfigs);
		model.addAttribute("template", templateConfigService.getTemplateConfigWithXsd(configId));
		model.addAttribute("options", templateConfigOptionsService.getAllTemplateConfig());
		model.addAttribute("fields", templateConfigDetailedService.getAllTemplateConfigDetailed(configId));
		return "templateOpen";
	}

//	@GetMapping(value = "/options")
//	@Produces(MediaType.APPLICATION_JSON)
//	public @ResponseBody TemplateConfigOptions[] showTemplateConfigOptions() {
//		return templateConfigOptionsService.getAllTemplateConfig();
//	}
	
	private List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}
	
	@PostMapping(value = "save")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseBody
	public Response save(@RequestParam("detailed") String detailed, @RequestParam("config") String config) throws JsonParseException, JsonMappingException, IOException {

		List<ResponseEntity<String>> response = new ArrayList<ResponseEntity<String>>(); 
		
		ObjectMapper objectMapper = new ObjectMapper();
		TemplateConfigDetailed[] templateConfigDetaileds = objectMapper.readValue(detailed, TemplateConfigDetailed[].class);
		
		TemplateConfig templateConfig = objectMapper.readValue(config, TemplateConfig.class);
		templateConfigService.updateTemplateConfig(templateConfig);
		
		TemplateConfigDetailed[] fields = templateConfigDetailedService.getTemplateConfigDetailedByFieldId( templateConfig.getId().intValue() );

		for(TemplateConfigDetailed templateConfigDetailed : templateConfigDetaileds) {
			if (null == templateConfigDetailed.getId()) {
				response.add(templateConfigDetailedService.insertTemplateConfigDetailed(templateConfigDetailed));
			}
			if (null != templateConfigDetailed.getId() && templateConfigDetailed.getId() > 0) {
				response.add(templateConfigDetailedService.updateTemplateConfigDetailed(templateConfigDetailed));
				int i = 0;
				for(i = 0; i< fields.length; i++) {
					if (fields[i].getId().equals(templateConfigDetailed.getId())) {
						break;
					}
				}
				if (i < fields.length) {
					fields = (TemplateConfigDetailed[]) ArrayUtils.remove(fields, i);
				}
			}
//			if(templateConfigDetailed.getFieldxpath().equalsIgnoreCase(templateConfig.getType())) {
//				LinkedHashMap<String, List<String>> filter = new LinkedHashMap<String, List<String>>();
//				filter.put("filter_txtemplatesconfig.id_exact", getFiltersParams(""+templateConfig.getId()));
//				Template[] templates = templatesService.getAllTemplates(filter);
//				for(Template template : templates){
//					template.setEntity(templateConfig.getType());
//					templatesService.updateTemplate(template,template.getId());
//				}
//			}
		}
		for (TemplateConfigDetailed field : fields) {
			templateConfigDetailedService.deleteTemplateConfigDetailed(field.getId());
		}
		return Response.ok().build();
	}
}
