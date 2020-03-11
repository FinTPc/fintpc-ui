package ro.allevo.fintpui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.Template;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.model.TemplateDetailed;
import ro.allevo.fintpui.model.TemplateGroup;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.service.InternalEntitiesService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.TemplateConfigDetailedService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.service.TemplateDetailedService;
import ro.allevo.fintpui.service.TemplateGroupService;
import ro.allevo.fintpui.service.TemplatesService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/templates")
public class TemplatesController {

	@Autowired
	Config config;

	@Autowired
	private TemplatesService templatesService;

	@Autowired
	private TemplateDetailedService templateDetailedService;
	
	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;
	
	@Autowired
	private TemplateConfigService templateConfigService;
	
	@Autowired
	private TemplateConfigDetailedService templateConfigDetailedService;
	
	@Autowired
	private TemplateGroupService templateGroupService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageTypeService messageTypeService;
	
	@Autowired
	private InternalEntitiesService internalEntitiesService;
	
	private static Logger logger = LogManager.getLogger(TemplatesController.class.getName());

	/*
	 * DISPLAY
	 */
	@GetMapping
	public String printTemplates(OAuth2Authentication auth, ModelMap model)
			throws JsonProcessingException {
		logger.info("/templates required");
		model.addAttribute("hasModifyRole",true);
		
		return "templates";
	}

	/*
	 * DISPLAY
	 */
	@GetMapping(value = "/page")
	public @ResponseBody String getTemplatesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/templates required");
		PagedCollection<Template> templates = templatesService.getPage();

		DataTables dt = new DataTables();
		if (null != templates) {
			dt.setData(templates.getItems());
			dt.setRecordsFiltered(templates.getTotal());
			dt.setRecordsTotal(templates.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}
	
	private List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}

	/*
	 * INSERT
	 */
	@GetMapping(value = "/add")
	public String addTemplate(ModelMap model, @ModelAttribute("template") Template template) {
		logger.info("/addTemplate required");
		template.setType(0);
		model.addAttribute("template", template);
		
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<String, List<String>>();
		filter.put("filter_type_exact", getFiltersParams("0"));
		
		MessageType[] messageTypes = messageTypeService.getMessageTypes();
		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig();
		
		for(TemplateConfig templateConfig : templateConfigs) {
			for(MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templateConfig.getMessagetype())) {
					templateConfig.setMessagetype(messageType.getFriendlyName());
					break;
				}
			}
		}
		
		model.addAttribute("simpleTemplates", templatesService.getAllTemplates(filter));
		model.addAttribute("templatesConfig", templateConfigs);
		model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues());
		
		model.addAttribute("formAction", "templates/insert");
		return "templates_add";
	}
	
	
	@GetMapping(value = "{configid}/fields")
	@ResponseBody
	public Map<String, Object> addXPaths(@PathVariable Integer configid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fields", templateConfigDetailedService.getAllTemplateConfigDetailed(configid));
		map.put("internal", internalEntitiesService.getAllInternalEntitiesByRights(templateConfigService.getTemplateConfig(configid).getMessagetype()));
		return map;
	}
	
	@GetMapping(value = "{configid}/groups")
	@ResponseBody
	public Template[] getSimpleTemplate(@PathVariable Integer configid) {
		
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<String, List<String>>();
		filter.put("filter_type_exact", getFiltersParams("0"));
		filter.put("filter_txtemplatesconfig.id_exact", getFiltersParams(configid.toString()));
		
		Template[] templates = templatesService.getAllTemplates(filter);
		
		return templates;
	}

