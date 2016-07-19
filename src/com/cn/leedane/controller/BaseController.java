package com.cn.leedane.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.util.CollectionUtils;

import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.UserService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.HttpUtil;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;

public class BaseController {
	
	@Resource
	private UserService<UserBean> userService;
	
	@Resource
	private UserHandler userHandler;
	
	/**
	 * 通过原先servlet方式输出json对象。
	 * 目的：解决复杂的文本中含有特殊的字符导致struts2的json
	 * 		解析失败，给客户端返回500的bug
	 */
	protected void printWriter(Map<String, Object> message, HttpServletResponse response){
		if(message.containsKey("json"))
			message.remove("json");
		
		if(message.containsKey("user"))
			message.remove("user");
		
		JSONObject jsonObject = JSONObject.fromObject(message);
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.append(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer != null)
				writer.close();
		}
		
	}

	
	/**
	 * 校验请求参数
	 * @param request
	 * @return
	 */
	protected boolean checkParams(Map<String, Object> message, HttpServletRequest request){
		boolean result = false;
		message.put("isSuccess", false);
		try{
			return checkLogin(request, message);
			//UserBean user = (UserBean) request.getAttribute("user");
			//JSONObject json = JSONObject.fromObject(request.getAttribute("params"));
			//message.put("json", json);
			//message.put("user", user);
			/*if(message.containsKey("")){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
				message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
			}*/
		}catch(Exception e){
			e.printStackTrace();
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.缺少请求参数.value));
			message.put("responseCode", EnumUtil.ResponseCode.缺少请求参数.value);
		}
		return result;
	}
	
	public boolean checkLogin(HttpServletRequest request, Map<String, Object> message){
		boolean result = false;
		String params = request.getParameter("params");
		if(StringUtil.isNull(params)){
			//校验用户信息
			JSONObject json = null;
			UserBean user = null;
			try {
				
				json = HttpUtil.getJsonObjectFromInputStream(null, request);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(json != null){		
				message.put("json", json);
				if(json.has("id")){
					//设置为了防止过滤路径，直接在这里加载用户请求有id为默认登录用户
					try {
						user = userService.findById(JsonUtil.getIntValue(json, "id"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					//设置为了防止过滤路径，直接在这里加载用户1为默认登录用户
					try {
						user = userService.findById(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//必须有免登陆验证码和账号
				if(json.has("no_login_code") && json.has("account")){
										
					//拿到免登陆码
					String noLoginCode = JsonUtil.getStringValue(json, "no_login_code");
					//拿到登录账户
					String account = JsonUtil.getStringValue(json, "account");
					//UserService<UserBean> userService = new UserServiceImpl();
					//user = userService.getUserByNoLoginCode(account, noLoginCode);
					String returnErrorMeg = "";
					int returnErrorCode = 0;
					//if(user != null ){
						//获取登录用户的状态
						int status = user.getStatus();
						
						boolean canDo = false;
						
						//0:被禁止 1：正常，2、注册未激活  ，3：未完善信息 ， 4：被禁言 ，5:注销
						switch (status) {
							case ConstantsUtil.STATUS_DISABLE:
								returnErrorMeg = "账号"+user.getAccount()+"已经被禁用，有问题请联系管理员";
								returnErrorCode = EnumUtil.ResponseCode.账号已被禁用.value;
								break;
							case ConstantsUtil.STATUS_NORMAL:
								canDo = true;
								break;
							case 2:
								returnErrorMeg = "请先激活账号"+ user.getAccount();
								returnErrorCode = EnumUtil.ResponseCode.账号未被激活.value;
								break;
							case 3:
								returnErrorMeg = "请先完善账号"+ user.getAccount() +"的信息";
								returnErrorCode = EnumUtil.ResponseCode.请先完善账号信息.value;
								break;
							case 4:
								returnErrorMeg = "账号"+ user.getAccount()+"已经被禁言，有问题请联系管理员";
								returnErrorCode = EnumUtil.ResponseCode.账号已被禁言.value;
								break;
							case 5:
								returnErrorMeg = "账号"+ user.getAccount()+"已经被注销，有问题请联系管理员";
								returnErrorCode = EnumUtil.ResponseCode.账号已被注销.value;
								break;
							default:
								break;
						}
						
						userHandler.addLastRequestTime(user.getId());
						
						//当验证账号的状态是正常的情况，继续执行action
						if(canDo){
							message.put("user", user);
							result = true;
						}else{
							message.put("message", returnErrorMeg);
							message.put("responseCode", returnErrorCode);
						}
							
					//}
				}
			}
		}else{
			message.put("json", params);
		}
		
		return result;
	}
	
	/**
	 * 从message中解析json数据
	 * @param message
	 * @return
	 */
	protected JSONObject getJsonFromMessage(Map<String, Object> message){
		JSONObject json = null;
		if(!CollectionUtils.isEmpty(message) && message.containsKey("json")){
			json = (JSONObject) message.get("json");
		}
		return json;
	}
	
	/**
	 * 从message中解析user数据
	 * @param message
	 * @return
	 */
	protected UserBean getUserFromMessage(Map<String, Object> message){
		UserBean user = null;
		if(!CollectionUtils.isEmpty(message) && message.containsKey("user")){
			user = (UserBean) message.get("user");
		}
		return user;
	}
}
