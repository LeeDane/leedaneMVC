package com.cn.leedane.service;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cn.leedane.model.UserBean;

/**
 * 聊天广场设置的Service类
 * @author LeeDane
 * 2017年3月2日 上午10:22:45
 * Version 1.0
 */
@Transactional("txManager")
public interface ChatSquareSettingService<ChatSquareSettingBean>{

	/**
	 * 更新设置
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> updateSetting(JSONObject jo, UserBean user,
			HttpServletRequest request) ;
	
	
	/**
	 * 获取聊天列表
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	//标记该方法不需要事务
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Map<String, Object> getSetting(JSONObject jo, UserBean user, HttpServletRequest request);
}
