package com.rhjf.appserver.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.sf.json.JSONObject;

public class RabbitmqSend {

	private final static String DFORDER = "DFORDER";
	
	private final static String EXCHANGE = "DFORDER";

	public static void sendMessage(String message) throws Exception {
		// 创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 设置RabbitMQ地址
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
		
		// 声明一个队列 --
		// 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。
		channel.queueDeclare(DFORDER, false , false, false, null);
		
		channel.exchangeDeclare(EXCHANGE , "direct");   //topic  direct
		channel.queueBind(DFORDER, EXCHANGE , "DF");
		
		// 发送消息到队列中
		channel.basicPublish(EXCHANGE, "DF", null , message.getBytes("UTF-8"));
		System.out.println("P [x] Sent '" + message + "'");
		// 关闭频道和连接
		channel.close();
		connection.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("orderNumber", "20171213164645529313707");
		json.put("dfType", "Trade");
		
		RabbitmqSend.sendMessage(json.toString());
//		Runtime.getRuntime().exec("java -jar /daifu.jar >> /log.log");
	}
}
