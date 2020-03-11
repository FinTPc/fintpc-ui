package ro.allevo.at.dao;

import org.springframework.stereotype.Service;

import ro.allevo.at.model.InputDataset;

@Service
public interface InputDatasetDao {
	
	public InputDataset[] getAllInputDatasets();
	public InputDataset getInputDataset(Long id);
	public InputDataset[] getAllInputDatasetsById(Long id);

}
