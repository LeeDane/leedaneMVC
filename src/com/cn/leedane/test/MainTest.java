package com.cn.leedane.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.ZXingCodeHandler;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.LuceneUtil;
import com.cn.leedane.utils.MD5Util;
import com.cn.leedane.utils.StringUtil;
import com.google.zxing.WriterException;

/**
 * main方法相关的测试类
 * @author LeeDane
 * 2016年7月12日 下午3:12:24
 * Version 1.0
 */
public class MainTest {

	public static void main(String[] args) throws IOException {
		int distance = 112456399 + 11;
		System.out.println(((distance/1000) > 0 ? ((distance/1000000) > 0 ? (distance/1000000)+ "千公里": (distance/1000) + "公里"): "") +  ((distance%1000) > 0 ? (distance%1000) +"米": ""));
		/*System.out.println(DateUtil.DateToString(new Date(), "yyyyMM"));
		System.out.println(DateUtil.DateToString(new Date(), "yyyy年MM月"));
		System.out.println(DateUtil.DateToString(new Date(), "MM月dd日"));
		System.out.println(DateUtil.DateToString(new Date(), "dd日HH时"));*/
	
	}
	
	private static void testImages() {
		Date d1 = DateUtil.stringToDate("2016-11-04 10:53:35");
		Date d2 = DateUtil.stringToDate("2016-11-04 10:48:01");
		System.out.println(DateUtil.leftMinutes(d1, d2));
		
	}

	private static void testStringBuffer() {
		/*StringBuffer buffer = new StringBuffer("hhfhfhf可减肥咖啡");
		buffer.*/
	}

	private static int getInt(){
		int i = 80;
		if(i > 0)
			return 10;
		return 0;
		
	}
}