package com.cn.leedane.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.FinancialTwoLevelCategoryBean;
import com.cn.leedane.service.FinancialTwoCategoryService;
import com.cn.leedane.utils.EnumUtil;

/**
 * 记账二级分类控制器
 * @author LeeDane
 * 2016年12月8日 下午11:23:10
 * Version 1.0
 */
@Controller
@RequestMapping("/leedane/financial/category/two")
public class FinancialTwoCategoryController extends BaseController{
	
	@Autowired
	private FinancialTwoCategoryService<FinancialTwoLevelCategoryBean> financialTwoCategoryService;
	
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
				printWriter(message, response, startTime);
				return null;
			}
			message.putAll(financialTwoCategoryService.getAll(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, startTime);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
        long endTime = System.currentTimeMillis();
        System.out.println("记账数据保存总计耗时："+ (endTime - startTime) +"毫秒");
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, startTime);
		return null;
    }
}
