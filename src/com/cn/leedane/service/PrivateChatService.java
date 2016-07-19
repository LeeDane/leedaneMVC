package com.cn.leedane.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.model.IDBean;
import com.cn.leedane.model.UserBean;

/**
 * 私信相关service接口类
 * @author LeeDane
 * 2016年7月12日 上午11:34:30
 * Version 1.0
 */
public interface PrivateChatService <T extends IDBean>{

	/**
	 * 获取私信的分页列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 发送私信信息
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> send(JSONObject jo, UserBean user, HttpServletRequest request);
}
