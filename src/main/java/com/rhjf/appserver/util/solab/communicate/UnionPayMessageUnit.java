package com.rhjf.appserver.util.solab.communicate;

import java.nio.ByteBuffer; 

import com.rhjf.appserver.util.solab.iso8583.IsoMessage;
import com.rhjf.appserver.util.solab.iso8583.IsoValue;
import com.rhjf.appserver.util.solab.iso8583.MessageFactory;

public class UnionPayMessageUnit {
	private static Config config = Config.getInstance();
	public UnionPayMessageUnit() 
	{
		
	}
	
	//打包消费报文
	public static byte[] packUp(IsoMessage message){
		MessageFactory mfact = config.getMessFact8583();
		IsoMessage m = mfact.newMessage(message.getType());
		m.setBinary(true);
		
		m.setIsoHeader(message.getIsoHeader());
		for(int i=2;i<65;i++){
			if(message.getObjectValue(i)!=null && !message.getObjectValue(i).toString().equals("")){
				IsoValue val1 = message.getField(i);
				m.setValue(i, val1.getValue(), val1.getType(), val1.getOriLen());
			}
		}
		
		ByteBuffer bb = m.writeToBuffer(2);
		byte[] ba8583 = bb.array();
		return ba8583;
	}
	
	
	public static IsoMessage coverAllMessage(IsoMessage srcMsg, IsoMessage destMsg){
		destMsg.setType(srcMsg.getType());
		
		for(int i=2;i<65;i++){
			if(srcMsg.getObjectValue(i)!=null && !srcMsg.getObjectValue(i).toString().equals("")){
				IsoValue val1 = srcMsg.getField(i);
				destMsg.setValue(i, val1.getValue(), val1.getType(), val1.getOriLen());
			}
		}
		return destMsg;
	}
	
}
