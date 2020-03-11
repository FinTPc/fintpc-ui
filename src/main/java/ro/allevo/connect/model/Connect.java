package ro.allevo.connect.model;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Connect {

	private int id;
	@NotNull
	private String clientId;
	@NotNull
	private String clientSecret;
	@NotNull
	private String userId;
	private String userSecret;
	private String accessTokenUri;
	private String userAuthorizationUri;
	private String redirectUri;
	private String userInfoUri;
	private Timestamp expirationDate;
	private String grantType;
	private String authenticationScheme;
	private String clientAuthenticationScheme;
	private String token;
	private String bic;
	private Consent consentEntity;
	private String timeTrigger;


	public Consent getConsentEntity() {
		return consentEntity;
	}

	public void setConsentEntity(Consent consentEntity) {
		this.consentEntity = consentEntity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserSecret() {
		return userSecret;
	}

	public void setUserSecret(String userSecret) {
		this.userSecret = userSecret;
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

	public void setAccessTokenUri(String accessTokenUri) {
		this.accessTokenUri = accessTokenUri;
	}

	public String getUserAuthorizationUri() {
		return userAuthorizationUri;
	}

	public void setUserAuthorizationUri(String userAuthorizationUri) {
		this.userAuthorizationUri = userAuthorizationUri;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getUserInfoUri() {
		return userInfoUri;
	}

	public void setUserInfoUri(String userInfoUri) {
		this.userInfoUri = userInfoUri;
	}

	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getAuthenticationScheme() {
		return authenticationScheme;
	}

	public void setAuthenticationScheme(String authenticationScheme) {
		this.authenticationScheme = authenticationScheme;
	}

	public String getClientAuthenticationScheme() {
		return clientAuthenticationScheme;
	}

	public void setClientAuthenticationScheme(String clientAuthenticationScheme) {
		this.clientAuthenticationScheme = clientAuthenticationScheme;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bankName) {
		this.bic = bankName;
	}

	@JsonGetter("rowid")
	public int getRowid() {
		return id;
	}

	public String getTimeTrigger() {
		return timeTrigger;
	}

	public void setTimeTrigger(String timeTrigger) {
		this.timeTrigger = timeTrigger;
	}

}
