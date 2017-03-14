package com.cn.leedane.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.mapper.ChatSquareMapper;
import com.cn.leedane.mapper.ChatSquareSettingMapper;
import com.cn.leedane.model.ChatSquareBean;
import com.cn.leedane.model.ChatSquareSettingBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.AdminRoleCheckService;
import com.cn.leedane.service.ChatSquareService;
import com.cn.leedane.service.ChatSquareSettingService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.utils.CollectionUtil;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;
/**
 * 聊天广场service的实现类
 * @author LeeDane
 * 2017年2月10日 下午4:12:06
 * Version 1.0
 */
@Service("chatSquareSettingService")
public class ChatSquareSettingServiceImpl extends AdminRoleCheckService implements ChatSquareSettingService<ChatSquareSettingBean>{
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private ChatSquareSettingMapper chatSquareSettingMapper;


	@Override
	public Map<String, Object> updateSetting(JSONObject jo, UserBean user,
				HttpServletRequest request) {
		logger.info("ChatSquareSettingServiceImpl-->updateSetting():jo=" +jo);
		int id = JsonUtil.getIntValue(jo, "settingId");//旧的记录获取ID
		ChatSquareSettingBean bean;
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(id > 0){
			bean = chatSquareSettingMapper.findById(ChatSquareSettingBean.class, id);
			if(bean == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
				message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				return message;
			}
		}else{
			bean = new ChatSquareSettingBean();
			bean.setCreateUserId(user.getId());
			bean.setCreateTime(new Date());
			bean.setStatus(ConstantsUtil.STATUS_NORMAL);
		}
		
		//设置是否开启
		bean.setAnonymity(JsonUtil.getBooleanValue(jo, "isAnonymity"));
		
		//设置自我介绍
		if(jo.has("firstHello")){
			boolean firstHello = JsonUtil.getBooleanValue(jo, "firstHello");
			if(firstHello){
				String introduction = JsonUtil.getStringValue(jo, "introduction");
				if(StringUtil.isNull(introduction)){
					introduction = ChatSquareSettingBean.DEFAULT_INTRODUCTION.replace("${username}", user.getAccount());
				}
				bean.setIntroduction(introduction);
			}else{
				
			}
			bean.setFirstHello(firstHello);
		}
		
		
		boolean result;
		
		if(id > 0)
			result = chatSquareSettingMapper.update(bean) > 0;
		else
			result = chatSquareSettingMapper.save(bean) > 0;
		
		if(result){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作成功.value));
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请求返回成功码.value));
			message.put("isSuccess", true);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"设置聊天室信息", StringUtil.getSuccessOrNoStr(result)).toString(), "updateSetting()", StringUtil.changeBooleanToInt(result), 0);		
		return message;
	}


	@Override
	public Map<String, Object> getSetting(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
