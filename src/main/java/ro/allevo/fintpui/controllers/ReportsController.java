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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.QueryParam;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.service.MessageService;
import ro.allevo.fintpui.service.MessageTypeService;
import ro.allevo.fintpui.service.QueueService;
import ro.allevo.fintpui.service.ReportService;
import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.JSONHelper;
import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.PayloadHelper;
import ro.allevo.fintpui.utils.ReportsExportHelper;
import ro.allevo.fintpui.utils.datatables.DataTables;

@Controller
@RequestMapping(value = "/reports")
public class ReportsController {

	private static Logger logger = LogManager.getLogger(ReportsController.class.getName());

	@Autowired
	private MessageService messageService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private QueueService queueService;

	@Autowired
	private MessageTypeService messageTypeService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/general/toPDF", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> ExportToPDF(@RequestParam Map<String, String> allRequestParams)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("/reports/toPDF requested");

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "FinTPMessages" + sdf.format(now) + ".pdf";

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode params = mapper.readValue(allRequestParams.get("params"), ObjectNode.class);

		String businessArea = ((ArrayNode) params.get("businessArea")).get(0).asText();

		ArrayNode selectedColumnsArray = (ArrayNode) params.get("columns");
		List<String> selectedColumns = new ArrayList<String>() {
			{
				for (JsonNode node : selectedColumnsArray)
					add(node.asText());
			} 
		};

		List<String> columnHeaders = getSelectedHeaders(businessArea, selectedColumns);

		List<List<String>> messages = getSelectedMessages(businessArea, params, selectedColumns);

