///*
//* FinTP - Financial Transactions Processing Application
//* Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
//*
//* This program is free software: you can redistribute it and/or modify
//* it under the terms of the GNU General Public License as published by
//* the Free Software Foundation, either version 3 of the License, or
//* (at your option) any later version.
//*
//* This program is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//* GNU General Public License for more details.
//*
//* You should have received a copy of the GNU General Public License
//* along with this program. If not, see <http://www.gnu.org/licenses/>
//* or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
//* phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
//*/
//
//package ro.allevo.fintpui.utils.servlets;
//
//import java.io.IOException;
//import java.net.URI;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.UriBuilder;
//
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.client.RestOperations;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientHandlerException;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.UniformInterfaceException;
//import com.sun.jersey.api.client.WebResource;
//
//@WebServlet("/queues/messagesDynamic")
//public class MessagesServlet extends HttpServlet {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -8669485266566590384L;
//
//	@Autowired
//	private ServletsHelper servletsHelper;
//
//	@Autowired
//	private RestOperations springRestClient;
//	
//	@Override
//	public void init(ServletConfig config) throws ServletException{
//		super.init(config);
//		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
//				config.getServletContext());
//	}
//	
//	@Override
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		
//		String queueName = request.getParameter("queue");
//		String type = request.getParameter("type");
//		String page = request.getParameter("page");
//		String pageSize = request.getParameter("pageSize");
//		String[] groupFieldsNames = request
//				.getParameterValues("groupFieldsNames[]");
//		String[] groupFieldsValues = request
//				.getParameterValues("groupFieldsValues[]");
//		String sortField = request.getParameter("sortField");
//		String sortOrder = request.getParameter("sortOrder");
//		String trnSearch = request.getParameter("trnSearch");
//		
//		Integer amountSearch;
//		if(request.getParameter("amountSearch") != null)
//			amountSearch = Integer.parseInt(request.getParameter("amountSearch"));
//		else 
//			amountSearch = 0;
//		
//		
//		boolean isTotalRequested = Boolean.parseBoolean(request
//				.getParameter("isTotalRequested"));
//
//		final Client client = servletsHelper.getAPIClient();
//		JSONObject responseData = new JSONObject();
//		try {
//			responseData = getTableInfo(client, queueName, type, page,
//					pageSize, sortField, sortOrder, isTotalRequested, groupFieldsNames, groupFieldsValues, trnSearch, amountSearch);
//
//			response.getWriter().println(responseData.toString());
//			response.getWriter().flush();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public JSONObject getTableInfo(Client client, String queueName,
//			String type, String page, String pageSize, String sortField, String sortOrder,
//			boolean isTotalRequested, String[] groupFieldsNames,
//			String[] groupFieldsValues, String trnSearch, Integer amountSearch) throws ClientHandlerException,
//			UniformInterfaceException, JSONException {
//		URI uri = UriBuilder
//				.fromPath(servletsHelper.getUrl())
//				.path("queues").path("by-name").path(queueName).path("messages")
//				.queryParam("messageType", type).build();
//		if (isTotalRequested) {
//			uri = UriBuilder.fromUri(uri).queryParam("total", "").build();
//		}
//		if (page != null) {
//			uri = UriBuilder.fromUri(uri).queryParam("page", page).build();
//		}
//		if (pageSize != null) {
//			uri = UriBuilder.fromUri(uri).queryParam("page_size", pageSize)
//					.build();
//		}
//		if(sortField != null && !sortField.equals("")){
//			uri = UriBuilder.fromUri(uri).queryParam("sort_field", sortField)
//					.build();
//		}
//		if(sortOrder != null && !sortOrder.equals("")){
//			uri = UriBuilder.fromUri(uri).queryParam("sort_order", sortOrder)
//					.build();
//		}
//		if (groupFieldsNames != null) {
//			for (int i = 0; i < groupFieldsValues.length; i++) {
//				uri = UriBuilder
//						.fromUri(uri)
//						.queryParam("filter_" + groupFieldsNames[i],
//								groupFieldsValues[i]).build();
//			}
//		}
//		
//		if (trnSearch != null) {
//			uri = UriBuilder.fromUri(uri).queryParam("filter_trn_cntor", trnSearch)
//					.build();
//		}
//		else {
//			uri = UriBuilder.fromUri(uri).queryParam("filter_trn_cntor", null)
//					.build();
//		}
//		if (amountSearch != -1) {
//			uri = UriBuilder.fromUri(uri).queryParam("filter_amount_exctor", amountSearch)
//					.build();
//		}
//		
////		WebResource webResource = client.resource(uri.toString());
////		
////		
////		ClientResponse response = webResource
////				.accept(MediaType.APPLICATION_JSON)
////				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
////		
////		JSONObject responseEntity = response.getEntity(JSONObject.class);
////		return responseEntity;
//		
//		return springRestClient.getForEntity(uri.toString(), JSONObject.class).getBody();
//	}
//
//}
