package ro.allevo.fintpui.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.TemplateConfigOptions;

@Service
public interface TemplateConfigOptionsDao {

	public TemplateConfigOptions[] getAllTemplateConfingOptions();

	public Map<Integer, List<String>> getAllConfingOptionsValues();
}
