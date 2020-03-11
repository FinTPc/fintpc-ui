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
package ro.allevo.fintpui.config;

import java.io.IOException;

import javax.ws.rs.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import ro.allevo.fintpui.exception.ConflictException;
import ro.allevo.fintpui.exception.NotAuthorizedException;

@Component
public class RestTemplateResponseErrorHandler 
  implements ResponseErrorHandler {
 
    private static final Series CLIENT_ERROR = Series.CLIENT_ERROR;
	private static final Series SERVER_ERROR = Series.SERVER_ERROR;

	@Override
    public boolean hasError(ClientHttpResponse httpResponse) 
      throws IOException {
		return (
          httpResponse.getStatusCode().series() == CLIENT_ERROR 
          || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }
 
    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
    	
    	if (httpResponse.getStatusCode()
          .series() == HttpStatus.Series.SERVER_ERROR) {
            // handle SERVER_ERROR
        }else if (httpResponse.getStatusCode()
          .series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException();
            }
            if (httpResponse.getStatusCode() == HttpStatus.CONFLICT) {
            	//String column = "";
//            	try {
//            		column = new JSONObject().getString("message");
//        		} catch (JSONException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
                throw new ConflictException(httpResponse.getBody());
            }
            if (httpResponse.getStatusCode() == HttpStatus.FORBIDDEN) {
            	throw new NotAuthorizedException();
            }
        }
    }


}