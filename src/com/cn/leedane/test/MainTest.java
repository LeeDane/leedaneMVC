package com.cn.leedane.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.filter.function.inListFunction;

import net.sf.json.JSONObject;

import com.cn.leedane.model.MoodBean;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.utils.BeanUtil;

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
		
		//支出大类
		/*Category categorySpend = new Category();
		List<ParentGategory> parentGategories = new ArrayList<ParentGategory>();
		
		ParentGategory parentGategory1 = new ParentGategory();
		parentGategory1.setId(101);
		parentGategory1.setValue("食品酒水");
		parentGategories.add(parentGategory1);
		List<SubCategory> subCategories1 = new ArrayList<SubCategory>();
		parentGategory1.setSubCategories(subCategories1);
		subCategories1.add(new SubCategory("早午晚餐", 1));
		subCategories1.add(new SubCategory("烟酒茶", 2));
		subCategories1.add(new SubCategory("水果零食", 3));
		
		ParentGategory parentGategory2 = new ParentGategory();
		parentGategory2.setId(102);
		parentGategory2.setValue("衣服饰品");
		parentGategories.add(parentGategory2);
		List<SubCategory> subCategories2 = new ArrayList<SubCategory>();
		parentGategory2.setSubCategories(subCategories2);
		subCategories2.add(new SubCategory("衣服裤子", 4));
		subCategories2.add(new SubCategory("鞋帽包包", 5));
		subCategories2.add(new SubCategory("化妆饰品", 6));
		
		ParentGategory parentGategory3 = new ParentGategory();
		parentGategory3.setId(103);
		parentGategory3.setValue("居家物业");
		parentGategories.add(parentGategory3);
		List<SubCategory> subCategories3 = new ArrayList<SubCategory>();
		parentGategory3.setSubCategories(subCategories3);
		subCategories3.add(new SubCategory("日常用品", 7));
		subCategories3.add(new SubCategory("水电煤气", 8));
		subCategories3.add(new SubCategory("房租", 9));
		subCategories3.add(new SubCategory("物业管理", 10));
		subCategories3.add(new SubCategory("维修保养", 11));
		
		ParentGategory parentGategory4 = new ParentGategory();
		parentGategory4.setId(104);
		parentGategory4.setValue("行车交通");
		parentGategories.add(parentGategory4);
		List<SubCategory> subCategories4 = new ArrayList<SubCategory>();
		parentGategory4.setSubCategories(subCategories4);
		subCategories4.add(new SubCategory("公共地铁", 12));
		subCategories4.add(new SubCategory("打车租车", 13));
		subCategories4.add(new SubCategory("私家车费用", 14));
		
		ParentGategory parentGategory5 = new ParentGategory();
		parentGategory5.setId(105);
		parentGategory5.setValue("交流通讯");
		parentGategories.add(parentGategory5);
		List<SubCategory> subCategories5 = new ArrayList<SubCategory>();
		parentGategory5.setSubCategories(subCategories5);
		subCategories5.add(new SubCategory("座机费", 15));
		subCategories5.add(new SubCategory("手机话费", 16));
		subCategories5.add(new SubCategory("上网费", 17));
		subCategories5.add(new SubCategory("邮寄费", 18));
		
		ParentGategory parentGategory6 = new ParentGategory();
		parentGategory6.setId(106);
		parentGategory6.setValue("休闲娱乐");
		parentGategories.add(parentGategory6);
		List<SubCategory> subCategories6 = new ArrayList<SubCategory>();
		parentGategory6.setSubCategories(subCategories6);
		subCategories6.add(new SubCategory("运动健身", 19));
		subCategories6.add(new SubCategory("腐败聚会", 20));
		subCategories6.add(new SubCategory("休闲玩乐", 21));
		subCategories6.add(new SubCategory("宠物宝贝", 22));
		subCategories6.add(new SubCategory("旅游度假", 23));
		
		ParentGategory parentGategory7 = new ParentGategory();
		parentGategory7.setId(107);
		parentGategory7.setValue("学习进修");
		parentGategories.add(parentGategory7);
		List<SubCategory> subCategories7 = new ArrayList<SubCategory>();
		parentGategory7.setSubCategories(subCategories7);
		subCategories7.add(new SubCategory("书报杂志", 24));
		subCategories7.add(new SubCategory("培训进修", 25));
		subCategories7.add(new SubCategory("数码装备", 26));
		
		ParentGategory parentGategory8 = new ParentGategory();
		parentGategory8.setId(108);
		parentGategory8.setValue("人情往来");
		parentGategories.add(parentGategory8);
		List<SubCategory> subCategories8 = new ArrayList<SubCategory>();
		parentGategory8.setSubCategories(subCategories8);
		subCategories8.add(new SubCategory("送礼请客", 27));
		subCategories8.add(new SubCategory("孝敬长辈", 28));
		subCategories8.add(new SubCategory("还人钱物", 29));
		subCategories8.add(new SubCategory("慈善捐助", 30));
		
		ParentGategory parentGategory9 = new ParentGategory();
		parentGategory9.setId(109);
		parentGategory9.setValue("医疗保健");
		parentGategories.add(parentGategory9);
		List<SubCategory> subCategories9 = new ArrayList<SubCategory>();
		parentGategory9.setSubCategories(subCategories9);
		subCategories9.add(new SubCategory("药品费", 31));
		subCategories9.add(new SubCategory("保健费", 32));
		subCategories9.add(new SubCategory("美容费", 33));
		subCategories9.add(new SubCategory("治疗费", 34));
		
		ParentGategory parentGategory10 = new ParentGategory();
		parentGategory10.setId(110);
		parentGategory10.setValue("金融保险");
		parentGategories.add(parentGategory10);
		List<SubCategory> subCategories10 = new ArrayList<SubCategory>();
		parentGategory10.setSubCategories(subCategories10);
		subCategories10.add(new SubCategory("银行手续", 35));
		subCategories10.add(new SubCategory("投资亏损", 36));
		subCategories10.add(new SubCategory("按揭还款", 37));
		subCategories10.add(new SubCategory("消费税收", 38));
		subCategories10.add(new SubCategory("利息支出", 39));
		subCategories10.add(new SubCategory("赔偿罚款", 40));
		
		ParentGategory parentGategory11 = new ParentGategory();
		parentGategory11.setId(111);
		parentGategory11.setValue("其他杂项");
		parentGategories.add(parentGategory11);
		List<SubCategory> subCategories11 = new ArrayList<SubCategory>();
		parentGategory11.setSubCategories(subCategories11);
		subCategories11.add(new SubCategory("其他支出", 41));
		subCategories11.add(new SubCategory("意外丢失", 42));
		subCategories11.add(new SubCategory("烂账丢失", 43));
		
		categorySpend.setParentGategories(parentGategories);*/
		
		//收入大类
		/*Category categoryIncome = new Category();
		List<ParentGategory> parentGategories2 = new ArrayList<ParentGategory>();
		
		ParentGategory parentGategory12 = new ParentGategory();
		parentGategory12.setId(112);
		parentGategory12.setValue("职业收入");
		parentGategories2.add(parentGategory12);
		List<SubCategory> subCategories12 = new ArrayList<SubCategory>();
		parentGategory12.setSubCategories(subCategories12);
		subCategories12.add(new SubCategory("工资收入", 44));
		subCategories12.add(new SubCategory("利息收入", 45));
		subCategories12.add(new SubCategory("加班收入", 46));
		subCategories12.add(new SubCategory("奖金收入", 47));
		subCategories12.add(new SubCategory("投资收入", 48));
		subCategories12.add(new SubCategory("兼职收入", 49));
		
		ParentGategory parentGategory13 = new ParentGategory();
		parentGategory13.setId(113);
		parentGategory13.setValue("其他收入");
		parentGategories2.add(parentGategory13);
		List<SubCategory> subCategories13 = new ArrayList<SubCategory>();
		parentGategory13.setSubCategories(subCategories13);
		subCategories13.add(new SubCategory("礼金收入", 50));
		subCategories13.add(new SubCategory("中奖收入", 51));
		subCategories13.add(new SubCategory("意外来钱", 52));
		subCategories13.add(new SubCategory("经营所得", 53));
		categoryIncome.setParentGategories(parentGategories2);*/
		
		/*String text = "酒水";
		int i = text.indexOf(" ");
		String s = text.substring(0, i);
		System.out.println(s);*/
		/*MoodBean moodBean = new MoodBean();
		moodBean.setCanComment(true);
		
		Map<String, Object> map = new HashMap<String, Object>();
		BeanUtil.convertBeanToMap(moodBean, map);
		System.out.println(1);*/
		/*Double double1 = 0.5667355888261354d;
		int i = (int)(double1 * 100);
		System.out.println(i);*/
		/*RedisUtil redisUtil = new RedisUtil();
		redisUtil.clearAll();*/
		/*Map<String, String[]> map = new HashMap<String, String[]>();
		String[] strs = new String[2];
		strs[0] = "11";
		strs[1] = "22";
		map.put("lee1", strs);
		JSONObject object = JSONObject.fromObject(map);
		System.out.println(object.toString());*/
		/*String date = "2017-09-01 11:11:11";
		System.out.println(date.substring(0, 10));
		System.out.println(date.substring(11, date.length()));
		
		List<String> strs = new ArrayList<String>();
		strs.add("09");
		strs.add("21");
		strs.add("05");
		strs.add("13");
		strs.add("20");
		strs.add("08");
		strs.add("14");
		strs.add("09");
		strs.add("01");
		
		int max = Integer.parseInt(strs.get(0));
		int min = Integer.parseInt(strs.get(0));
		for(String s: strs){
			max = Math.max(max, Integer.parseInt(s));
			min = Math.min(min, Integer.parseInt(s));
		}
		
		System.out.println("max:"+max);
		System.out.println("min:"+min);*/
		/*List<String> datasList = new ArrayList<String>();
		datasList.add("test1");
		datasList.add("test2");
		datasList.add("test3");
		datasList.add("test4");
		datasList.add("test5");
		datasList.add("test6");
		datasList.add("test7");
		for(int i = 0; i < datasList.size(); i++){
			if(i == 2){
				datasList.
			}
		}
		datasList.remove(2);
		datasList.add(2, "test18");
		//String string = datasList.get(2);
		//string = "test18";
		System.out.println(datasList.get(2));*/
		/*final String ssString;
		int i = 0;
		if( i == 0 ){
			ssString = "kk2";
		}else {
			ssString = "kk";
		}
		System.err.println(ssString);*/
		/*Set<Integer> set = new HashSet<Integer>();
		set.add(100);
		set.add(10);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("inserts", set);
		
		System.out.println(jsonObject.toString());*/
		String s = "http://103.229.124.237:8080/SungoalApp/app/getJsonData.do";
		if(s != null){
			int lastIndex = s.lastIndexOf("/");
			s = s.substring(0, lastIndex) + "/BillCheckServlet.do";
		}
		System.out.println(s);
		
	}
}