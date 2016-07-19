package com.cn.leedane.test;

import java.io.File;
import java.io.IOException;

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
		
		/*String ss = "U2FsdGVkX1+GwIYSkb6ewlmShIAAR+k1oKV87HFVoZlaCLjKUa3RsXxMIzs88xv2gvX9wXRao4SLaiOyB8113w=";
		System.out.println(Base64Util.decode(ss.getBytes()));*/
		
		/*System.out.println(DateUtil.DateToString(new Date(), "HH:mm"));
		
		String content = "你好 哈哈";
		if(content.charAt(2) == ' ')
			System.out.println(true);
		else
			System.out.println(false);
		
		String str = "[14]你好 [78]jd[提供如 []89]]";
		Set<Integer> imgIds = StringUtil.getImgIdList(str);
		for(Integer id: imgIds)
			System.out.println(id);
		
		String test1 = "123";
		String test2 = new String("123");
		System.out.println(test1 == test2);*/
		
		File f = new File("d://work");
		System.out.println(f.getCanonicalPath());
		System.out.println(f.getAbsolutePath());
		System.out.println(f.getPath());
	}	
	
	

}
