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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ro.allevo.fintpui.service.UserService;
import ro.allevo.fintpui.utils.ApplicationCacheManager;
//import ro.allevo.fintpui.utils.UserCacheManager;
import ro.allevo.fintpui.utils.RestClient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@WebServlet("/batchRequest")
@Deprecated
public class BatchRequestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7109942102128193051L;

	private static Logger logger = LogManager
			.getLogger(BatchRequestServlet.class.getName());

	@Autowired
	private ServletsHelper servletsHelper;

	@Autowired
	private UserService userservice;

	int userid;

	private static String DATA_CHANGED_ERROR_MESSAGE = "The messages on your screen are not up to date."
			+ " Please reload page, then perform the desired actions.";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		boolean isUserFilterEnabled = new Boolean(request
				.getParameter("userFilter"));
		String groupKey = request.getParameter("groupkey");

		// build url depending on arguments received
		// if groupkey is specified return details about that batch request
		// else, get all groupkeys (verifying whether the filtering by user is
		// enabled
		
		URI uri = UriBuilder.fromPath(servletsHelper.getUrl())
				.path("batchrequests").build();
 
		if (groupKey != null) {
			
			uri = UriBuilder.fromUri(uri).path(groupKey).build();
		} else {
			if (isUserFilterEnabled) {
				
				String username = ((UserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal())
						.getUsername();
				uri = UriBuilder.fromUri(uri).queryParam("user", username)
						.build();
			}
		}

		RestTemplate client = new RestClient();
		String apiResponse = client.getForObject(uri, String.class);

		response.setContentType("application/json");
		response.getWriter().println(apiResponse);
		response.getWriter().flush();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		logger.info("POST  /batchRequest servlet");
		JSONObject requestEntity = new JSONObject();
		String[] fields = request.getParameterValues("fields[]");
		try {
			String username = new String(
					((UserDetails) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal()).getUsername());
			requestEntity.put("groupkey", request.getParameter("groupkey"));
			requestEntity.put("queuename", request.getParameter("queuename"));
			requestEntity.put("msgtype", request.getParameter("msgtype"));
			requestEntity.put("timekey", request.getParameter("timekey"));
			requestEntity.put("username", username);
			for (int i = 0; i < fields.length; i++) {
				requestEntity.put("field" + (i + 1) + "val", fields[i]);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		final Client client = servletsHelper.getAPIClient();
		URI uri = UriBuilder.fromPath(servletsHelper.getUrl())
				.path("batchrequests").build();
		WebResource webResource = client.resource(uri.toString());
		ClientResponse clientResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestEntity);
		JSONObject responseEntity = clientResponse.getEntity(JSONObject.class);

		response.setContentType("application/json");

		switch (clientResponse.getStatus()) {
		case 202:
			response.setStatus(202);
			response.getWriter().println(responseEntity.toString());
			response.getWriter().flush();
			break;
		case 406:
			response.setStatus(406);
			// create custon answer
			responseEntity = new JSONObject();
			try {
				responseEntity.put("message", DATA_CHANGED_ERROR_MESSAGE);
				response.getWriter().println(responseEntity.toString());
				response.getWriter().flush();

			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case 403:
			response.setStatus(403);
			response.getWriter().println(responseEntity.toString());
			response.getWriter().flush();
			break;
		default:
			throw new RuntimeException("Failed : HTTP error code : "
					+ clientResponse.getStatus()
					+ " => handle this type of response");
		}
	}

	private String getUsername(int userId, Client client) throws JSONException {
		Cache cache = CacheManager.getInstance().getCache(
				ApplicationCacheManager.USERS);
		Element element = cache.get(userId);
		// if value in cache, return it
		if (element != null) {
			return (String) element.getObjectValue();
		}
		String userName = userservice.getUserById(userId).getUsername();
		// else, if info is useful (known user) update cache
		if (userName != null) {
			String value = "" + userName;
			cache.put(new Element(userId, value));
			return value;
		}

		return "-";
	}
}
