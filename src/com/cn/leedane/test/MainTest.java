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

import net.sf.json.JSONObject;

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
		/*String str = "@d哈 nihao @天天让人  @123";
		Set<String> set = StringUtil.getAtUserName(str);
		if(set != null && set.size()> 0){
			for(String s: set){
				System.out.println(s);
			}
		}*/
		
		/*long length = 6235000;
		 java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");
         double d = length / 1024.00/ 1024.00;
         String size = df.format(d) +"M";
         
		System.out.println(size);*/
		/*String value = " @";
		//只有一个@字符启动好友选择
        if(value.equals("@")){
        	System.out.println("sss");
        }

        if(value.endsWith(" @")){
        	System.out.println("aasss");
        }*/
		
		//Pattern p=Pattern.compile("\\[([^\\[\\]]+)\\]");
		/*Pattern pattern = Pattern.compile("\\#([^\\[\\]]+)\\#");
		String heheString = "#23#";
		Matcher matcher = pattern.matcher(heheString);
        while (matcher.find()) {
                String group0 = matcher.group().trim();;
                System.out.println(group0);
        }*/
		
		System.out.println(getInt());
	}
	
	private static int getInt(){
		int i = 80;
		if(i > 0)
			return 10;
		return 0;
		
	}
}