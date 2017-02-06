package com.cn.leedane.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.ScoreBean;
import com.cn.leedane.service.ScoreService;
import com.cn.leedane.utils.EnumUtil;

@Controller
@RequestMapping("/leedane/score")
public class ScoreController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ScoreService<ScoreBean> scoreService;
	
	@Resource
	public void setScoreService(ScoreService<ScoreBean> scoreService) {
		this.scoreService = scoreService;
	}
	
	/**
	 * 分页获取积分历史列表
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(scoreService.getLimit(getJsonFromMessage(message), getUserFromMessage(message), request));
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
