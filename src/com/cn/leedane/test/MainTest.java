package com.cn.leedane.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.ZXingCodeHandler;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.utils.DateUtil;
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
		RedisUtil redisUtil = new RedisUtil();
		int number = 0;
		String key = UserHandler.getRedisUserNameLoginErrorKey("leedane");
		//redisUtil.delete(key);
		if(redisUtil.hasKey(key)){
			String string = redisUtil.getString(key);
			//截取14位是因为前面14位被第一次错我的时间格式字符串
			number = StringUtil.changeObjectToInt(string.substring(14, string.length()));
		}
		System.out.println("number="+number);
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