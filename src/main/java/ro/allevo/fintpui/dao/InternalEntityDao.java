package ro.allevo.fintpui.dao;


import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.InternalEntity;

@Service
public interface InternalEntityDao {

	public InternalEntity[] getAllInternalEntities();
	public InternalEntity getInternalEntity(String id);
	public void insertInternalEntity(InternalEntity internalEntity);
	public void updateInternalEntity(InternalEntity internalEntity, String id);
	public void deleteInternalEntity(String id);
	public String[] getInternalsByRights(String messagetype);
}
