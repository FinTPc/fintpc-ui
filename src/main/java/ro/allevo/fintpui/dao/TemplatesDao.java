package ro.allevo.fintpui.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.Template;
@Service
public interface TemplatesDao {
	
	public Template[] getAllTemplates();
	public Template[] getAllTemplates(LinkedHashMap<String, List<String>> params);
	public Template getTemplate(Integer id);
	public ResponseEntity<String> insertTemplate(Template template);
	public void updateTemplate(Template template, Integer id);
	public void deleteTemplate(Integer id);
	
}
