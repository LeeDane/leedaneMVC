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
	 * 客户端数据同步
	 * @param jo
	 * @param user
	 * @param request
	 * @return 返回成功同步的数量和有冲突的数据ID数组
	 */
	public Map<String, Object> synchronous(JSONObject jo, UserBean user, HttpServletRequest request);
	
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
	public Map<String, Object> force(JSONObject jo, UserBean user, HttpServletRequest request);
}
