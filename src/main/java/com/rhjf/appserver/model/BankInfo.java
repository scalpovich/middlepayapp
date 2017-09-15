package com.rhjf.appserver.model;

public class BankInfo {
	private int netCode;
	private String netCodeRemarks;
	private String platMerchantID;
	private String platTermNo;
	public int getNetCode() {
		return netCode;
	}
	public void setNetCode(int netCode) {
		this.netCode = netCode;
	}
	
	public String getNetCodeRemarks() {
		return netCodeRemarks;
	}
	public void setNetCodeRemarks(String netCodeRemarks) {
		this.netCodeRemarks = netCodeRemarks;
	}
	public String getPlatMerchantID() {
		return platMerchantID;
	}
	public void setPlatMerchantID(String platMerchantID) {
		this.platMerchantID = platMerchantID;
	}
	public String getPlatTermNo() {
		return platTermNo;
	}
	public void setPlatTermNo(String platTermNo) {
		this.platTermNo = platTermNo;
	}
	
	
}
