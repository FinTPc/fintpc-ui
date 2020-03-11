package ro.allevo.fintpui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.TemplateConfigRestApiDao;
import ro.allevo.fintpui.model.TemplateConfig;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class TemplateConfigService {
	
	@Autowired
	private TemplateConfigRestApiDao templateConfigDao;
	
//	@Autowired
//	private TemplateSmallConfigRestApiDao templateSmallConfigDao;
	
	public TemplateConfig[] getAllTemplateConfig() {
		return templateConfigDao.getAllTemplateConfings();
	}
	
	public PagedCollection<TemplateConfig> getPage() {
		return templateConfigDao.getPage();
	}

	public TemplateConfig getTemplateConfigWithXsd(Integer id) {
		return templateConfigDao.getTemplateConfigWithXsd(id);
	}
	
	public TemplateConfig getTemplateConfig(Integer id) {
		return templateConfigDao.getTemplateConfig(id);
	}

	public void updateTemplateConfig(TemplateConfig templateConfig) {
		templateConfigDao.updateTemplateConfig(templateConfig);
		
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
