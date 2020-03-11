package ro.allevo.fintpui.model;

import java.io.Serializable;

public class QueuesCountEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String queueName;
	private long noOfTx;
	private String label;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public long getNoOfTx() {
		return noOfTx;
	}
	public void setNoOfTx(long noOfTx) {
		this.noOfTx = noOfTx;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
