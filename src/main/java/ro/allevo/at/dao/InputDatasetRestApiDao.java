package ro.allevo.at.dao;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.allevo.at.model.InputDataset;
import ro.allevo.fintpui.config.Config;
import ro.allevo.fintpui.dao.RestApiDao;

@Service
public class InputDatasetRestApiDao extends RestApiDao<InputDataset> implements InputDatasetDao {
	
	@Autowired
	Config config;
	
	public InputDatasetRestApiDao() {
		super(InputDataset.class);
	}
	
	@Override
	public URI getBaseUrl() {
		return UriBuilder.fromUri(config.getAtURL()).path("tests").build();
	}

	@Override
	public InputDataset[] getAllInputDatasets() {
		return getAll();
	}

	// de implementat daca e nevoie get(id) , momentan nu o sa mearga
	@Override
	public InputDataset getInputDataset(Long id) {
		return get(String.valueOf(id));
	}

	@Override
	public InputDataset[] getAllInputDatasetsById(Long id) {
		URI uri = UriBuilder.fromUri(config.getAtURL()).path("tests").path("" + id).path("datasetinput").build();
		return getAll(uri);
	}

}
