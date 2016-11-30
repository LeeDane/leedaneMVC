package com.cn.leedane.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.model.IDBean;
import com.cn.leedane.model.UserBean;
/**
 * 记账位置信息的Service类
 * @author LeeDane
 * 2016年11月22日 下午2:51:47
 * Version 1.0
 */
public interface FinancialLocationService<T extends IDBean>{

	/**
	 * 添加记账位置
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> add(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 更新记账位置
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> update(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 删除记账位置
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> delete(JSONObject jo, UserBean user, HttpServletRequest request) ;



	/**
	 * 获取记账位置列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> paging(JSONObject jo, UserBean user,
			HttpServletRequest request);
	
}
