package com.cn.leedane.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.SignInBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.SignInService;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.JsonUtil;

@Controller
@RequestMapping("/leedane/signIn")
public class SignInController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private SignInService<SignInBean> signInService;
	
	// 操作日志
	@Autowired
	protected OperateLogService<OperateLogBean> operateLogService;
	
	/**
	 * 执行签到的主方法
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/signIn")
	public String signIn(HttpServletRequest request, HttpServletResponse response){
		//保存签到记录
		//更新积分
		//更新操作日志
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			int id = JsonUtil.getIntValue(getJsonFromMessage(message), "id", getUserFromMessage(message).getId());
			boolean isSave = signInService.saveSignIn(getJsonFromMessage(message), userService.findById(id), request);
			message.put("isSuccess", isSave);
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 判断当天是否已经登录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/currentDateIsSignIn")
	public String currentDateIsSignIn(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			JSONObject json = getJsonFromMessage(message);
			UserBean user = getUserFromMessage(message);
			if(json.has("id")) {
				int id = JsonUtil.getIntValue(getJsonFromMessage(message), "id", getUserFromMessage(message).getId());
				String dateTime = DateUtil.DateToString(new Date(), "yyyy-MM-dd");		
				message.put("isSuccess", signInService.isSign(id, dateTime));
				
				// 保存操作日志信息
				String subject = user.getAccount()+"判断当天是否签到";
				this.operateLogService.saveOperateLog(user, request, new Date(), subject, "currentDateIsSignIn", 1, 0);
				printWriter(message, response);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获取签到历史记录
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			List<Map<String, Object>> result= signInService.getSignInByLimit(getJsonFromMessage(message), getUserFromMessage(message), request);
			System.out.println("获得签到的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
}
