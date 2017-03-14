package com.cn.leedane.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.leedane.model.ChatSquareBean;
import com.cn.leedane.model.ChatSquareSettingBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.service.ChatSquareService;
import com.cn.leedane.service.ChatSquareSettingService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EnumUtil;

/**
 * 聊天广场(包括设置)的控制类
 * @author LeeDane
 * 2017年3月2日 上午10:45:12
 * Version 1.0
 */
@Controller
@RequestMapping("/leedane/chat/square")
public class ChatSquareController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ChatSquareService<ChatSquareBean> chatSquareService;
	
	/**
	 * 聊天设置
	 */
	@Autowired
	private ChatSquareSettingService<ChatSquareSettingBean> chatSquareSettingService;
	
	// 操作日志
	@Autowired
	protected OperateLogService<OperateLogBean> operateLogService;
	
	/**
	 * 获取系统当天活跃的用户列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getActiveUser")
	public String getActiveUser(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			checkParams(message, request);
			
			message.putAll(chatSquareService.getActiveUser(DateUtil.getTodayStart(), 8));
			message.put("responseCode", EnumUtil.ResponseCode.请求返回成功码.value);
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	
	
	/**
	 * 获取系统当天活跃的用户列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/setting/{sid}", method = RequestMethod.PUT, produces = {"application/json;charset=UTF-8"})
	public String updateSetting(@PathVariable int sid, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		System.out.println("sid="+ sid);
		long start = System.currentTimeMillis();
		try {
			checkParams(message, request);
			message.putAll(chatSquareService.getActiveUser(DateUtil.getTodayStart(), 8));
			message.put("responseCode", EnumUtil.ResponseCode.请求返回成功码.value);
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
}
