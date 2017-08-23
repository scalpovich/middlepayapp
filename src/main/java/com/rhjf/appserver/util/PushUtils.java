package com.rhjf.appserver.util;

import com.rhjf.appserver.constant.Constant;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;


public class PushUtils {

	
	static LoggerTool log = new LoggerTool(PushUtils.class);
	
	public static void IOSPush(String content,String tonken){
		log.info(XingeApp.pushTokenIos(Constant.XingeApp_IOS_ACCESS_ID, Constant.XingApp_IOS_ACCESS_KEY, content, tonken,  XingeApp.IOSENV_PROD).toString()); 
	}
	
	public static void AndroidPush(String title, String content , String token){
		XingeApp xinge = new XingeApp(Constant.XingApp_Android_ACCESS_ID, Constant.XingApp_Android_ACCESS_KEY);
		Style style = new Style(0, 1, 1, 1, 0, 1,0,1);
		Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setType(Message.TYPE_NOTIFICATION);
        message.setExpireTime(0);
        message.setStyle(style);
        log.info(xinge.pushSingleDevice(token, message).toString().toLowerCase());
	}
}
