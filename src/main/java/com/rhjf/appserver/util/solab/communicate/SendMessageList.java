package com.rhjf.appserver.util.solab.communicate;

import java.util.LinkedList;

public class SendMessageList 
{
	private LinkedList<byte[]> sendList = new LinkedList<byte[]>() ;
	
	private static SendMessageList instance = null;
	
	public static SendMessageList getInstance(){
		if(instance==null){
			instance = new SendMessageList();
			return instance;
		}else{
			return instance;
		}
	}
	
	public void putMessage(byte[] pReqMessage)
	{
		synchronized(sendList)
		{
			sendList.add(pReqMessage) ;
		}
	}
	
	public byte[] getMessage()
	{
		if(sendList.size() > 0)
		{
			byte[] pReqMessage = sendList.get(0) ;
			return pReqMessage;
		}else
		{
			return null;
		}
	}
	
	public void removeMessage()
	{
		synchronized(sendList)
		{
			sendList.remove(0) ;
		}
	}
}
