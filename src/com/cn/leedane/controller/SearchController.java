package com.cn.leedane.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.leedane.lucene.solr.BlogSolrHandler;
import com.cn.leedane.lucene.solr.MoodSolrHandler;
import com.cn.leedane.lucene.solr.UserSolrHandler;
import com.cn.leedane.model.BlogBean;
import com.cn.leedane.model.MoodBean;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.MoodService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;

/**
 * 搜索相关的controller处理类
 * @author LeeDane
 * 2016年12月21日 上午11:40:40
 * Version 1.0
 */
@Controller
@RequestMapping("/leedane/search")
public class SearchController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MoodService<MoodBean> moodService;
	
	@Autowired
	private BlogService<BlogBean> blogService;
	
	/**
	 * 执行搜索
	 */
	@RequestMapping(value="/get", method=RequestMethod.POST)
	public String execute(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			JSONObject jsonObject = getJsonFromMessage(message);
			//查询的类型，目前支持0、全部，1、博客（正文和标题），2、说说(正文)，3、用户(姓名，中文名，邮件，手机号码，证件号码)
			int type = JsonUtil.getIntValue(jsonObject, "type", 0);
			String keyword = JsonUtil.getStringValue(jsonObject, "keyword"); //搜索关键字
			//String platform = JsonUtil.getStringValue(jsonObject, "platform", "web");//平台名称
			if(StringUtil.isNull(keyword)){
				message.put("message", "请检索关键字为空");
				printWriter(message, response, start);
				return null;
			}
			List<Map<String, Object>> responses = new ArrayList<Map<String, Object>>();
			List<Integer> tempIds = new ArrayList<Integer>();
			//获取全部
			if(type == 0){
				tempIds.add(ConstantsUtil.SEARCH_TYPE_BLOG);
				tempIds.add(ConstantsUtil.SEARCH_TYPE_MOOD);
				tempIds.add(ConstantsUtil.SEARCH_TYPE_USER);
			}else{
				tempIds.add(type);
			}
			//启动多线程
			List<Future<Map<String, Object>>> futures = new ArrayList<Future<Map<String, Object>>>();
			SingleSearchTask searchTask;
			//派发5个线程执行
			ExecutorService threadpool = Executors.newFixedThreadPool(5);
			for(int tempId: tempIds){
				searchTask = new SingleSearchTask(tempId, keyword, 0);
				futures.add(threadpool.submit(searchTask));
			}
			threadpool.shutdown();
			
			for(int i = 0; i < futures.size() ;i++){
				try {
					//保存每次请求执行后的结果
					if(futures.get(i).get() != null)
							responses.add(futures.get(i).get());
				} catch (InterruptedException e) {
					e.printStackTrace();
					futures.get(i).cancel(true);
				} catch (ExecutionException e) {
					e.printStackTrace();
					futures.get(i).cancel(true);
				}
			}
			
			List<Map<String, List<Map<String, Object>>>> rs = new ArrayList<Map<String,List<Map<String,Object>>>>();
			for(Map<String, Object> response1: responses){
				int tempId = StringUtil.changeObjectToInt(response1.get("tempId"));
				QueryResponse response2 = (QueryResponse) response1.get("queryResponse");
				SolrDocumentList documentList= response2.getResults();
				Map<String, List<Map<String, Object>>> docsMap = new HashMap<String, List<Map<String,Object>>>();
				List<Map<String, Object>> ds = new ArrayList<Map<String,Object>>();
				 
		        for (SolrDocument solrDocument : documentList){
		        	solrDocument.removeFields("_version_");
		        	
		        	if(solrDocument.containsKey("createTime")){
		        		solrDocument.setField("createTime", DateUtil.formatLocaleTime(StringUtil.changeNotNull(solrDocument.getFieldValue("createTime")), DateUtil.DEFAULT_DATE_FORMAT));
		            }
		            
		            if(solrDocument.containsKey("registerTime")){
		            	solrDocument.setField("registerTime", DateUtil.formatLocaleTime(StringUtil.changeNotNull(solrDocument.getFieldValue("registerTime")), DateUtil.DEFAULT_DATE_FORMAT));
		            }
		            Map<String, Object> map = new HashMap<String, Object>();
		            for(Entry<String, Object> m: solrDocument.entrySet()){
		            	map.put(m.getKey(), m.getValue());
		            }
		        	 //对web平台处理高亮
					/*if("web".equalsIgnoreCase(platform) && !response2.getHighlighting().isEmpty()){
						Map<String, Map<String, List<String>>>  highlightings = response2.getHighlighting();
						Map<String, List<String>> msMap = highlightings.get(String.valueOf(tempId));
					}*/
		            /*System.out.println("id = " +solrDocument.getFieldValue("id"));
		            System.out.println("account = " +solrDocument.getFieldValue("account"));
		            System.out.println("personalIntroduction = " +solrDocument.getFieldValue("personalIntroduction"));*/
		            
		            ds.add(map);
		        }
		        docsMap.put(String.valueOf(tempId), ds);
		        rs.add(docsMap);
				//搜索得到的结果数
			    System.out.println("Find:" + documentList.getNumFound());
			}
			//System.out.println("data="+com.alibaba.fastjson.JSONArray.toJSONString(rs));
			
			//UserBean userBean = userService.findById(1);
			//UserSolrHandler.getInstance().addBean(userBean);
			//List<BlogBean> blogs = blogService.getBlogBeans("select * from t_blog where status=?", ConstantsUtil.STATUS_NORMAL);
			//BlogSolrHandler.getInstance().addBeans(blogs);
			
			//List<MoodBean> moods = moodService.getMoodBeans("select * from t_mood where status=?", ConstantsUtil.STATUS_NORMAL);
			//MoodSolrHandler.getInstance().addBeans(moods);
			message.put("data", rs);
			message.put("isSuccess", true);
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 搜索用户
	 * @return
	 */
	@RequestMapping("/user")
	public String user(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(userService.search(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 搜索心情
	 * @return
	 */
	@RequestMapping("/mood")
	public String mood(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(moodService.search(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 搜索心情
	 * @return
	 */
	@RequestMapping("/blog")
	public String blog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.search(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}     
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	
	/**
	 * 获取指定类型搜索的域（type不能为0，为0需要特殊处理）
	 * 查询的类型，目前支持0、全部，1、博客（正文和标题），2、说说(正文)，3、用户(姓名，中文名，邮件，手机号码,证件号码)
	 * @param tempId
	 * @return
	 */
	/*private String[] getSearchFields(int tempId){
		String[] array = null;
		switch (tempId) {
		case ConstantsUtil.SEARCH_TYPE_BLOG:
			array = new String[2];
			array[0] = "CONTENT";
			array[1] = "TITLE";
			break;
		case ConstantsUtil.SEARCH_TYPE_MOOD:
			array = new String[1];
			array[0] = "CONTENT";
			break;
		case ConstantsUtil.SEARCH_TYPE_USER:
			array = new String[6];
			array[0] = "ACCOUNT";
			array[1] = "CHINANAME";
			array[2] = "EMAIL";
			array[3] = "QQ";
			array[4] = "PERSONALINTRODUCTION";
			array[5] = "NATIVEPLACE";
			break;
		}
		return array;
	}*/
	
	/**
	 * 获取指定类型搜索的数量
	 * 查询的类型，目前支持0、全部，1、博客（正文和标题），2、说说(正文)，3、用户(姓名，中文名，邮件，手机号码,证件号码)
	 * @param type
	 * @return
	 */
	private int getSearchRows(int tempId){
		int rows = 0;
		switch (tempId) {
		case ConstantsUtil.SEARCH_TYPE_BLOG:
			rows = ConstantsUtil.DEFAULT_BLOG_SEARCH_ROWS;
			break;
		case ConstantsUtil.SEARCH_TYPE_MOOD:
			rows = ConstantsUtil.DEFAULT_MOOD_SEARCH_ROWS;
			break;
		case ConstantsUtil.SEARCH_TYPE_USER:
			rows = ConstantsUtil.DEFAULT_USER_SEARCH_ROWS;
			break;
		}
		return rows;
	}
	
	class SingleSearchTask implements Callable<Map<String, Object>>{
		private int tempId;
		private String keyword;
		private int start;
		public SingleSearchTask(int tempId, String keyword, int start) {
			this.tempId = tempId;
			this.keyword = keyword;
			this.start = start;
		}

		@Override
		public Map<String, Object> call() throws Exception {
			SolrQuery query = new SolrQuery();
		    query.setQuery("*"+keyword+"*"); //模糊查询
		    //query.setFields(getSearchFields(tempId));
		    //query.setSort("price", ORDER.asc);
		    query.setStart(start);
		    query.setRows(getSearchRows(tempId));
		    query.setSort("registerTime", ORDER.desc);
		    // 以下给两个字段开启了高亮
		    query.addHighlightField("account"); 
		    query.addHighlightField("personalIntroduction"); 
		    // 以下两个方法主要是在高亮的关键字前后加上html代码 
		    query.setHighlightSimplePre("<font color='red'>"); 
		    query.setHighlightSimplePost("</front>");
		    query.set("wt", "xml");
		    query.set("indent", "true");
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("tempId", tempId);
		    if(tempId == ConstantsUtil.SEARCH_TYPE_BLOG){
		    	map.put("queryResponse", BlogSolrHandler.getInstance().query(query));
		    }else if(tempId == ConstantsUtil.SEARCH_TYPE_MOOD){
		    	map.put("queryResponse", MoodSolrHandler.getInstance().query(query));
		    }else if(tempId == ConstantsUtil.SEARCH_TYPE_USER){
		    	map.put("queryResponse", UserSolrHandler.getInstance().query(query));
		    }
		    return map;
		}
	}
}
