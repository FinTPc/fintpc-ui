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

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.exception.ConflictException;
import ro.allevo.fintpui.exception.NotAuthorizedException;
import ro.allevo.fintpui.utils.JSONHelper;

@ControllerAdvice
public class GlobalExceptionHandlerController {
	
	
	@Autowired
	Config config;
	
	
	private static Logger logger = LogManager.getLogger(GlobalExceptionHandlerController.class
			.getName());

	
	public static final String DEFAULT_ERROR_VIEW = "errorPage";
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ResourceAccessException.class)
	public ModelAndView handleAccessException(ResourceAccessException exception){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", config.getMessage("general.error"));
		modelAndView.setViewName(DEFAULT_ERROR_VIEW);
		return modelAndView;
	}
	
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(NotAuthorizedException.class)
	public ModelAndView handleNotAuthorized(NotAuthorizedException exception){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", config.getMessage("error.forbidden"));
		modelAndView.setViewName(DEFAULT_ERROR_VIEW);
		return modelAndView;
	}
	
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflict(final ConflictException exception) throws JsonProcessingException{
		return new ResponseEntity<String>(
				JSONHelper.toString(
					new Serializable() {
						public String getMessage() {
							return exception.getMessage();
						}
						
						public String getReason() {
							return exception.getReason();
						}
						
						public JsonNode getExtraInfo() {
							JsonNode extraInfo = exception.getExtraInfo();
							
							if (null != extraInfo && extraInfo.isObject()) {
								JsonNode table = extraInfo.get("table");
							
								//try and resolve table to ui screen
								if (null != table) {
									try {
										String screen = config.getMessage("screen." + table.asText());
										
										if (null != screen)
											((ObjectNode) extraInfo).put("screen", screen);
									}
									catch(Exception ex) {
										ex.printStackTrace();
									}
								}
								
								
							}
							
							return extraInfo;
						}
					}), 
				HttpStatus.CONFLICT);
	}
	
	
}
