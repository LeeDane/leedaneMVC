package com.cn.leedane.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.handler.CloudStoreHandler;
import com.cn.leedane.model.EmailBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.rabbitmq.SendMessage;
import com.cn.leedane.rabbitmq.send.EmailSend;
import com.cn.leedane.rabbitmq.send.ISend;
import com.cn.leedane.utils.Base64Util;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.EmailType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;
import com.cn.leedane.wechat.util.HttpRequestUtil;
import com.qiniu.util.Auth;

@Controller
@RequestMapping("/leedane/tool")
public class ToolController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 翻译
	 * @return
	 */
	@RequestMapping("/fanyi")
	public String fanyi(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			String content = JsonUtil.getStringValue(getJsonFromMessage(message), "content");
			String msg = HttpRequestUtil.sendAndRecieveFromYoudao(content);
			msg = StringUtil.getYoudaoFanyiContent(msg);
			message.put("isSuccess", true);
			message.put("message", msg);
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
	 * 发送邮件通知的接口
	 * @return
	 */
	@RequestMapping("/sendEmail")
	public String sendEmail(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			//{"id":1, "to_user_id": 2}
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			JSONObject json = getJsonFromMessage(message);
			String toUserId = JsonUtil.getStringValue(json, "to_user_id");//接收邮件的用户的Id，必须
			String content = JsonUtil.getStringValue(json, "content"); //邮件的内容，必须
			String object = JsonUtil.getStringValue(json, "object"); //邮件的标题，必须
			if(StringUtil.isNull(toUserId) || StringUtil.isNull(content) || StringUtil.isNull(object)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.参数不存在或为空.value));
				message.put("responseCode", EnumUtil.ResponseCode.参数不存在或为空.value);
				printWriter(message, response, start);
				return null;
			}
			
			UserBean toUser = userService.findById(StringUtil.changeObjectToInt(toUserId));
			if(toUser == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该用户不存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.该用户不存在.value);
				printWriter(message, response, start);
				return null;
			}
			if(StringUtil.isNull(toUser.getEmail())){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.对方还没有绑定电子邮箱.value));
				message.put("responseCode", EnumUtil.ResponseCode.对方还没有绑定电子邮箱.value);
				printWriter(message, response, start);
				return null;
			}
			
			//String content = "用户："+user.getAccount() +"已经添加您为好友，请您尽快处理，谢谢！";
			//String object = "LeeDane好友添加请求确认";
			Set<UserBean> set = new HashSet<UserBean>();		
			set.add(toUser);
			EmailBean emailBean = new EmailBean();
			emailBean.setContent(content);
			emailBean.setCreateTime(new Date());
			emailBean.setFrom(getUserFromMessage(message));
			emailBean.setSubject(object);
			emailBean.setReplyTo(set);
			emailBean.setType(EmailType.新邮件.value); //新邮件

			try {
				ISend send = new EmailSend(emailBean);
				SendMessage sendMessage = new SendMessage(send);
				sendMessage.sendMsg();//发送消息队列到消息队列
				message.put("isSuccess", true);
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件已经发送.value));

			} catch (Exception e) {
				e.printStackTrace();
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.邮件发送失败.value)+",失败原因是："+e.toString());
				message.put("responseCode", EnumUtil.ResponseCode.邮件发送失败.value);
			}
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
	 * 发送信息
	 * @return
	 */
	@RequestMapping("/sendMessage")
	public String sendMessage(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			//type: 1为通知，2为邮件，3为私信，4为短信
			message.put("isSuccess", false);
			message.putAll(userService.sendMessage(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 获取七牛服务器的token凭证
	 * @return
	 */
	@RequestMapping("/getQiNiuToken")
	public String getQiNiuToken(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		message.put("isSuccess", false);
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.put("isSuccess", true);
			message.put("message", getToken());
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
	 * 根据图片地址获取网络图片流
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getNetwordImage")
	public String getNetwordImage(HttpServletRequest request, HttpServletResponse response){
		String imgUrl = request.getParameter("url");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		InputStream is = null;
		ByteArrayOutputStream out = null;
		long start = System.currentTimeMillis();
		try {			
			URL url = new URL(imgUrl);
			URLConnection uc = url.openConnection(); 
			is = uc.getInputStream(); 	    
			out = new ByteArrayOutputStream(); 
			int i=0;
			while((i = is.read())!=-1)   { 
				out.write(i); 
			}
			message.put("isSuccess", true);
			message.put("message", ConstantsUtil.BASE64_JPG_IMAGE_HEAD + new String(Base64Util.encode(out.toByteArray())));
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	
	/**
     * 获取token 本地生成
     * 
     * @return
     */
    private String getToken() {
    	Auth auth = Auth.create(CloudStoreHandler.ACCESSKEY, CloudStoreHandler.SECRETKEY);
    	return auth.uploadToken(CloudStoreHandler.BUCKETNAME);
    }
}
