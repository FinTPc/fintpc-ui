package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.TemplateConfig;

@Service
public interface TemplateConfigDao {

	public TemplateConfig[] getAllTemplateConfings();
	public TemplateConfig getTemplateConfigWithXsd(Integer id);
	public TemplateConfig getTemplateConfig(Integer id);
//	public void insertTemplateConsfing(TemplateConfig templateConfig);
//	public void updateTemplateConsfing(TemplateConfig templateConfig, Long id);
//	public void deleteTemplateConsfing(Long id);
}
