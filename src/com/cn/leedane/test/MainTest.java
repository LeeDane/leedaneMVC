package com.cn.leedane.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.cn.leedane.utils.DateUtil;

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
		String string = "发货及支付空间设计师科技馆";
		StringBuffer buffer = new StringBuffer(string);
		String bStart = "<b>";
		buffer.insert(start, bStart);
		String bEnd = "</b>";
		buffer.insert(start + bStart.length() + 2, bEnd);
		System.out.println(buffer.toString());
	}
	
	private static int getInt(){
		int i = 80;
		if(i > 0)
			return 10;
		return 0;
		
	}
}