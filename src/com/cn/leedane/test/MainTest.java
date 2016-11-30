package com.cn.leedane.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.cn.leedane.handler.ZXingCodeHandler;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.MD5Util;
import com.google.zxing.WriterException;

/**
 * main方法相关的测试类
 * @author LeeDane
 * 2016年7月12日 下午3:12:24
 * Version 1.0
 */
public class MainTest {

	public static void main(String[] args) throws IOException {
		
		String oldString = "14804709117126e31568670e51be42bc7978cc2066ea0339.53588594388793";
		String newString = "148047091171266e31568670e51be42bc7978cc2066ea0";
		System.err.println(MD5Util.compute("leedane"));
		System.out.println(oldString.startsWith(newString));
		//Desktop.getDesktop().open(new File("D:\\work\\project\\Caesar4_zhenzhou_xc\\web\\bin\\RMISearcherServer.bat"));
		String ss = "kkd";
		String[] array = ss.split(",");
		for(String s: array)
			System.out.println(s);
		try {
			System.out.println(ZXingCodeHandler.createQRCode("http://www.baidu.com", 300));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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