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

package ro.allevo.fintpui.utils.servlets;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@WebServlet("/routeRequest")
public class RouteRequestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4780995753062269904L;

	private static Logger logger = LogManager
			.getLogger(BatchRequestServlet.class.getName());

	@Autowired
	private ServletsHelper servletsHelper;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("POST /routeRequest");
		JSONObject requestEntity = new JSONObject();
		String username = new String(((UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal()).getUsername());
		try {
			requestEntity.put("user", username);
			JSONArray messagesAsJSON = new JSONArray();
			String[] messages = request.getParameterValues("messages[]");
			for (int i = 0; i < messages.length; i++) {
				messagesAsJSON.put(messages[i]);
			}
			requestEntity.put("action", request.getParameter("action"));
			requestEntity.put("messages", messagesAsJSON);
			requestEntity.put("user", username);
			requestEntity.put("source", request.getParameter("source"));
			String destination = request.getParameter("destination");
			if (destination != null) {
				requestEntity.put("destination", destination);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		final Client client = servletsHelper.getAPIClient();
		URI uri = UriBuilder.fromPath(servletsHelper.getUrl())
				.path("routerequests").build();

		WebResource webResource = client.resource(uri.toString());
		ClientResponse clientResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestEntity);
		JSONObject responseEntity = clientResponse.getEntity(JSONObject.class);
		response.setContentType("application/json");
		response.getWriter().println(responseEntity.toString());
		response.getWriter().flush();
	}
}
