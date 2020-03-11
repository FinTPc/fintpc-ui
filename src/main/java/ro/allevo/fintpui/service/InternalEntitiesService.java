package ro.allevo.fintpui.service;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.InternalEntityRestApiDao;
import ro.allevo.fintpui.model.InternalEntity;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class InternalEntitiesService {

	@Autowired
	private InternalEntityRestApiDao internalEntityDao;
	
	public InternalEntity[] getAllInternalEntities() {
		return internalEntityDao.getAllInternalEntities();
	}
	
	public PagedCollection<InternalEntity> getPage() {
		return internalEntityDao.getPage();
	}
	
	public InternalEntity getInternalEntity(String id) {
		return internalEntityDao.getInternalEntity(id);
	}

	public void insertInternalEntity(InternalEntity internalEntities) {
		internalEntityDao.insertInternalEntity(internalEntities);
	}

	public void updateInternalEntity(InternalEntity internalEntity, String id) {
		internalEntityDao.updateInternalEntity(internalEntity, id);
	}

	public void deleteInternalEntity(String id) {
		System.out.println("Service " + id);
		internalEntityDao.deleteInternalEntity(id);
	}
	
	public ArrayList<String> getAllInternalEntityNames() {
		InternalEntity[] internalEntities = getAllInternalEntities();
		ArrayList<String> result = new ArrayList<>();
 		for(InternalEntity internalEntity : internalEntities){
			result.add(internalEntity.getName());
		}
		return result;
	}
	
	public String[] getAllInternalEntitiesByRights(String messagetype) {
		return internalEntityDao.getInternalsByRights(messagetype);
	}
}
