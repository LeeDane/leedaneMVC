package com.cn.leedane.rabbitmq.recieve;

/**
 * 接收信息的接口
 * @author LeeDane
`* 2016年8月5日 下午3:24:46
 * Version 1.0
 */
public interface IRecieve {
	
	/**
	 * 获取队列的名称
	 * @return
	 */
	public String getQueueName();
	
	/**
	 * 队列中对象的类名
	 * @return
	 */
	public Class<?> getQueueClass();
	
	/**
	 * 执行队列的操作
	 * @param obj
	 */
	public boolean excute(Object obj);

}
