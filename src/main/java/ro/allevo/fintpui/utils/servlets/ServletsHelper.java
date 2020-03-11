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

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@Deprecated
public class ServletsHelper {
	
	private String url;
	
	private String uiUrl;
	
	@SuppressWarnings("unused")
	@Deprecated
	public Client getAPIClient(){
		final ClientConfig cc = new DefaultClientConfig();
		final Client client = Client.create(cc);
		
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		client.addFilter(new HTTPBasicAuthFilter("admin","admin"));
		return client;
	}

	//mutat pe Config
	public String getUrl() {
		
		return url;
		
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Deprecated
	public ClientResponse getAPIResource(URI uri) {
		final Client client = getAPIClient();
		WebResource webResource = client.resource(uri);
		
		ClientResponse clientResponse = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		return clientResponse;
	}

	@Deprecated
	public JSONObject postAPIResource(URI uri, Object requestObject){
		try {
			ObjectMapper mapper = new  ObjectMapper();
			//mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
			Object requestEntity = null;
			if (requestObject.getClass().isArray())
				requestEntity = new JSONArray(mapper.writeValueAsString(requestObject));
			else
				requestEntity = new JSONObject(mapper.writeValueAsString(requestObject));
			
			
			final Client client = getAPIClient();
			WebResource webResource = client.resource(uri);
			ClientResponse clientResponse = 
					webResource.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestEntity);
			JSONObject responseEntity = clientResponse.getEntity(JSONObject.class);
			return responseEntity;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated
	public JSONObject putAPIResource(URI uri, Object requestObject){
		try {
			ObjectMapper mapper = new  ObjectMapper();
			//mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
			JSONObject requestEntity = new JSONObject(mapper.writeValueAsString(requestObject));
			final Client client = getAPIClient();
			WebResource webResource = client.resource(uri);
			ClientResponse clientResponse = 
					webResource.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.put(ClientResponse.class, requestEntity);
			JSONObject responseEntity = clientResponse.getEntity(JSONObject.class);
			return responseEntity;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated
	public JSONObject deleteAPIResource(URI uri){
		final Client client = getAPIClient();
		WebResource webResource = client.resource(uri);
		ClientResponse clientResponse = 
				webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		JSONObject responseEntity = clientResponse.getEntity(JSONObject.class);
		return responseEntity;
	}

	public String getUiUrl() {
		return uiUrl;
	}

	public void setUiUrl(String uiUrl) {
		this.uiUrl = uiUrl;
	}
	
	
}
