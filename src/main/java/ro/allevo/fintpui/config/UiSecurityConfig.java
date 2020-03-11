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

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableOAuth2Sso
@Configuration
public class UiSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http)
        throws Exception {
		http.antMatcher("/**")
        .authorizeRequests()
        //.antMatchers("/login**")
        //.permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/home").deleteCookies("JSESSIONID").deleteCookies("UI2SESSION")
        .invalidateHttpSession(true) 
        .clearAuthentication(true).permitAll();
//		http.antMatcher("/**").authorizeRequests()
//            .antMatchers("/", "/webjars/**", "/login").permitAll()
//            .anyRequest().authenticated()
            
//            .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            .and().formLogin().and()
//            .httpBasic().disable();
    }

    @Bean
    public OAuth2RestOperations restOperations(
    		OAuth2ClientContext context,
    		OAuth2ProtectedResourceDetails resource
        ) {
    	OAuth2RestTemplate restTemplate =  new OAuth2RestTemplate(resource, context);
    	restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());    	
        return restTemplate;
    }
  
}