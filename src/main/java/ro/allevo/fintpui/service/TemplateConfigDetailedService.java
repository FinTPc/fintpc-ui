package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplateConfigDetailedRestApiDao;
import ro.allevo.fintpui.model.TemplateConfigDetailed;

@Service
public class TemplateConfigDetailedService {
	
	@Autowired
	TemplateConfigDetailedRestApiDao templateConfigDetailedRestApiDao;
	
	public TemplateConfigDetailed[] getTemplateConfigDetailedByFieldId(Integer configId) {
		return templateConfigDetailedRestApiDao.getAllTemplateConfigDetailed(configId);
	}
	
	public ResponseEntity<String> insertTemplateConfigDetailed(TemplateConfigDetailed templateConfigDetailed) {
		return templateConfigDetailedRestApiDao.insertTemplatesConfigDetailed(templateConfigDetailed);
	}

	public TemplateConfigDetailed[] getAllTemplateConfigDetailed(Integer configId) {
		return templateConfigDetailedRestApiDao.getAllTemplateConfigDetailed(configId);
	}

	public ResponseEntity<String> updateTemplateConfigDetailed(TemplateConfigDetailed templateConfigDetailed) {
		return templateConfigDetailedRestApiDao.updateTempatesConfigDetailed(templateConfigDetailed);
	}

	public void deleteTemplateConfigDetailed(Integer id) {
		templateConfigDetailedRestApiDao.deleteTemplatesConfigDetailedById(id);
		
	}

}
