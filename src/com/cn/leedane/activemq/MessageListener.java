package com.cn.leedane.activemq;

import javax.jms.Message;

/**
 * 消息监听
 * @author LeeDane
 * 2015年8月12日 下午3:29:54
 * Version 1.0
 */
public class MessageListener implements javax.jms.MessageListener{

	@Override
	public void onMessage(Message arg0) {
		System.out.println("消息的监听");
	}

}
