package ro.allevo.fintpui.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelper {

	public static String toString(Object value) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(value);
	}
	
}
