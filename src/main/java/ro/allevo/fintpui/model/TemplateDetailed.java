package ro.allevo.fintpui.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the txtemplatesdetailed database table.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateDetailed implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String value;

	private Template txtemplate;

	private TemplateConfigDetailed txtemplatesconfigdetailed;
	
	public TemplateDetailed() {
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Template getTxtemplate() {
		return txtemplate;
	}

	public void setTxtemplate(Template txtemplate) {
		this.txtemplate = txtemplate;
	}

	public TemplateConfigDetailed getTxtemplatesconfigdetailed() {
		return txtemplatesconfigdetailed;
	}

	public void setTxtemplatesconfigdetailed(TemplateConfigDetailed txtemplatesconfigdetailed) {
		this.txtemplatesconfigdetailed = txtemplatesconfigdetailed;
	}
	
//	@Override
//	public String toString() {
//		return "TemplateDetailed [id=" + id + ", value=" + value + ", txtemplate=" + txtemplate + "]";
//				//+ ", txtemplatesconfigdetailed=" + txtemplatesconfigdetailed 
//	}

	
}