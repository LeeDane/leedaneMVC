package com.cn.leedane.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.model.IDBean;
import com.cn.leedane.model.UserBean;

/**
 * 记账相关service接口类
 * @author LeeDane
 * 2016年7月22日 上午8:31:34
 * Version 1.0
 */
public interface FinancialService<T extends IDBean>{
	
	/**
	 * 保存数据
	 * @param jo
	 * @param user
	 * @param request
	 * @return 返回服务器保存成功的ID
	 */
	public Map<String, Object> save(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 更新数据
	 * @param jo
	 * @param user
	 * @param request
	 * @return 
	 */
	public Map<String, Object> update(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 删除数据
	 * @param jo
	 * @param user
	 * @param request
	 * @return 
	 */
	public Map<String, Object> delete(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 客户端数据同步
	 * @param jo
	 * @param user
	 * @param request
	 * @return 返回成功同步的数量和有冲突的数据ID数组
	 */
	public Map<String, Object> synchronous(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 客户端强制更新数据(
     * 		在synchronous()后返回的冲突数据进行强制以客户端或者服务器端的为主，
     * 		要是以客户端的为主，将删掉服务器端的数据；
     * 		要是以服务器端的为主，将返回服务器端的数据，这时可以端需要做的就是替换掉客户端本地
     * 		数据为服务器端返回的数据。
     * )
	 * @param jo
	 * @param user
	 * @param request
	 * @return 
	 */
	public Map<String, Object> force(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取指定年份的数据
	 * @param jo
	 * @param user
	 * @param request
	 * @return 返回该年所有的记账记录
	 */
	public Map<String, Object> getByYear(JSONObject jsonObject, UserBean user, HttpServletRequest request);
	
	/**
	 * 获取该用户全部的数据
	 * @param jo
	 * @param user
	 * @param request
	 * @return 返回该用户所有的记账记录
	 */
	public Map<String, Object> getAll(JSONObject jsonObject, UserBean user, HttpServletRequest request);
}
