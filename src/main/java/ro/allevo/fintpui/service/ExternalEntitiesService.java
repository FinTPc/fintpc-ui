package ro.allevo.fintpui.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.fintpui.dao.ExternalEntityRestApiDao;
import ro.allevo.fintpui.model.ExternalEntity;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ExternalEntitiesService {

	@Autowired
	private ExternalEntityRestApiDao externalEntityDao;
	
	public ExternalEntity[] getAllExternalEntities() {
		return externalEntityDao.getAllExternalEntities();
	}
	
	public PagedCollection<ExternalEntity> getPage() {
		return externalEntityDao.getPage();
	}
	
	public ExternalEntity getExternalEntity(String name) {
		return externalEntityDao.getExternalEntity(name);
	}

	public void insertExternalEntity(ExternalEntity externalEntities) {
		externalEntityDao.insertExternalEntity(externalEntities);
	}

	public void updateExternalEntity(ExternalEntity externalEntity, String name) {
		externalEntityDao.updateExternalEntity(externalEntity, name);
	}

	public void deleteExternalEntity(String name) {
		externalEntityDao.deleteExternalEntity(name);
	}
	
	public ArrayList<String> getAllExternalEntityNames() {
		ExternalEntity[] externalEntities = getAllExternalEntities();
		ArrayList<String> result = new ArrayList<>();
 		for(ExternalEntity externalEntity : externalEntities){
			result.add(externalEntity.getName());
		}
		return result;
	}

}
