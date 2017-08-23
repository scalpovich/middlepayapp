package com.rhjf.appserver.util.solab.communicate;

 import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import com.rhjf.appserver.util.solab.iso8583.IsoMessage;

public class RecvMessageList {
	
	private LinkedList<RecvMessageNote> recvList = new LinkedList<RecvMessageNote>() ;
	private static long lastCkTime = (new Date()).getTime();
	private static RecvMessageList instance = null;
	
	public static RecvMessageList getInstance(){
		if(instance==null){
			instance = new RecvMessageList();
			return instance;
		}else{
			return instance;
		}
	}
	
	public void putMessage(String pTranType, String field3,String pMerchantID, String pTermID, String batchNum,
						   String pSerialNum, IsoMessage pMessageData)
	{
		Date timestmp = new Date();
		RecvMessageNote note = new RecvMessageNote(pTranType,field3, pMerchantID, pTermID, batchNum, pSerialNum, pMessageData, timestmp);
		
		synchronized(recvList)
		{
			recvList.add(note);
		}
	}
	
	public IsoMessage getMessage(String pTranType, String field3, String pMerchantID, String pTermID,String batchNum, String pSerialNum)
	{
		RecvMessageNote note;
		Date endTime=null;
		try {
		//计时取系统时间
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
			Date d=new Date();
			endTime=sdf.parse(sdf.format(new Date(d.getTime()+50*1000)));
			while(true)
			{
				synchronized(recvList)
				{
					for(int i=0; i<recvList.size(); i++)
					{
						note = recvList.get(i) ;
						System.out.println(String.format("expected merchant value is %s, actual is %s",pMerchantID, note.szMerchantID));
						System.out.println(String.format("expected serial value is %s, actual is %s",pSerialNum, note.szSerialNum));
						System.out.println(String.format("expected termid value is %s, actual is %s",pTermID, note.szTermID));
						System.out.println(String.format("expected transtype value is %s, actual is %s",pTranType, note.szTranType));
						System.out.println(String.format("expected filed3 value is %s, actual is %s",field3, note.tran_procode));
						System.out.println(String.format("expected batchnum value is %s, actual is %s",batchNum, note.szBatchNum));
						
						if(note.szMerchantID.equalsIgnoreCase(pMerchantID)&&note.szSerialNum.equalsIgnoreCase(pSerialNum)
								&&note.szTermID.equalsIgnoreCase(pTermID)&&note.szTranType.equalsIgnoreCase(pTranType)
								&&note.tran_procode.equalsIgnoreCase(field3)
								)
						{
							if (!pTranType.equalsIgnoreCase("0810")&&!note.szBatchNum.equalsIgnoreCase(batchNum))
							{
								continue;
							}
							
							recvList.remove(note);
							return note.szMessageData;
						}
					}
					
					long nowtmp = (new Date()).getTime(); // 每4小时清理一下僵尸对象
					if (nowtmp > lastCkTime + 4*60*60*1000)
					{
						System.out.println("开始进行对象清理！");
						Iterator<RecvMessageNote> itr = recvList.iterator();
						while(itr.hasNext())
						{
							RecvMessageNote item = itr.next();
							if (nowtmp - item.timeStmp.getTime() > 60*1000)
							{
								itr.remove();
							}
						}
						
						lastCkTime = nowtmp;
					}
				}
				
				Date now=new Date();
				boolean ifRetn=now.after(endTime);
				if(!ifRetn){
					break;
				}else{
					Thread.sleep(500) ;
				}
			}
			} catch (ParseException e) {//用于日期中捕获异常
				e.printStackTrace();
			}catch (InterruptedException e) {//用于休眠500时捕获异常
				e.printStackTrace();
			}
			return null ;
	}
}
