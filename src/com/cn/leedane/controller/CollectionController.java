package com.cn.leedane.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.CollectionBean;
import com.cn.leedane.service.CollectionService;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.JsonUtil;

@Controller
@RequestMapping("/leedane/collection")
public class CollectionController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	//收藏夹service
	@Autowired
	private CollectionService<CollectionBean> collectionService;

	/**
	 * 添加收藏
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.put("isSuccess", collectionService.addCollect(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 删除收藏
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				printWriter(message, response);
				return null;
			}
			message.putAll(collectionService.deleteCollection(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 获取收藏列表
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
			//为了安全，必须是登录用户才能操作
			int toUserId = JsonUtil.getIntValue(getJsonFromMessage(message), "toUserId");
			if(toUserId != getUserFromMessage(message).getId()){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
				message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				printWriter(message, response);
				return null;
			}
			
			List<Map<String, Object>> result= collectionService.getLimit(getJsonFromMessage(message), getUserFromMessage(message), request);
			System.out.println("获得收藏的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter(message, response);
		return null;
	}
}
