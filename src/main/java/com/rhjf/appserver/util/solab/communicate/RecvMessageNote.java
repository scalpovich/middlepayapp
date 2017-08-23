package com.rhjf.appserver.util.solab.communicate;

import java.util.Date;

import com.rhjf.appserver.util.solab.iso8583.IsoMessage;

public class RecvMessageNote {
	
	String tran_procode;
	String szTranType;//交易类型
	String szMerchantID;//商户号
	String szTermID;//终端号
	String szSerialNum;//流水号
	String szBatchNum;
	Date timeStmp; // 标记对象入队列的时间，防止僵尸对象过多，导致内存泄露
	IsoMessage szMessageData;//报文
	
	public RecvMessageNote(String pTranType, String field3, String pMerchantID, String pTermID,String batchNum ,String pSerialNum, IsoMessage pMessageData, Date timeStmp)
	{
		this.szTranType = pTranType;
		this.szMerchantID = pMerchantID;
		this.szTermID = pTermID;
		this.szSerialNum = pSerialNum;
		this.szMessageData = pMessageData;
		this.tran_procode = field3;
		this.szBatchNum = batchNum;
		this.timeStmp = timeStmp;
	}
}

