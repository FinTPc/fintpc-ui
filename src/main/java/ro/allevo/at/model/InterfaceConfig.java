package ro.allevo.at.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceConfig {
	
	private Integer id;
	private String description;
	private String inputtype;
	private String location;
	private String name;
	private String txtype;
	private List<ExpectedOutputDataset> expectedoutputdatasets;
	private List<InputDataset> inputdatasets;
	private List<TxProcessingTest> txprocessingtests;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInputtype() {
		return inputtype;
	}
	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTxtype() {
		return txtype;
	}
	public void setTxtype(String txtype) {
		this.txtype = txtype;
	}
	public List<ExpectedOutputDataset> getExpectedoutputdatasets() {
		return expectedoutputdatasets;
	}
	public void setExpectedoutputdatasets(List<ExpectedOutputDataset> expectedoutputdatasets) {
		this.expectedoutputdatasets = expectedoutputdatasets;
	}
	public List<InputDataset> getInputdatasets() {
		return inputdatasets;
	}
	public void setInputdatasets(List<InputDataset> inputdatasets) {
		this.inputdatasets = inputdatasets;
	}
	public List<TxProcessingTest> getTxprocessingtests() {
		return txprocessingtests;
	}
	public void setTxprocessingtests(List<TxProcessingTest> txprocessingtests) {
		this.txprocessingtests = txprocessingtests;
	}

}
