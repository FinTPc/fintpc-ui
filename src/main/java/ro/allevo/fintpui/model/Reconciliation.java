package ro.allevo.fintpui.model;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reconciliation {
	
	@NotNull
	@Size(max = 7)
	private String matchType;  
	
	@Size(max = 35)
	private String stmtStatementreference;

	@Size(max = 140)
	private String stmtName;
	

	private Double stmtAmount;
	
	@Size(max = 3)
	private String stmtCurrency;
	
	@Size(max = 35)
	private String stmtAccountnumber;
	
	@Size(max = 500)
	private String stmtRemittanceinfo;
	
	@Size(max = 18)
	private String stmtstatementNumber;
	
	@Size(max = 10)
	private String stmtValuedate;
  
	@Size(max = 30)
	private String stmtCorrelationid;
	
	@Size(max = 35)
	private String pymtMessagetype;
	
	@Size(max = 35)
	private String pymtEndtoendid;

	@Size(max = 70)
	private String pymtDbtcustomername;
	
	
	private Double pymtAmount;
	
	@Size(max = 3)
	private String pymtCurrency;
    
	@Size(max = 35)
	private String pymtDbtaccount;
	
	@Size(max = 140)
	private String pymtRemittanceinfo;
	
	@Size(max = 10)
	private String pymtValuedate;

	@Size(max = 70)
	private String pymtCdtcustomername;
	
	@Size(max = 35)
	private String pymtCdtaccount;
	
	@Size(max = 30)
	private String pymtCorrelationid;
	
	public String getStmtstatementNumber() {
		return stmtstatementNumber;
	}

	public void setStmtstatementNumber(String stmtstatementNumber) {
		this.stmtstatementNumber = stmtstatementNumber;
	}

	public String getMatchType() {
		return matchType;
	}



	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}



	public String getStmtStatementreference() {
		return stmtStatementreference;
	}



	public void setStmtStatementreference(String stmtStatementreference) {
		this.stmtStatementreference = stmtStatementreference;
	}



	public String getStmtName() {
		return stmtName;
	}



	public void setStmtName(String stmtName) {
		this.stmtName = stmtName;
	}



	public Double getStmtAmount() {
		return stmtAmount;
	}



	public void setStmtAmount(Double stmtAmount) {
		this.stmtAmount = stmtAmount;
	}



	public String getStmtCurrency() {
		return stmtCurrency;
	}



	public void setStmtCurrency(String stmtCurrency) {
		this.stmtCurrency = stmtCurrency;
	}



	public String getStmtAccountnumber() {
		return stmtAccountnumber;
	}



	public void setStmtAccountnumber(String stmtAccountnumber) {
		this.stmtAccountnumber = stmtAccountnumber;
	}



	public String getStmtRemittanceinfo() {
		return stmtRemittanceinfo;
	}



	public void setStmtRemittanceinfo(String stmtRemittanceinfo) {
		this.stmtRemittanceinfo = stmtRemittanceinfo;
	}



	public String getStmtValuedate() {
		return stmtValuedate;
	}



	public void setStmtValuedate(String stmtValuedate) {
		this.stmtValuedate = stmtValuedate;
	}



	public String getStmtCorrelationid() {
		return stmtCorrelationid;
	}

	public void setStmtCorrelationid(String stmtCorrelationid) {
		this.stmtCorrelationid = stmtCorrelationid;
	}



	public String getPymtMessagetype() {
		return pymtMessagetype;
	}



	public void setPymtMessagetype(String pymtMessagetype) {
		this.pymtMessagetype = pymtMessagetype;
	}



	public String getPymtEndtoendid() {
		return pymtEndtoendid;
	}



	public void setPymtEndtoendid(String pymtEndtoendid) {
		this.pymtEndtoendid = pymtEndtoendid;
	}



	public String getPymtDbtcustomername() {
		return pymtDbtcustomername;
	}



	public void setPymtDbtcustomername(String pymtDbtcustomername) {
		this.pymtDbtcustomername = pymtDbtcustomername;
	}



	public Double getPymtAmount() {
		return pymtAmount;
	}



	public void setPymtAmount(Double pymtAmount) {
		this.pymtAmount = pymtAmount;
	}



	public String getPymtCurrency() {
		return pymtCurrency;
	}



	public void setPymtCurrency(String pymtCurrency) {
		this.pymtCurrency = pymtCurrency;
	}



	public String getPymtDbtaccount() {
		return pymtDbtaccount;
	}



	public void setPymtDbtaccount(String pymtDbtaccount) {
		this.pymtDbtaccount = pymtDbtaccount;
	}



	public String getPymtRemittanceinfo() {
		return pymtRemittanceinfo;
	}



	public void setPymtRemittanceinfo(String pymtRemittanceinfo) {
		this.pymtRemittanceinfo = pymtRemittanceinfo;
	}



	public String getPymtValuedate() {
		return pymtValuedate;
	}



	public void setPymtValuedate(String pymtValuedate) {
		this.pymtValuedate = pymtValuedate;
	}



	public String getPymtCdtcustomername() {
		return pymtCdtcustomername;
	}



	public void setPymtCdtcustomername(String pymtCdtcustomername) {
		this.pymtCdtcustomername = pymtCdtcustomername;
	}



	public String getPymtCdtaccount() {
		return pymtCdtaccount;
	}



	public void setPymtCdtaccount(String pymtCdtaccount) {
		this.pymtCdtaccount = pymtCdtaccount;
	}



	public String getPymtCorrelationid() {
		return pymtCorrelationid;
	}



	public void setPymtCorrelationid(String pymtCorrelationid) {
		this.pymtCorrelationid = pymtCorrelationid;
	}



	@JsonGetter("rowid")
	public String getRowid() {
		return matchType;
	}


	
}
