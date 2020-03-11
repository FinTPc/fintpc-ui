package ro.allevo.fintpui.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplateConfigOptionsRestApiDao;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.model.TemplateConfigOptions;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateConfigOptionsService {
	
	@Autowired
	private TemplateConfigOptionsRestApiDao templateConfigOptionsDao;
	
//	@Autowired
//	private TemplateSmallConfigRestApiDao templateSmallConfigDao;
	
	public TemplateConfigOptions[] getAllTemplateConfig() {
		return templateConfigOptionsDao.getAllTemplateConfingOptions();
	}
	
	public Map<Integer, List<String>> getAllConfingOptionsValues(){
		return templateConfigOptionsDao.getAllConfingOptionsValues();
	}
		
//	public void insertTemplateConfig(TemplateConfig templateConfig) {
//		templateConfigDao.insertTemplateConsfing(templateConfig);
//	}
//	
//	public void updateTemplateConfig(TemplateConfig templateConfig, Long id) {
//		templateConfigDao.updateTemplateConsfing(templateConfig, id);
//	}
//	
//	public void deleteTemplateConfig(Long id) {
//		templateConfigDao.deleteTemplateConsfing(id);
//	}
}
