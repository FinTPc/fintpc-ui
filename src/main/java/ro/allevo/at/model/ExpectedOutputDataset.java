package ro.allevo.at.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpectedOutputDataset {
	
	private Integer id;
	private String dataset;
	private InputDataset inputdataset;
	private InterfaceConfig interfaceconfig;
	
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
	public InputDataset getInputdataset() {
		return inputdataset;
	}
	public void setInputdataset(InputDataset inputdataset) {
		this.inputdataset = inputdataset;
	}
	public InterfaceConfig getInterfaceconfig() {
		return interfaceconfig;
	}
	public void setInterfaceconfig(InterfaceConfig interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
	}
}
