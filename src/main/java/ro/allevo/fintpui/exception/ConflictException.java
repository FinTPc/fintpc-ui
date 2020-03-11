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

package ro.allevo.fintpui.exception;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException{

	private static final long serialVersionUID = 2735806673061983386L;
	
	private ObjectNode body;
	
	public ConflictException(InputStream stream) throws IOException {
		super();
		this.body = new ObjectMapper().readValue(stream, ObjectNode.class);
	}
	
	@Override
	public String getMessage() {
		return body.get("message").asText();
	}
	
	public String getReason() {
		JsonNode reason = body.get("reason");
		if (null != reason)
			return reason.asText();
		
		return null;
	}
	
	public JsonNode getExtraInfo() {
		return body.get("extraInfo");
	}
}
