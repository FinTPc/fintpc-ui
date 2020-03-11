package ro.allevo.connect.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.connect.dao.ConnectRestApiDao;
import ro.allevo.connect.model.Connect;
import ro.allevo.fintpui.utils.PagedCollection;

@Service
public class ConnectService {

	@Autowired
	private ConnectRestApiDao connectDao;
	
	public Connect[] getAllConnect() {
		return connectDao.getAllConnects();
	}
	
	public PagedCollection<Connect> getPage(){
		return connectDao.getPage();
	}
	
	public PagedCollection<Connect> getPage(LinkedHashMap<String, List<String>> param){
		return connectDao.getPage(param);
	}
	
	public Connect getConnect(Long id) {
		return connectDao.get(String.valueOf(id));
	}
	
	public void insertConnect(Connect connect) {
		connectDao.insertConnect(connect);
	}
	
	public void deleteConnect(Long id) {
		connectDao.delete(String.valueOf(id));
	}
	
	public ResponseEntity<String> updateConnect(Connect connect) {
		return connectDao.updateConnect(connect);
	}
	
	/*
	 * public void getToken(Connect connect, Long id) {
	 * connectDao.newTokenConnect(id); }
	 */
	public String newToken(Long bic, String code, String sha) {
		return connectDao.newTokenConnect(bic, code, sha);	
	}
	
}
