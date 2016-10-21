package com.cn.leedane.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.activemq.filter.function.inListFunction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.sf.json.JSONObject;

import com.cn.leedane.crawl.BaseCrawlBean;
import com.cn.leedane.model.MoodBean;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.utils.BeanUtil;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.StringUtil;
import com.sun.org.apache.regexp.internal.recompile;

/**
 * main方法相关的测试类
 * @author LeeDane
 * 2016年7月12日 下午3:12:24
 * Version 1.0
 */
public class MainTest {

	public static void main(String[] args) throws IOException {
		int start = 2;
		int end  = 3;
		String tag = "div";
		String string = "<a>发货及<div></a>支付空间rr<a>设计</a>师科技馆</div>";
		int focusIndex = string.lastIndexOf("馆");
		//从左边找a的开始索引
		String left = string.substring(0, focusIndex);
		String right = string.substring(focusIndex, string.length());
		int leftLastTagIndex = left.lastIndexOf("</a>");
		int leftFirstTagIndex = left.lastIndexOf("<a>");
		int rightFirtTagIndex = right.indexOf("<a>");
		int rightLastTagIndex = right.indexOf("</a>");
		
		if((leftLastTagIndex == -1 && leftFirstTagIndex > 0) || leftLastTagIndex < leftFirstTagIndex){
			if((rightFirtTagIndex == -1 && rightLastTagIndex > 0) || rightFirtTagIndex > rightLastTagIndex){
				System.out.println(true);
				return;
			}
		}
		System.out.println(false);
		
		//testStringBuffer();
		testImages();
	}
	
	private static void testImages() {
		String ss = "http://www.baidu.co3m/iigiggg";
		int index = ss.indexOf("http://www.baidu.com");
		System.out.println(index);
		
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