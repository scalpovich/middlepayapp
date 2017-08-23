package com.rhjf.appserver.model;

public class BankKey {
	private String macKey;
	private String pinKey;
	private String tdKey;
	private String tmKey;
	private String batchNo;
	private int keyIndex;
	private String channelNo;
	
	
	public String getMacKey() {
		return macKey;
	}
	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}
	public String getPinKey() {
		return pinKey;
	}
	public void setPinKey(String pinKey) {
		this.pinKey = pinKey;
	}
	public String getTdKey() {
		return tdKey;
	}
	public void setTdKey(String tdKey) {
		this.tdKey = tdKey;
	}
	public String getTmKey() {
		return tmKey;
	}
	public void setTmKey(String tmKey) {
		this.tmKey = tmKey;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public int getKeyIndex() {
		return keyIndex;
	}
	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	
}