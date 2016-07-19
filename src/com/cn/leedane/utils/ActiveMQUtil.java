package com.cn.leedane.utils;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.cn.leedane.activemq.MessageListener;

/**
 * activeMQ相关工具类
 * @author LeeDane
 * 2016年7月12日 下午2:38:59
 * Version 1.0
 */
public class ActiveMQUtil {


	private final static String[] USERS = {"aa","bb","cc","dd","ee"};
	
	private static ActiveMQUtil mActiveMQUtil;
	
	private ActiveMQUtil(){}
	
	/**
	 * 实例化对象
	 * @return
	 */
	public static synchronized ActiveMQUtil getInstance(){
		if(mActiveMQUtil == null){
			mActiveMQUtil = new ActiveMQUtil();
		}
		return mActiveMQUtil;
	}

	/**
	 * 发送ActiveMQ点对点的消息
	 */
	public void sendMessage(){
		try {
			//创建一个连接工厂
			String url = "tcp://localhost:61616";
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			//设置用户名和密码，这个用户名和密码在conf目录下的crednetials.properties文件中，也可以在activemq.xml中配置
			connectionFactory.setUserName("admin");
			connectionFactory.setPassword("admin");
			
			//创建连接
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			//创建Session，参数解释：
			//第一个参数是是否使用事务：当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
			//第二个参数消息的确认模式：
			//AUTO_ACKNOWLENGE : 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中，可能因为错误而丢失消息。
			//CLIENT_ACKNOWLEDGE : 由消息接收者确认收到消息，通过调用消息的acknowledge()方法(会通知消息提供者收到消息)
			//DUPS_OK_ACKNOWLEDGE : 指定消息提供者在消息接收者没有确认发送时重新发送消息（这种确认模式不在乎接收者收到重复的消息）
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			//创建消息生产者
			MessageProducer producer = session.createProducer(null);
			
			for(int i = 0 ; i < USERS.length; i++){
				//创建目标，就创建主题也可以创建队列
				Destination destination = session.createQueue("user."+USERS[i]);
				TextMessage message = session.createTextMessage("Hello-->" + USERS[i]);
				producer.send(destination, message);  
			}
			//设置持久化，DeliveryMode.PERSISTENT 和DeliveryMode.NON_PERSISTENT
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			//创建消息
			//String text = "Hello ActiveMQ1";
			
			//发送消息到ActiveMQ
			//producer.send(message);
			
			System.out.println("Message is sent");
			//关闭资源
			session.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取ActiveMQ点对点消息
	 * @return
	 */
	public String getMessage(){
		String result = "";
		try {
			
			String url = "tcp://localhost:61616";
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			//设置用户名和密码，这个用户名和密码在conf目录下的crednetials.properties文件中，也可以在activemq.xml中配置
			connectionFactory.setUserName("admin");
			connectionFactory.setPassword("admin");
			//创建连接
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			//创建session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			for(int i = 0 ; i < USERS.length; i++){
				//创建目标，就创建主题也可以创建队列
				Destination destination = session.createQueue("user."+USERS[i]);
				//创建消息消费者
				MessageConsumer consumer = session.createConsumer(destination);
				//接收消息，参数：接收消息的超时时间，为0的话则不超时，receive返回下一个消息，但是超时了或者消费者被关闭，返回nul
				Message message = consumer.receive(3000);
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: " + text);
					result = result + text;
					
				}else{
					if(message == null) return null;
					System.out.println("Received: " + message);	
					result = message.toString();
				}
				consumer.setMessageListener(new MessageListener());
				//关闭资源
				consumer.close();
			}
			session.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	 * 发送ActiveMQ订阅模式的消息
	 */
	public void sendTopicMessage(){
		try {
			//创建一个连接工厂
			String url = "tcp://localhost:61616";
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			//设置用户名和密码，这个用户名和密码在conf目录下的crednetials.properties文件中，也可以在activemq.xml中配置
			connectionFactory.setUserName("admin");
			connectionFactory.setPassword("admin");
			
			//创建连接
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			//创建Session，参数解释：
			//第一个参数是是否使用事务：当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
			//第二个参数消息的确认模式：
			//AUTO_ACKNOWLENGE : 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中，可能因为错误而丢失消息。
			//CLIENT_ACKNOWLEDGE : 由消息接收者确认收到消息，通过调用消息的acknowledge()方法(会通知消息提供者收到消息)
			//DUPS_OK_ACKNOWLEDGE : 指定消息提供者在消息接收者没有确认发送时重新发送消息（这种确认模式不在乎接收者收到重复的消息）
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Topic topic = session.createTopic("leedaneTopic");  
			//创建消息生产者
			MessageProducer producer = session.createProducer(topic);
					
			//创建目标，就创建主题也可以创建队列
			//Destination destination = session.createQueue("all");
			TextMessage message = session.createTextMessage("Hello-->All12");
			producer.send(message);  
			
			//设置持久化，DeliveryMode.PERSISTENT 和DeliveryMode.NON_PERSISTENT
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			//创建消息
			//String text = "Hello ActiveMQ1";
			
			//发送消息到ActiveMQ
			//producer.send(message);
		
			System.out.println("Message is sent");
			//关闭资源
			session.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取ActiveMQ订阅模式消息
	 * @return
	 */
	public String getTopicMessage(){
		String result = "";
		try {
			
			String url = "tcp://localhost:61616";
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			//设置用户名和密码，这个用户名和密码在conf目录下的crednetials.properties文件中，也可以在activemq.xml中配置
			connectionFactory.setUserName("admin");
			connectionFactory.setPassword("admin");
			//创建连接
			Connection connection = connectionFactory.createConnection();
			connection.start();
			
			//创建session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Topic topic = session.createTopic("leedaneTopic");  
			/*for(int i = 0 ; i < USERS.length; i++){
				//创建目标，就创建主题也可以创建队列
				//Destination destination = session.createQueue("user."+USERS[i]);
				//创建消息消费者
				MessageConsumer consumer = session.createConsumer(topic);
				//接收消息，参数：接收消息的超时时间，为0的话则不超时，receive返回下一个消息，但是超时了或者消费者被关闭，返回nul
				Message message = consumer.receive(3000);
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: " + text);
					result = result + text;
					
				}else{
					System.out.println("Received: " + message);	
					result = message.toString();
				}
				//关闭资源
				consumer.close();
			}*/
			
			MessageConsumer consumer = session.createConsumer(topic);
			Message message = consumer.receive(3000);
			if(message instanceof TextMessage){
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println("Received: " + text);
				result = result + text;
				
			}else{
				System.out.println("Received: " + message);	
				result = message != null ? message.toString() : null;
			}
			/* consumer.setMessageListener(new MessageListener() {  
				 public void onMessage(Message message) {  
					 TextMessage tm = (TextMessage) message;  
					 try {  
						 System.out.println("Received message: " + tm.getText());  
					 } catch (JMSException e) {  
						 e.printStackTrace();  
					 }  
				 }  
			}); */ 

			
			
			
			//session.close();
			//connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		ActiveMQUtil.getInstance().sendMessage();
		System.out.println(ActiveMQUtil.getInstance().getMessage());
	}
}
