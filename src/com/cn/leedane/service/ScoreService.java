package com.cn.leedane.service;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.model.IDBean;
import com.cn.leedane.model.ScoreBean;
import com.cn.leedane.model.UserBean;
/**
 * 积分的Service类
 * @author LeeDane
 * 2016年7月12日 上午11:35:07
 * Version 1.0
 */
public interface ScoreService<T extends IDBean>{
	
	/**
	 * 基础的保存实体的方法
	 * @param t
	 * @return
	 */
	public boolean save(ScoreBean t);
	
	/**
	 * 获取当前用户的总积分
	 * @param userId
	 * @return
	 */
	public int getTotalScore(int userId);

	/**
	 * 分页获得积分历史列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> getLimit(JSONObject jo, UserBean user, HttpServletRequest request);
	
}
