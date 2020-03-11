package ro.allevo.at.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InputDataset {

	private Integer id;
	private String dataset;
	private String datasettype;
	private List<ExpectedOutputDataset> expectedoutputdatasets;
	private InterfaceConfig interfaceconfig;
	private List<TxProcessingTestLog> txprocessingtestlogs;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public String getDatasettype() {
		return datasettype;
	}
	public void setDatasettype(String datasettype) {
		this.datasettype = datasettype;
	}
	public List<ExpectedOutputDataset> getExpectedoutputdatasets() {
		return expectedoutputdatasets;
	}
	public void setExpectedoutputdatasets(List<ExpectedOutputDataset> expectedoutputdatasets) {
		this.expectedoutputdatasets = expectedoutputdatasets;
	}
	public InterfaceConfig getInterfaceconfig() {
		return interfaceconfig;
	}
	public void setInterfaceconfig(InterfaceConfig interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
	}
	public List<TxProcessingTestLog> getTxprocessingtestlogs() {
		return txprocessingtestlogs;
	}
	public void setTxprocessingtestlogs(List<TxProcessingTestLog> txprocessingtestlogs) {
		this.txprocessingtestlogs = txprocessingtestlogs;
	}
	
}
