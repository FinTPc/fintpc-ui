package ro.allevo.at.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TxProcessingTestLog {
	
	private Integer id;
	private Timestamp insertdate;
	private Integer status;
	private InputDataset inputdataset;
	private TxProcessingTest txprocessingtest;
	private String txtype;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Timestamp getInsertdate() {
		return insertdate;
	}
	public void setInsertdate(Timestamp insertdate) {
		this.insertdate = insertdate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public InputDataset getInputdataset() {
		return inputdataset;
	}
	public void setInputdataset(InputDataset inputdataset) {
		this.inputdataset = inputdataset;
	}
	public TxProcessingTest getTxprocessingtest() {
		return txprocessingtest;
	}
	public void setTxprocessingtest(TxProcessingTest txprocessingtest) {
		this.txprocessingtest = txprocessingtest;
	}
	public String getTxtype() {
		return txtype;
	}
	public void setTxtype(String txtype) {
		this.txtype = txtype;
	}
}
