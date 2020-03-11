package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bank{
	@NotNull
    @Size(min=1,max=35)
	private String name;
	@NotNull
	@Size(min = 8, max = 8,message = "size must be 8")
	private String bic;
	@Size(max=70)
	private String address;
	@NotNull
    @Size(min= 2, max=2, message = "this field must be completed")
	private String country;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@JsonGetter("rowid")
	public String getRowid() {
		return bic;
	}

}
