package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplateGroupRestApiDao;
import ro.allevo.fintpui.model.TemplateGroup;

@Service
public class TemplateGroupService {

	@Autowired
	private TemplateGroupRestApiDao templateGroupDao;
	
	public TemplateGroup[] getAllTemplateGroups(Integer templateId) {
		return templateGroupDao.getAllTemplateGroups(templateId);
	}
	
//	public TemplateGroup getTemplateGroup(Integer id) {
//		return templateGroupDao.getTemplateGroup(id);
//	}
	
	public ResponseEntity<String> insertTemplateGroup(TemplateGroup templateGroup){
		return templateGroupDao.insertTemplateGroup(templateGroup);
	}

	public ResponseEntity<String> updateTemplateGroup(TemplateGroup templateGroup) {
		return templateGroupDao.updateTemplateGroup(templateGroup);	
	}

	public ResponseEntity<String> deleteTemplateGroup(Integer id) {
		return templateGroupDao.deleteTemplateGroup(id);
	}
}