		return ReportsExportHelper.ExportToPDF(fileName, columnHeaders, messages);
	}

	@RequestMapping(value = "/general/toExcel", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> ExportToExcel(@RequestParam Map<String, String> allRequestParams)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("/reports/toExcel requested");

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "FinTPMessages" + sdf.format(now) + ".xlsx";

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode params = mapper.readValue(allRequestParams.get("params"), ObjectNode.class);

		String businessArea = ((ArrayNode) params.get("businessArea")).get(0).asText();

		ArrayNode selectedColumnsArray = (ArrayNode) params.get("columns");
		List<String> selectedColumns = new ArrayList<String>() {
			{
				for (JsonNode node : selectedColumnsArray)
					add(node.asText());
			}
		};

		List<String> columnHeaders = getSelectedHeaders(businessArea, selectedColumns);

		List<List<String>> messages = getSelectedMessages(businessArea, params, selectedColumns);

		return ReportsExportHelper.ExportToExcel(fileName, columnHeaders, messages);
	}

	private List<String> getSelectedHeaders(String businessArea, List<String> selectedColumns) {
		ObjectNode[] columns = reportService.getMessageResults(businessArea);

		List<String> columnHeaders = new ArrayList<String>() {
			{
				add("No");

				for (ObjectNode node : columns)
					if (selectedColumns.contains(node.get("field").asText()))
						add(node.get("label").asText());
			}
		};

		return columnHeaders;
	}

	private List<List<String>> getSelectedMessages(String businessArea, ObjectNode params,
			List<String> selectedColumns) {
		ObjectNode[] messages = messageService.getAllMessages(businessArea, prepareFilters(params));

		List<List<String>> data = new ArrayList<List<String>>() {
			{
				long i = 0;

				for (ObjectNode message : messages) {
					i++;

					List<String> row = new ArrayList<String>();

					row.add(0, i + "");

					for (String column : selectedColumns)
						if (null != message.get(column))
							row.add(message.get(column).asText());
						else
							row.add("");

					add(row);
				}
			}
		};

		return data;
	}

	private LinkedHashMap<String, List<String>> prepareFilters(ObjectNode params) {
		LinkedHashMap<String, List<String>> filters = new LinkedHashMap<String, List<String>>() {
			{
				Iterator<String> iter = params.fieldNames();

				while (iter.hasNext()) {
					String key = iter.next();

					if (key.startsWith("filter_")) {
						JsonNode node = params.get(key);

						if (key.endsWith("_idate")) { // split date interval
							String dates[];
							if (node.isArray()) {
								dates = ((ArrayNode) node).get(0).asText().split(" - ");
							} else {
								dates = ((ArrayNode) node).asText().split(" - ");
							}
							if (dates.length > 1) {
								String baseKey = key.replace("_idate", "");
								put(baseKey + "_ldate", new ArrayList<String>() {
									{
										add(dates[0]);
									}
								});
								put(baseKey + "_udate", new ArrayList<String>() {
									{
										add(dates[1]);
									}
								});
							}
						} else {
							List<String> values = new ArrayList<String>() {
								{
									if (node.isTextual() && !node.asText().isEmpty())
										add(node.asText());
									else if (node.isArray())
										for (JsonNode n : (ArrayNode) node)
											if (n.isTextual() && !n.asText().isEmpty())
												add(n.asText());
								}
							};

							if (values.size() > 0)
								put(key, values);
						}
					}
				}
			}
		};

		return filters;
	}

	@RequestMapping(value = "/general/page", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String dataTable(@RequestParam int draw, @RequestParam Map<String, String> allRequestParams)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("/reports/get requested");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode params = mapper.readValue(allRequestParams.get("params"), ObjectNode.class);

		String businessArea = ((ArrayNode) params.get("businessArea")).get(0).asText();

		PagedCollection<ObjectNode> messages = messageService.getMessages(businessArea, prepareFilters(params));

		DataTables dt = new DataTables();
		if (null != messages) {
			dt.setData(messages.getItems());
			dt.setRecordsFiltered(messages.getTotal());
			dt.setRecordsTotal(messages.getTotal());
		}
		dt.setDraw(draw);
		return JSONHelper.toString(dt);
	}

	@GetMapping(value = "/general")
	public String printForm(ModelMap model) {
		logger.info("/reportsForm requested");

		model.addAttribute("businessareas", messageTypeService.getBusinessAreas());

		model.addAttribute("states", reportService.getTransactionStatuses());
		model.addAttribute("userNames", userService.getUserList());

		model.addAttribute("queues", queueService.getQueueList());

		return "reportsForm";
	}

	@GetMapping(value = "general/search")
	public String printResults(ModelMap model, @RequestParam Map<String, String> allRequestParams)
			throws ParseException, SQLException {
		logger.info("/reports requested");
		model.addAttribute("businessareas", messageTypeService.getBusinessAreas());

		model.addAllAttributes(allRequestParams);

		model.addAttribute("states", reportService.getTransactionStatuses());
		model.addAttribute("userNames", userService.getUserList());

		model.addAttribute("queues", queueService.getQueueList());
		return "reports";
	}

	@RequestMapping(value = "/view-message", method = RequestMethod.GET)
	public String printMessage(ModelMap model, @RequestParam(value = "id", required = true) String id, // correlationid
			@RequestParam(value = "businessArea", required = true) String businessArea) {

		ObjectNode message = messageService.getMessage(businessArea, id);
		model.addAttribute("message", message);

		String messagePayload = messageService.getFeedbackPayload(id);

		try {
			Document payload = PayloadHelper.parsePayload(messagePayload);

			// friendly tags
			PayloadHelper.friendlyPayload(payload);

			model.addAttribute("payload", payload);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("payloadString", StringEscapeUtils.escapeHtml(messagePayload));
		}

		return "viewMessage";
	}
	
	@RequestMapping(value = "/message-status", method = RequestMethod.GET)
	@ResponseBody
	public String status(ModelMap model, @RequestParam(value = "id", required = true) String id, @RequestParam(value = "businessArea", required = true) String businessArea) {
		ObjectNode message = messageService.getMessage(businessArea, id);
		if(message.get("paymentid").textValue()==null) {
			return "Cannot get status code!";
		}
		return "Status code : " + messageService.getMessageStatus(message.findValue("orderingbank").textValue(), message.get("paymentid").textValue()).findValue("statusCode").textValue();
	}

	@RequestMapping(value = "/message-types", method = RequestMethod.GET)
	@ResponseBody
	public String getMessageTypes(ModelMap model,
			@RequestParam(value = "businessArea", required = true) String businessArea) throws JsonProcessingException {
		return JSONHelper.toString(messageTypeService.getMessageTypes(businessArea));
	}

	@RequestMapping(value = "/message-criteria", method = RequestMethod.GET)
	@ResponseBody
	public String getFilters(ModelMap model, @RequestParam(value = "businessArea", required = true) String businessArea)
			throws JsonProcessingException {
		return JSONHelper.toString(reportService.getMessageCriteria(businessArea));
	}

	@RequestMapping(value = "/message-results", method = RequestMethod.GET)
	@ResponseBody
	public String getColumns(ModelMap model, @RequestParam(value = "businessArea", required = true) String businessArea)
			throws JsonProcessingException {
		return JSONHelper.toString(reportService.getMessageResults(businessArea));
	}
	
	@RequestMapping(value = "/getfilter", method = RequestMethod.GET)
	@ResponseBody
	public String getFilter(ModelMap model, OAuth2Authentication auth, @RequestParam(value = "report", required = true) String businessArea) throws JsonProcessingException {
		String user = auth.getName();
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("filter_insertdate_idate", "2019-09-23 00:00:00 - 2019-12-23 23:59:59");
		map.put("filter_endtoendid", "original ID");
		map.put("filter_reference", "erfasdfasdf");
		map.put("filter_valuedate", "2019-12-24");
		map.put("filter_status_exact", "New");
		map.put("filter_sourcefilename", "");
		map.put("filter_destinationfilename", "");
		map.put("filter_dbtcustomername_exact", "Bam Brasov");
		map.put("filter_dbtaccount_exact", "RO90CECEIL0103RON0000007");
		map.put("filter_orderingbank_exact", "CECEROBU");
		map.put("filter_cdtcustomername_exact", "Martinel SA");
		map.put("filter_cdtaccount_exact", "RO06RNCB0124001876033202");
		map.put("filter_beneficiarybank_exact", "VICBMD2X");
		map.put("filter_amount_lnum",  "");
		map.put("filter_currency_exact",  "RON");
		
		return JSONHelper.toString(map);
		//return JSONHelper.toString(reportService.getFilterResults(businessArea, user));
	}
	
	@GetMapping(value = "/savefilter")
	public void saveFilter(@QueryParam(value="filters") String[][] filters) {
		System.out.println(filters);
	}
}
