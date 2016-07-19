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

import com.cn.leedane.model.TransmitBean;
import com.cn.leedane.service.TransmitService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/transmit")
public class TransmitController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	//转发service
	@Autowired
	private TransmitService<TransmitBean> transmitService;

	/**
	 * 转发
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(transmitService.add(getJsonFromMessage(message), getUserFromMessage(message), request));
			long end = System.currentTimeMillis();
			System.out.println("转发总计耗时：" +(end - start) +"毫秒");
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
	 * 删除转发
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(transmitService.deleteTransmit(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 获取转发列表
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
			List<Map<String, Object>> result= transmitService.getLimit(getJsonFromMessage(message), getUserFromMessage(message), request);
			System.out.println("获得转发的数量：" +result.size());
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

	/**
	 * 更改转发编辑状态
	 * {'can_transmit':true, 'table_name':'t_mood', 'table_id': 1},所有参数全部必须
	 * @return
	 */
	@RequestMapping("/updateTransmitStatus")
	public String updateTransmitStatus(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(transmitService.updateTransmitStatus(getJsonFromMessage(message), getUserFromMessage(message), request));
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
