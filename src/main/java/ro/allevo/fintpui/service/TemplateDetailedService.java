package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplateDetailedRestApiDao;
import ro.allevo.fintpui.model.TemplateDetailed;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateDetailedService {
	
	@Autowired
	private TemplateDetailedRestApiDao templateDetailedDao;
	
	public TemplateDetailed[] getAllTemplateDetailed(Integer id) {
		return templateDetailedDao.getAllTemplateDetailed(id);
	}
	
	public PagedCollection<TemplateDetailed> getPage() {
		return templateDetailedDao.getPage();
	}

	public TemplateDetailed getTemplateDetailed(Integer id) {
		return templateDetailedDao.getTemplateDetailed(id);
	}

	public ResponseEntity<String> insertTemplateDetailed(TemplateDetailed templateDetailed) {
		return templateDetailedDao.insertTemplateDetailed(templateDetailed);
	}

	public ResponseEntity<String> updateTemplateDetailed(TemplateDetailed templateDetailed) {
		return templateDetailedDao.updateTemplateDetailed(templateDetailed);
	}

	public ResponseEntity<String> deleteTemplateDetailedByTemplateId(Integer templateId) {
		return templateDetailedDao.deleteTemplateDetailedByTemplateId(templateId);
	}
	

}
