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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.model.MessageType;
import ro.allevo.fintpui.model.MessagesGroup;
import ro.allevo.fintpui.model.Queue;
import ro.allevo.fintpui.model.QueuesCountEntity;
import ro.allevo.fintpui.model.RoutingJobParameters;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigDetailed;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.model.UserAction;
import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.QueueService;
import ro.allevo.fintpui.service.ReportService;
import ro.allevo.fintpui.service.ServiceMapService;
import ro.allevo.fintpui.service.TemplateConfigDetailedService;
import ro.allevo.fintpui.service.TemplateConfigOptionsService;
import ro.allevo.fintpui.service.TemplateConfigService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.Pair;
import ro.allevo.fintpui.utils.PayloadHelper;
import ro.allevo.fintpui.utils.Roles;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/queues")
public class QueuesController {

	@Autowired
	Config config;

	@Autowired
	private QueueService queueService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ServiceMapService serviceMapService;

	@Autowired
	private MessageTypeService messageTypeService;

	@Autowired
	private MessageService messagesService;
	
	@Autowired
	private TemplateConfigDetailedService templateConfigDetailedService;
	
	@Autowired
	private TemplateConfigService templateConfigService;
	
	@Autowired
	private TemplateConfigOptionsService templateConfigOptions;
	
	private static Logger logger = LogManager.getLogger(QueuesController.class.getName());

