package com.cn.leedane.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.FinancialOneLevelCategoryBean;
import com.cn.leedane.service.FinancialOneCategoryService;
import com.cn.leedane.utils.EnumUtil;

/**
 * 记账一级分类控制器
 * @author LeeDane
 * 2016年12月8日 下午11:19:03
 * Version 1.0
 */
@Controller
@RequestMapping("/leedane/financial/category/one")
public class FinancialOneCategoryController extends BaseController{
	
	@Autowired
	private FinancialOneCategoryService<FinancialOneLevelCategoryBean> financialOneCategoryService;
	
	/**
     * 获取二级分类的
     * @return 
     */
	@RequestMapping("/getAll")
    public String getAll(HttpServletRequest request, HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	Map<String, Object> message = new HashMap<String, Object>();
    	try{
    		if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(financialOneCategoryService.getAll(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
        long endTime = System.currentTimeMillis();
        System.out.println("记账数据保存总计耗时："+ (endTime - startTime) +"毫秒");
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
    }
}
