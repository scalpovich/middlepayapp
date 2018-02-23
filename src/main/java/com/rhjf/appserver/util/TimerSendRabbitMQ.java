package com.rhjf.appserver.util;

import java.util.TimerTask;

public class TimerSendRabbitMQ extends TimerTask{
	
	private String message;
	
	private String delayTime;
	
	public TimerSendRabbitMQ(String message , String delayTime ){
		this.message = message;
		this.delayTime = delayTime;
	}

	@Override
	public void run() {
		try {
			RabbitmqSend.sendMessage(message , delayTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
