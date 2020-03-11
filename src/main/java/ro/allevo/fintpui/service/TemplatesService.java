package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplatesRestApiDao;
import ro.allevo.fintpui.model.Template;
import ro.allevo.fintpui.utils.PagedCollection;
@Service
public class TemplatesService {
	
	@Autowired
	private TemplatesRestApiDao templatesDao;

	public Template[] getAllTemplates(){
		return templatesDao.getAllTemplates();
	}
	
	public Template[] getAllTemplates(LinkedHashMap<String, List<String>> param){
		return templatesDao.getAllTemplates(param);
	}
	
	public PagedCollection<Template> getPage(){
		return templatesDao.getPage();
	}

	public Template getTemplate(Integer id){
		return templatesDao.getTemplate(id);
	}

	public ResponseEntity<String> insertTemplate(Template template){
		return templatesDao.insertTemplate(template);
	}

	public void updateTemplate(Template template, Integer id){
		templatesDao.updateTemplate(template, id);
	}

	public void deleteTemplate(Integer id){
		templatesDao.deleteTemplate(id);
	}

	public ArrayList<String> getAllTemplatesNames() {
		Template[] externalEntities = getAllTemplates();
		ArrayList<String> result = new ArrayList<>();
 		for(Template template : externalEntities){
			result.add(template.getName());
		}
		return result;
	}
	
}
