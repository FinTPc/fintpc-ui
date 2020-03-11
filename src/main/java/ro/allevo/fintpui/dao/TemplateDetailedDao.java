package ro.allevo.fintpui.dao;

import org.springframework.http.ResponseEntity;

import ro.allevo.fintpui.model.TemplateDetailed;

public interface TemplateDetailedDao {

	public TemplateDetailed[] getAllTemplateDetailed(Integer configId);
	public TemplateDetailed getTemplateDetailed(Integer configId);
	public ResponseEntity<String> insertTemplateDetailed(TemplateDetailed templateDetailed);
	public ResponseEntity<String> updateTemplateDetailed(TemplateDetailed templateDetailed);
	public ResponseEntity<String> deleteTemplateDetailedByTemplateId(Integer templateId); 
}
