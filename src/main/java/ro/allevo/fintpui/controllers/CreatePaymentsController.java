package ro.allevo.fintpui.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.model.Manualtransactions;
import ro.allevo.fintpui.model.Template;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateGroup;
import ro.allevo.fintpui.model.User;
import ro.allevo.fintpui.service.ManualtransactionsService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.service.TemplatesService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.Filters;
import ro.allevo.fintpui.utils.PayloadHelper;

@Controller
@RequestMapping(value = "payment-create")
public class CreatePaymentsController {

	@Autowired
	TemplatesService templateService;

	@Autowired
	TemplateConfigService templateConfigService;

	@Autowired
	TemplateConfigOptionsService templateConfigOptions;
	
	@Autowired
	ManualtransactionsService manualtransactionsService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	private UserService userService;

	@GetMapping
	public String getPayments(ModelMap model) {
//		model.addAttribute("validationXsd", 1);
//		model.addAttribute("payments", 0);
//		model.addAttribute("sample", 0);
		model.addAttribute("title", "Create payments");
		model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues());
		return "paymentCreate";
	}

	@GetMapping(value = "/{id}/sample")
	@ResponseBody
	public Template[] getSample(@PathVariable Integer id) {

		LinkedHashMap<String, List<String>> filter = new LinkedHashMap<String, List<String>>();
		switch (id) {
		case 2:
			filter.put("filter_type_exact", Filters.getFiltersParams("0"));
			break;
		case 3:
			filter.put("filter_type_exact", Filters.getFiltersParams("1"));
			break;
		}

		Template[] templates = templateService.getAllTemplates(filter);
		return templates;
	}

	@GetMapping(value = "/{id}/config")
	@ResponseBody
	public TemplateConfig getConfigDetailed(@PathVariable Integer id) {
		TemplateConfig templatesConfigs = templateConfigService.getTemplateConfig(id);
		return templatesConfigs;
	}

	@GetMapping(value = "/{id}/template")
	@ResponseBody
	public List<Template> getTemplate(@PathVariable Integer id) {
		List<Template> listTemplate = new ArrayList<Template>();
		Template template = templateService.getTemplate(id);
		if (template.getTxtemplatesgroups().length > 0) {
			for (TemplateGroup group : template.getTxtemplatesgroups()) {
				listTemplate.add(templateService.getTemplate(group.getGroupid()));
			}
		} else {
			listTemplate.add(template);
		}
		return listTemplate;
	}

	@GetMapping(value = "/templates")
	@ResponseBody
	public TemplateConfig[] getTemplatesConfigs() {
		return templateConfigService.getAllTemplateConfig();
	}

	@GetMapping(value = "{id}")
	public String getTemplatesConfig(ModelMap model, @PathVariable Integer id,
			@QueryParam(value = "payments") Integer payments, @QueryParam(value = "sample") Integer sample) {
		model.addAttribute("validationXsd", templateConfigService.getTemplateConfigWithXsd(id));
		model.addAttribute("configs", templateConfigService.getAllTemplateConfig());
		model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues());
		model.addAttribute("payments", payments);
		model.addAttribute("sample", sample);
		return "paymentCreate";
	}

	@GetMapping(value = "/create-payload")
	@ResponseBody
	public String savePayload(@RequestParam("payload") String payload) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		List<LinkedHashMap<String, String>> payloadMap = new ArrayList<LinkedHashMap<String, String>>();
		payloadMap = objectMapper.readValue(payload, List.class);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element element = doc.createElement("Document");
		doc.createElementNS("xmlns", "urn:iso:std:iso:20022:tech:xsd:camt.054.001.06Alv");
		doc.appendChild(element);
		
		for(LinkedHashMap<String, String> xPaths : payloadMap) {
			System.out.println(xPaths.values());
			String[] segments = xPaths.get("fieldxpath").split("/");
			segments = (String[]) ArrayUtils.remove(segments, 0);
	        PayloadHelper.createElement(doc, doc.getDocumentElement(), segments, 1, xPaths.get("fieldvalue")); 
		}
		System.out.println(PayloadHelper.getStringFromDoc(doc));
		Manualtransactions payment = new Manualtransactions();
		
		HttpSession session = request.getSession();
		String userName = session.getAttribute("userName").toString();
		User user = userService.getUser(userName);
		
		payment.setPayload(PayloadHelper.getStringFromDoc(doc));
		payment.setStatus(0);
		payment.setUserid(user.getId());
		manualtransactionsService.insertManualtransactions(payment);
		return "paymentCreate";
	}
}
