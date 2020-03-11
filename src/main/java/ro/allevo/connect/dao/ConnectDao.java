package ro.allevo.connect.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ro.allevo.connect.model.Connect;
@Service
public interface ConnectDao {

	public Connect[] getAllConnects();
	public Connect getConnect(Long id);
	public void insertConnect(Connect connect);
	public ResponseEntity<String> updateConnect(Connect connect);
	public void deleteConnect(Long id);
}
