package com.cn.leedane.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.model.IDBean;
import com.cn.leedane.model.UserBean;

/**
 * App版本管理service接口类
 * @author LeeDane
 * 2016年7月12日 上午11:28:51
 * Version 1.0
 */
public interface AppVersionService <T extends IDBean>{
	/**
	 * 获取最新版本号
	 * @param jo 格式"{'version','1.0.0'}"
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getNewest(JSONObject jo, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取数据库中上传的最新版本
	 * @return
	 */
	public List<Map<String, Object>> getNewestVersion();
	
	
}
