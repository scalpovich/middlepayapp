package com.rhjf.appserver.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.sf.json.JSONObject;  

public class P {
	 private final static String queueName = "queue_again";  
	 
		
	private final static String EXCHANGE = "exchange_again";
	  
	public static void main(String[] argv) throws Exception {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
		factory.setHost("10.10.20.101");
		factory.setUsername("zhoufy");
		factory.setPassword("zhoufy");
		factory.setPort(5672);
		factory.setVirtualHost("/");
		// 创建一个新的连接
		Connection connection = factory.newConnection();
		// 创建一个频道
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE, "direct", true);

		channel.queueDeclare(queueName, true, false, false, null);
		
		channel.queueBind(queueName, EXCHANGE, "againDF");
		
		JSONObject json = new JSONObject();
		json.put("orderNumber", "20180202132034790787952");
		json.put("dfType", "Trade");
		
		System.out.println(json.toString());

//		channel.basicPublish(EXCHANGE,"againDF",null,json.toString().getBytes());
		
		// 关闭频道和连接
		channel.close();
		connection.close();
	}
}
