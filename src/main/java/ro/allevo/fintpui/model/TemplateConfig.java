package ro.allevo.fintpui.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateConfig {

	@NotNull
	private Long id;
	private String messagetype;
	private String validationxsd;
	private String type;
	private TemplateConfigDetailed[] txtemplatesconfigdetaileds;
	private Template[] txtemplates;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessagetype() {
		return messagetype;
	}
	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}
	public String getValidationxsd() {
		return validationxsd;
	}
	public void setValidationxsd(String validationxsd) {
		this.validationxsd = validationxsd;
	}
	@JsonGetter("rowid")
	public Long getRowid() {
		return id;
	}
	public TemplateConfigDetailed[] getTxtemplatesconfigdetaileds() {
		return txtemplatesconfigdetaileds;
	}
	public void setTxtemplatesconfigdetaileds(TemplateConfigDetailed[] txtemplatesconfigdetaileds) {
		this.txtemplatesconfigdetaileds = txtemplatesconfigdetaileds;
	}
	public Template[] getTxtemplates() {
		return txtemplates;
	}
	public void setTxtemplates(Template[] txtemplates) {
		this.txtemplates = txtemplates;
	}
	
}
