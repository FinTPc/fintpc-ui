package ro.allevo.fintpui.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Template {
	private Integer id;
	
	private String entity;

	private String name;

	private Integer userid;

	private TemplateConfig txtemplatesconfig;
	
	private Integer type;
	
	private TemplateDetailed[] txtemplatesdetaileds;
	
	private TemplateGroup[] txtemplatesgroups;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
		
	public TemplateGroup[] getTxtemplatesgroups() {
		return txtemplatesgroups;
	}
	public void setTxtemplatesgroups(TemplateGroup[] txtemplatesgroups) {
		this.txtemplatesgroups = txtemplatesgroups;
	}
	public TemplateDetailed[] getTxtemplatesdetaileds() {
		return txtemplatesdetaileds;
	}
	public void setTxtemplatesdetaileds(TemplateDetailed[] txtemplatesdetaileds) {
		this.txtemplatesdetaileds = txtemplatesdetaileds;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
	public TemplateConfig getTxtemplatesconfig() {
		return txtemplatesconfig;
	}
	public void setTxtemplatesconfig(TemplateConfig txtemplatesconfig) {
		this.txtemplatesconfig = txtemplatesconfig;
	}
	@JsonGetter("rowid")
	public Integer getRowid() {
		return id;
	}
//	@Override
//	public String toString() {
//		return "Template [id=" + id + ", entity=" + entity + ", name=" + name + ", userid="
//				+ userid /*
//							 * + ", configid=" + txtemplatesconfig.getId()
//							 */ 
//				+ ", type=" + type + ", txtemplatesdetaileds=" + txtemplatesdetaileds
//				+ "]";
//	}
	
}
