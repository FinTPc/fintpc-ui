package ro.allevo.fintpui.dao;

import org.springframework.http.ResponseEntity;

import ro.allevo.fintpui.model.TemplateGroup;

public interface TemplateGroupDao {
	
	public ResponseEntity<String> insertTemplateGroup(TemplateGroup templateGroup);
	public ResponseEntity<String> updateTemplateGroup(TemplateGroup templateGroup);
	//public TemplateGroup[] getTemplatesGroup(Integer templateId);
}