//	public void groupRoutingJobs(HttpServletRequest request, @RequestParam Map<String, String> allRequestParams)
//			throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		ObjectNode item = objectMapper.readValue(allRequestParams.get("item"), ObjectNode.class);
//
//		String[] grpKeys = new String[item.get("groupKeys").size()];
//
//		for (int i = 0; i < item.get("groupKeys").size(); i++) {
//			grpKeys[i] = item.get("groupKeys").get(i).asText();
//		}
	
	
	@PostMapping(value = "insert")
	@ResponseBody
	public String insertTemplate( @RequestParam("template") String templateAsString) throws IOException {
		logger.info("insert template requested");
		ObjectMapper objectMapper = new ObjectMapper();
		Template template = objectMapper.readValue(templateAsString, Template.class);
		
		TemplateConfig  templateConfig = templateConfigService.getTemplateConfig(template.getTxtemplatesconfig().getId().intValue());
		for(TemplateDetailed templateDetailed : template.getTxtemplatesdetaileds()) {
			if (null != templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption() &&
				templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption().getDatasource().equals("internal-entities.name") 
				&& templateDetailed.getTxtemplatesconfigdetailed().getFieldxpath().equals(templateConfig.getType())) {
				template.setEntity(templateDetailed.getValue());
				break;
			}
		}

		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		template.setUserid(user.getId());
		
		TemplateDetailed[] templateDetaileds = template.getTxtemplatesdetaileds();
		TemplateGroup[] templateGroups = template.getTxtemplatesgroups();
		template.setTxtemplatesdetaileds(null);
		template.setTxtemplatesgroups(null);
		ResponseEntity<String> abc = templatesService.insertTemplate(template);
		
		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<String, List<String>>();
		filter.put("filter_name_exact", getFiltersParams(template.getName()));
		
		Template[] insertTemplate = templatesService.getAllTemplates(filter);
		Template templateNew = insertTemplate[0];
		//insert template detailed
		for(TemplateDetailed templateDetailed: templateDetaileds) {
			templateDetailed.setTxtemplate(templateNew);
			templateDetailedService.insertTemplateDetailed(templateDetailed);
		}
		//insert template group
		for(TemplateGroup templateGroup : templateGroups) {
			templateGroup.setTxtemplate(templateNew);
			templateGroup.setUserid(user.getId());
			templateGroupService.insertTemplateGroup(templateGroup);
			
		}

		return "[]";
	}

	/*
	 * DELETE
	 */
	@RequestMapping(value = "/{id}/delete")
	public String deleteTemplate(@PathVariable Integer id) {
		logger.info("delete template required");
		templatesService.deleteTemplate(id);
		return "redirect:/templates";
	}

	/*
	 * EDIT
	 */
	@GetMapping(value = "/{id}/edit")
	public String editTemplate(ModelMap model, @ModelAttribute("template") Template template, @PathVariable Integer id) {
		logger.info("/editTemplate requested");
		template = templatesService.getTemplate(id);

//		TemplateConfig[] templateConfigs = this.templateConfigService.getAllTemplateConfig();
//		for(int i = 0; i < templateConfigs.length;i++) {
//			Template[] arrTemplates = templateConfigs[i].getTxtemplates();
//			for (Template templates : arrTemplates) {
//				if (template.getId() == template.getId()) {
//					templateConfig = templateConfigs[i];
//					break;
//				}
//			}
//		}
//		template.setTxtemplatesconfig(templateConfig);

		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<String, List<String>>();
		filter.put("filter_type_exact", getFiltersParams("0"));
		filter.put("filter_configid_exact", getFiltersParams(String.valueOf(template.getTxtemplatesconfig().getId())));

		Template[] simpleTemplates = templatesService.getAllTemplates(filter);
		for(TemplateGroup templateGroup : template.getTxtemplatesgroups()) {
			Integer templateId = templateGroup.getGroupid();
			for (Template simpleTemplate : simpleTemplates) {
				if (simpleTemplate.getId().equals(templateId)) {
					templateGroup.setName(simpleTemplate.getName());
					break;
				}
			}
		}

		MessageType[] messageTypes = messageTypeService.getMessageTypes();
		TemplateConfig[] templateConfigs = templateConfigService.getAllTemplateConfig();

		for(TemplateConfig templatesConfig : templateConfigs) {
			for(MessageType messageType : messageTypes) {
				if (messageType.getMessageType().equals(templatesConfig.getMessagetype())) {
					templatesConfig.setMessagetype(messageType.getFriendlyName());
					break;
				}
			}
		}

		model.addAttribute("simpleTemplates", simpleTemplates);
		model.addAttribute("templatesConfig", templateConfigs);
		model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues());
		model.addAttribute("template", template);

//		Map<Integer, String> fields = new HashMap<Integer, String>();

//		for(TemplateDetailed field:template.getTxtemplatesdetaileds()) {
//			fields.put(field.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption().getId(), field.getTxtemplatesconfigdetailed().getFieldlabel());
//		}
//		model.addAttribute("xPaths", fields);

		model.addAttribute("formAction", "templates/update");

		return "templates_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateTemplate(@RequestParam("template") String templateAsString) throws IOException {
		logger.info("update template requested");
		ObjectMapper objectMapper = new ObjectMapper();
		Template template = objectMapper.readValue(templateAsString, Template.class);
		
		Template oldTemplate = templatesService.getTemplate(template.getId());
		
		if (template.getTxtemplatesconfig().getId() != oldTemplate.getTxtemplatesconfig().getId()) {
			templateDetailedService.deleteTemplateDetailedByTemplateId(template.getId());
		}
		
		TemplateConfig  templateConfig = templateConfigService.getTemplateConfig(template.getTxtemplatesconfig().getId().intValue());
		for(TemplateDetailed templateDetailed : template.getTxtemplatesdetaileds()) {
			if (null != templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption() &&
					templateDetailed.getTxtemplatesconfigdetailed().getTxtemplatesconfigoption().getDatasource().equals("internal-entities.name") 
					&& templateDetailed.getTxtemplatesconfigdetailed().getFieldxpath().equals(templateConfig.getType())) {
				template.setEntity(templateDetailed.getValue());
				break;
			}
		}


		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		template.setUserid(user.getId());

		TemplateDetailed[] templateDetaileds = template.getTxtemplatesdetaileds();
		TemplateGroup[] templateGroups = template.getTxtemplatesgroups();
		template.setTxtemplatesdetaileds(null);
		template.setTxtemplatesgroups(null);
		templatesService.updateTemplate(template, template.getId());

		//insert template detailed
		for(TemplateDetailed templateDetailed: templateDetaileds) {
			templateDetailed.setTxtemplate(template);
			if (null != templateDetailed.getId()) {
				templateDetailedService.updateTemplateDetailed(templateDetailed);
			}else {
				templateDetailedService.insertTemplateDetailed(templateDetailed);
			}
		}
		
		TemplateGroup[] templatesGroups = templateGroupService.getAllTemplateGroups(template.getId());
		//insert template group
		for(TemplateGroup templateGroup : templateGroups) {
			templateGroup.setTxtemplate(template);
			if (null != templateGroup.getId()) {
				int i;
				for(i = 0; i< templatesGroups.length; i++) {
					if (templatesGroups[i].getId().equals(templateGroup.getId())) {
						break;
					}
				}
				if (i < templatesGroups.length) {
					templatesGroups = (TemplateGroup[]) ArrayUtils.remove(templatesGroups, i);
				}
				templateGroupService.updateTemplateGroup(templateGroup);
			}else {
				templateGroupService.insertTemplateGroup(templateGroup);
			}
		}
		for(TemplateGroup templateGroup : templatesGroups) {
			templateGroupService.deleteTemplateGroup(templateGroup.getId());
		}
		return "[]";
	}

}
