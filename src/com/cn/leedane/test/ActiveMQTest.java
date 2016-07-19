package com.cn.leedane.test;

import java.io.File;
import java.io.IOException;

import org.apache.activemq.command.ActiveMQTopic;
import org.aspectj.util.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.utils.ActiveMQUtil;
import com.cn.leedane.activemq.JmsSender;

/**
 * activeMQ相关的测试类
 * @author LeeDane
 * 2016年7月12日 下午3:08:47
 * Version 1.0
 */
public class ActiveMQTest extends BaseTest {
	
	@Autowired  
	private JmsSender messageSender;  
	
	private String destination;  
	private int no = 10* 10000;  
	private String message;  

	@Before  
	public void init() throws IOException {  
		String filePath = Thread.currentThread().getContextClassLoader()  
					.getResource("").getPath()  
                + "message.txt";  
		message = FileUtil.readAsString(new File(filePath));  
		this.destination = "asyncTopic";  
		//开启异步发送  
		this.messageSender.setSendAsync(true);  
	}  
	@Test  
	public void send() throws InterruptedException {  
		ActiveMQTopic dest = new ActiveMQTopic(this.destination);  
		for (int i = 0; i < no; i++) {  
			messageSender.sendSingle(message, dest);  
		}  
		Thread.sleep(1000000000);  
	}  

	
	/**
	 * 发送ActiveMQ点对点的消息
	 */
	@Test
	public void sendMessage(){
		ActiveMQUtil.getInstance().sendMessage();
	}
	
	/**
	 * 获取ActiveMQ点对点消息
	 * @return
	 */
	@Test
	public void getMessage() {
		System.out.println(ActiveMQUtil.getInstance().getMessage());
	}
	
	
}
