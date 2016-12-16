package com.cn.leedane.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.FilePathBean;
import com.cn.leedane.service.AppVersionService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/appVersion")
public class AppVersionController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	//上传filePath表的service
	@Autowired
	private AppVersionService<FilePathBean> appVersionService;
	
	/**
	 * 获取APP的最新版本信息
	 * @return
	 */
	@RequestMapping("/getNewest")
	public String getNewest(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();	
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(appVersionService.getNewest(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("检查最新app版本" +(end - start) +"毫秒");
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获取APP的版本列表信息
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			checkParams(message, request);
			
			message.putAll(appVersionService.paging(getJsonFromMessage(message), getUserFromMessage(message), request));
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
