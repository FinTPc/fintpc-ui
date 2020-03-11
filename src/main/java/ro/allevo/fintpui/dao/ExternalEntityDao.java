package ro.allevo.fintpui.dao;


import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.ExternalEntity;

@Service
public interface ExternalEntityDao {

	public ExternalEntity[] getAllExternalEntities();
	public ExternalEntity getExternalEntity(String id);
	public void insertExternalEntity(ExternalEntity externalEntity);
	public void updateExternalEntity(ExternalEntity externalEntity, String id);
	public void deleteExternalEntity(String id);
	}
