package com.cn.leedane.model;

import com.cn.leedane.mybatis.table.annotation.Column;


/**
 * 聊天广场设置实体类
 * @author LeeDane
 * 2017年3月2日 上午10:09:05
 * Version 1.0
 */
//@Table(name="T_ATTENTION")
public class ChatSquareSettingBean extends RecordTimeBean{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 默认的自我介绍
	 */
	public static final String DEFAULT_INTRODUCTION = "大家好，我是${username}。";
	//状态,1：正常，0:禁用，2、删除
	
	private String introduction; //介绍 
	
	private boolean firstHello; //第一次进入聊天室，是否启动自我介绍，控制introduction属性
	
	private boolean isAnonymity; //是否启动匿名发送(默认是false)

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public boolean isFirstHello() {
		return firstHello;
	}

	public void setFirstHello(boolean firstHello) {
		this.firstHello = firstHello;
	}

	public boolean isAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}
	
}