	/*
	 * DISPLAY Transactions
	 */
	@GetMapping
	public ModelAndView printMenu(OAuth2Authentication auth, ModelMap model) {

		logger.info("/queues requested");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.QUEUES_VIEW)))
			throw new NotAuthorizedException();

		return new ModelAndView("queues", model);
	}

	/* Display Administration Queues */
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView printMenuQueuesAdmin(OAuth2Authentication auth, ModelMap model) {

		logger.info("/queues admin requested");
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.QUEUES_VIEW)))
			throw new NotAuthorizedException();
		model.addAttribute("hasModifyRole",
				auth.getAuthorities().contains(new SimpleGrantedAuthority(Roles.QUEUES_MODIFY)));

		return new ModelAndView("queuesAdmin", model);
	}

	@GetMapping(value = "/count")
	public @ResponseBody String getQueuesCount() throws JsonProcessingException {
		logger.info("/queues required");
		Map<Integer, Long> queuesCount = new HashMap<Integer, Long>();
		long item = 0;
		long sumNoOfTx = 0;
		for (QueuesCountEntity qCount : queueService.getQueuesCount()) {
			queuesCount.put(qCount.getId(), qCount.getNoOfTx());
			item++;
			sumNoOfTx += qCount.getNoOfTx();
		}
		queuesCount.put(0, item);
		queuesCount.put(-1, sumNoOfTx);
		return JSONHelper.toString(queuesCount);
	}

	@GetMapping(value = "/page")
	public @ResponseBody String getQueuesJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/queues required");
		PagedCollection<Queue> queues = queueService.getPage();

		DataTables dt = new DataTables();
		if (null != queues) {
			dt.setData(queues.getItems());
			dt.setRecordsFiltered(queues.getTotal());
			dt.setRecordsTotal(queues.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@GetMapping(value = "/page-finale")
	public @ResponseBody String getTransactionsFinaleJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/queues required");
		PagedCollection<Queue> queues = queueService.getTransactionsFinale();

		DataTables dt = new DataTables();
		if (null != queues) {
			dt.setData(queues.getItems());
			dt.setRecordsFiltered(queues.getTotal());
			dt.setRecordsTotal(queues.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@GetMapping(value = "/page-interm")
	public @ResponseBody String getTransactionsIntermJson(@RequestParam int draw) throws JsonProcessingException {
		logger.info("/queues required");
		PagedCollection<Queue> queues = queueService.getTransactionsInterm();

		DataTables dt = new DataTables();
		if (null != queues) {
			dt.setData(queues.getItems());
			dt.setRecordsFiltered(queues.getTotal());
			dt.setRecordsTotal(queues.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	/*
	 * INSERT
	 */

	@GetMapping(value = "/add")
	public String addQueue(ModelMap model, @ModelAttribute("queue") Queue queue) {
		logger.info("/addQueue required");
		model.addAttribute("queue", queue);
		model.addAttribute("types", getQueueTypes("edit"));
		model.addAttribute("formAction", "insert");
		model.addAttribute("connectors", serviceMapService.getAllServiceNamesAndId());
		return "queues_add";
	}	

	@PostMapping(value = "insert")
	@ResponseBody
	public String insertBank(ModelMap model, @Valid @ModelAttribute("queue") Queue queue, BindingResult bindingResult)
			throws JsonProcessingException {
		logger.info("insert queue requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		queueService.insertQueue(queue);
		return "[]";
	}

	/*
	 * EDIT
	 */

	@GetMapping(value = "/{queueName}/edit")
	public String editBank(ModelMap model, @ModelAttribute("queue") Queue queue, @PathVariable String queueName) {
		logger.info("/editQueue requested");
		queue = queueService.getQueue(queueName);
		model.addAttribute("queue", queue);
		model.addAttribute("types", getQueueTypes("edit"));
		model.addAttribute("formAction", "update");
		model.addAttribute("connectors", serviceMapService.getAllServiceNamesAndId());
		return "queues_add";
	}

	@PostMapping(value = "/update")
	@ResponseBody
	public String updateBank(ModelMap model, @ModelAttribute("queue") @Valid Queue queue, BindingResult bindingResult,
			@RequestParam("name") String name) throws JsonProcessingException {
		logger.info("update queue requested");
		if (bindingResult.hasErrors()) {
			return JSONHelper.toString(bindingResult.getAllErrors());
		}
		queueService.updateQueue(name, queue);
		return "[]";
	}

	/*
	 * DELETE
	 */

	@RequestMapping(value = "/{queueName}/delete")
	public String deleteQueue(@PathVariable String queueName) {
		logger.info("delete queue required");
		queueService.deleteQueue(queueName);
		return "redirect:/queues/admin";
	}

	/*
	 * @RequestMapping(value = "getQueueButtons", method = RequestMethod.GET)
	 * public @ResponseBody String getQueueButtons(@RequestParam Map<String,String>
	 * allRequestParams) { JSONObject r = new JSONObject(); return r.toString(); }
	 */

	// @RequestMapping(value = "/view-payload", method = RequestMethod.GET)
	@GetMapping(value = "/payload")
	public String viewPayload(ModelMap model, @RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "type", required = true) String messageType,
			@RequestParam(value = "action", required = false, defaultValue="view") String action) {

		String businessArea = messageTypeService.getMessageType(messageType).getBusinessArea();

		ObjectNode entryQueueMessage = messageService.getEntryQueueMessage(id);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document payload = null;
		String correlationId = "";

		try {
			correlationId = entryQueueMessage.get("correlationId").asText();

			builder = factory.newDocumentBuilder();
			// payload = builder.parse(new InputSource(new
			// StringReader(entryQueueMessage.get("payload").asText())));
			payload = PayloadHelper.parsePayload(entryQueueMessage.get("payload").asText());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("payloadString",
					StringEscapeUtils.escapeHtml(entryQueueMessage.get("payload").asText()));
		}

		ObjectNode message = messageService.getMessage(businessArea, correlationId);// correlationid
		model.addAttribute("message", message);

		// friendly tags
		if(action.equals("view")) {
			if (null != payload)
				PayloadHelper.friendlyPayload(payload);
		}else{
			List<TemplateConfig> templatesConfig = new ArrayList(Arrays.asList(templateConfigService.getAllTemplateConfig()));
			Optional<TemplateConfig> config = templatesConfig.stream().filter(o -> o.getMessagetype().equals(messageType)).findFirst();
	    	Long configTemplateId = null;
			if(config.isPresent()){
				configTemplateId = config.get().getId();
	    	}
			TemplateConfigDetailed[] configOptions = templateConfigDetailedService.getAllTemplateConfigDetailed(configTemplateId.intValue());
			
			model.addAttribute("dropDowns", templateConfigOptions.getAllConfingOptionsValues());
			Map<Integer, String> options = new HashMap<Integer, String>();
			for(TemplateConfigOptions option:templateConfigOptions.getAllTemplateConfig()) {
				options.put(option.getId(), option.getDatasource());
			}
			model.addAttribute("templateOptions", options);
			model.addAttribute("id", id);
			if (null != payload) {
				PayloadHelper.editPayload(payload, configOptions);
			}
		}
		model.addAttribute("payload", payload);
		model.addAttribute("action", action);
		
		return "viewMessage";
	}
	
	@PostMapping(value = "{id}/save-payload")
	@ResponseBody
	public String savePayload(ModelMap model, @PathVariable(value = "id", required = true) String id, @RequestParam() Map<String, String> map) throws ParserConfigurationException, SAXException, IOException  {
		
		ObjectNode entryQueueMessage = messageService.getEntryQueueMessage(id);
		queueService.updatePayload(entryQueueMessage.get("correlationId").asText(), PayloadHelper.savePayload(PayloadHelper.parsePayload(entryQueueMessage.get("payload").asText()), map)); 
		return "";
	}
	

	@RequestMapping(value = "/{queueName}", method = RequestMethod.GET)
	public String viewQueue(@PathVariable String queueName, ModelMap model) {
		logger.info("/queues/" + queueName + " requested");

		// add queue name attribute
		model.addAttribute("queueName", queueName);

		List<MessageType> messageTypes = new ArrayList<MessageType>();

		// build hashmap of headers (message type is the key, array
		// containing table headers is the value)
		// get groups for current message type
		// if there are no groups, then treat as a non-batchalbe page

		//
		// headers Map is a hash in which key: messageType (s.a. 103)
		// value: array of strings which represent the table headers
		// name (s.a. Sender, Receiver, Reference ...)
		//
		// HashMap<String, List<String>> headersMap = new HashMap<>();
		//
		// columns Map is a hash in which key: messageType (s.a. 103)
		// value: array of strings which represent the fields contained
		// by the json object returned by fintp API (s.a. sender,
		// receiver, trn ...) they correspond in a 1-1 relantionship
		// with headersMap
		//
		HashMap<String, ObjectNode[]> columnsMap = new HashMap<>();

		//
		// gropsMap is a hash table in which key: message type value :
		// list of groups belonging to that message type
		//
		HashMap<String, List<MessagesGroup>> groupsMap = new HashMap<>();

		//
		// gropsFieldsMap is a hash table in which key: message type
		// (that admits groups/batches) value : "grouped by" fields
		//
		HashMap<String, ObjectNode[]> groupFieldsMap = new HashMap<>();

		// HashMap<MessageType, Boolean> msgType = new HashMap<>();

		// add messagetypes array
		try {
			messageTypes.addAll(Arrays.asList(queueService.getMessageTypesInQueue(queueName)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < messageTypes.size(); i++) {
			String messageType = messageTypes.get(i).getMessageType();

			columnsMap.put(messageType, reportService.getTableHeaders(messageType));

			groupsMap.put(messageType, reportService.getTransactionsInGroup(queueName, messageType));

			groupFieldsMap.put(messageType, reportService.getGroupHeaders(messageType));
		}

		model.addAttribute("messageTypes", messageTypes);
		model.addAttribute("columns", columnsMap);
		model.addAttribute("groupsMap", groupsMap);
		model.addAttribute("groupFieldNames", groupFieldsMap);

		UserAction[] selectionActions = null;
		UserAction[] groupActions = null;

		for (MessageType messageType : messageTypes) {
			selectionActions = queueService.getSelectionActions(queueName, messageType.getMessageType());
			groupActions = queueService.getGroupActions(queueName, messageType.getMessageType());
		}

		model.addAttribute("selActions", selectionActions);
		model.addAttribute("grpActions", groupActions);

		return "queue";
	}

	@GetMapping(value = "/page-messages")
	public @ResponseBody String getMessagesDynamicJson(@RequestParam int draw, @RequestParam String queueName,
			@RequestParam String messageType) throws JsonProcessingException, JSONException {

		PagedCollection<ObjectNode> messages = messagesService.getMessagesInQueue(queueName, messageType);

		DataTables dt = new DataTables();
		if (null != messages) {
			dt.setData(messages.getItems());
			dt.setRecordsFiltered(messages.getTotal());
			dt.setRecordsTotal(messages.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@PostMapping(value = "/message-routing-jobs")
	@ResponseBody
	public String messageRoutingJobs(HttpServletRequest request, @RequestParam Map<String, String> allRequestParams)
			throws JsonParseException, JsonMappingException, IOException, org.json.JSONException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode item = objectMapper.readValue(allRequestParams.get("item"), ObjectNode.class);
		String[] msgIds = new String[item.get("messageId").size()];

		for (int i = 0; i < item.get("messageId").size(); i++) {
			msgIds[i] = item.get("messageId").get(i).asText();
		}

		String reason = "";

		if (null != item.get("reason"))
			reason = item.get("reason").asText();
		
		String details = "";
		if (null != item.get("details"))
			details = item.get("details").asText();

		RoutingJobParameters params = new RoutingJobParameters(item.get("action").asText(), reason, details,
				item.get("messageType").asText(), msgIds);
		queueService.sendRoutingJobs(item.get("queueName").asText(), params);
		
		return item.get("action").asText();
	}

	@PostMapping(value = "/group-routing-jobs")
	@ResponseBody
	public void groupRoutingJobs(HttpServletRequest request, @RequestParam Map<String, String> allRequestParams)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode item = objectMapper.readValue(allRequestParams.get("item"), ObjectNode.class);

		String[] grpKeys = new String[item.get("groupKeys").size()];

		for (int i = 0; i < item.get("groupKeys").size(); i++) {
			grpKeys[i] = item.get("groupKeys").get(i).asText();
		}

		String[] timeKeys = new String[item.get("timeKeys").size()];

		for (int i = 0; i < item.get("timeKeys").size(); i++) {
			timeKeys[i] = item.get("timeKeys").get(i).asText();
		}

		String[][] fieldValues = new String[item.get("fieldValues").size()][3];
		for (int i = 0; i < item.get("fieldValues").size(); i++)
			for (int j = 0; j < item.get("fieldValues").get(i).size(); j++) {
				fieldValues[i][j] = item.get("fieldValues").get(i).get(j).asText();
			}

		RoutingJobParameters params = new RoutingJobParameters(item.get("action").asText(), "", "",
				item.get("messageType").asText(), grpKeys, timeKeys, fieldValues);
		queueService.sendRoutingJobs(item.get("queueName").asText(), params);
	}
	
	private List<Pair<String, String>> getQueueTypes(String omitType){
		List<Pair<String, String>> result = new ArrayList<Pair<String,String>>();
		List<Pair<String, String>> listToVerify = queueService.getQueueTypes();
		for(Pair<String, String> type :listToVerify) {
			if(!type.getSecond().equalsIgnoreCase(omitType)) {
				result.add(type);
			}
		}
		return result;
	}
}
