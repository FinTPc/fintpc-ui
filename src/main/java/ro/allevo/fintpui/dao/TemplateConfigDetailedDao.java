package ro.allevo.fintpui.dao;

import javax.ws.rs.core.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.TemplateConfigDetailed;

@Service
public interface TemplateConfigDetailedDao {
	
	public TemplateConfigDetailed[] getAllTemplateConfigDetailed(Integer configId);
	public TemplateConfigDetailed getTemplateConfigDetailedById(Integer configId, Long fieldId);
	public ResponseEntity<String> insertTemplatesConfigDetailed(TemplateConfigDetailed templateConfigDetailed);
	public Response deleteTemplatesConfigDetailedsByConfigId(Integer configId);
	public Response deleteTemplatesConfigDetailedById(Integer id);
}
