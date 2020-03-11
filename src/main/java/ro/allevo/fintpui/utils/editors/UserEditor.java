package ro.allevo.fintpui.utils.editors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.model.User;

public class UserEditor extends PropertyEditorSupport {
	
	@Override
    public void setAsText(String text) throws IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		try {
			node = mapper.readValue(text, JsonNode.class);
			
			User user = new User();
			user.setUsername(node.get("username").asText());
			user.setEmail(node.get("email").asText());
	         
	        setValue(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static class Deserialize extends JsonDeserializer<User> {

		@Override
		public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(p);
			
			User user = new User();
			user.setUsername(node.get("username").asText());
			user.setEmail(node.get("email").asText());
			
			return user;
		}
		
	}
	
}
