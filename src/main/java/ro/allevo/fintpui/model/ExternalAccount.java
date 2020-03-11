package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAccount {
	
	@NotNull
    @Size(min=1,max=35)
	private String entityName;
	@NotNull
	@Size(min = 3, max = 3,message = "size must be 3")
	private String currency;
	@NotNull
	@Size(min=8,max=35)
	private String accountNumber;
	@NotNull
	@Size(min = 8, max = 8,message = "size must be 8")
	private String bic;
	@Size(max=70)
	private String description;
	@Size(max=1)
	private String locked;
	@Size(max=1)	
	private String defaultAccount;
	@Size(max=70)	
	private String otherDetails;
	
	@NotNull
	private long id;
	
	@JsonGetter("defaultAccount")
	public String getDefaultAccount() {
		return defaultAccount;
	}
	@JsonSetter("defaultAccount")
	public void setDefaultAccount(String defAccount) {
		this.defaultAccount = defAccount;
	}
	@JsonGetter("entityName")
	public String getEntityName() {
		return entityName;
	}
	@JsonSetter("entityName")
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}
	public long getId() {
		return id;
	}
	@JsonGetter("rowid")
	public long getRowid() {
		return id;
	}
	
	
}
