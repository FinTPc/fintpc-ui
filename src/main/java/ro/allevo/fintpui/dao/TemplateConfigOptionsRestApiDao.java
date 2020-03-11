package ro.allevo.fintpui.dao;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateConfigOptionsRestApiDao extends RestApiDao<TemplateConfigOptions> implements TemplateConfigOptionsDao{

	@Autowired
	Config config;

	public TemplateConfigOptionsRestApiDao() {
		super(TemplateConfigOptions.class);
	}

	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAPIUrl()).path("templates-options").build();
	}

	@Override
	public TemplateConfigOptions[] getAllTemplateConfingOptions() {
		return getAll();
	}
	
	@Override
	public Map<Integer, List<String>> getAllConfingOptionsValues() {
		
		TemplateConfigOptions[] templatesOptions = getAll();
		
		Map<Integer, List<String>> optionsValues = new HashMap<Integer, List<String>>();
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter stringWriter = new StringWriter();
		JSONArray arrayLists = null;
		List<String> result = null;
		PagedCollection<?> pc = null;
		URI uri = null;
		try {
			for(TemplateConfigOptions templateOption:templatesOptions) {
				String[] option = templateOption.getDatasource().split("\\.");
				if(option.length == 2) {
					uri =  UriBuilder.fromUri(config.getAPIUrl()).path(option[0]).build();
					pc = getObject(uri, PagedCollection.class);
					objectMapper = new ObjectMapper();
					stringWriter = new StringWriter();
					objectMapper.writeValue(stringWriter, pc.getItems() );
					arrayLists = new JSONArray(stringWriter.toString());
					result = new LinkedList<String>();
					for(int i=0;i<arrayLists.length();i++) {
						String val = arrayLists.getJSONObject(i).optString(option[1]);
						if(val.length()>0)
							result.add(val);
					}
					optionsValues.put(templateOption.getId(), result);
				}
				
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return optionsValues;
	}
	
	
	
}
