package com.rhjf.appserver.enums;

public enum TradeCode {

	
	WXScanCode("WXScanCode" , "1"),
	AliScanCode("AliScanCode","2");

	
	private String name;
	private String index;
	

	// 构造方法
    private TradeCode(String name , String index){
		this.name = name;
		this.index = index;
	}
	
    // 普通方法
    public static String getName(String index) {
        for (TradeCode tradeCode : TradeCode.values()) {
            if (tradeCode.getIndex() == index) {
                return tradeCode.name;
            }
        }
        return null;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
}
