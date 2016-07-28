package com.cn.leedane.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.model.BlogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.JsoupUtil;
import com.cn.leedane.utils.StringUtil;

@Controller
@RequestMapping("/leedane/blog")
public class BlogController extends BaseController{

	protected final Log log = LogFactory.getLog(BlogController.class);
	
	/**
	 * 系统级别的缓存对象
	 */
	@Autowired
	private SystemCache systemCache;
	
	
	private BlogBean blog;//博客实体
		
	@Autowired
	private BlogService<BlogBean> blogService;


	/**
	 * 发布博客
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/releaseBlog")
	public String releaseBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try{
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			JSONObject json = getJsonFromMessage(message);
			String adminId = (String) systemCache.getCache("admin-id");
			int aid = 1;
			if(!StringUtil.isNull(adminId)){
				aid = Integer.parseInt(adminId);
			}
			UserBean user = userService.findById(aid);
			
			/**
			 * 是否有主图
			 */
			boolean hasImg = JsonUtil.getBooleanValue(json, "has_img");
			
			/**
			 * 是否要自动截取摘要摘要
			 */
			boolean hasDigest = JsonUtil.getBooleanValue(json, "has_digest");
			blog = new BlogBean();
			blog.setTitle(JsonUtil.getStringValue(json, "title"));
			String content = JsonUtil.getStringValue(json, "content");
			blog.setContent(content);
			blog.setTag(JsonUtil.getStringValue(json, "tag"));
			blog.setFroms(JsonUtil.getStringValue(json, "froms"));
			blog.setStatus(JsonUtil.getIntValue(json, "status"));
			String imgUrl = JsonUtil.getStringValue(json, "img_url");
			String digest = "";
			if(hasImg){
				//获取主图信息
				blog.setHasImg(hasImg);
						
				//判断是否有指定的图片，没有的话会自动解析内容中的第一张图片的src的值作为地址
				if(StringUtil.isNull(imgUrl)){
					Document h = Jsoup.parse(content);
					Elements a = h.getElementsByTag("img");
					imgUrl= a.get(0).attr("src");
				}
				
				blog.setImgUrl(imgUrl);
				
				/**
				 * 非链接的字符串
				 */
				if(!StringUtil.isLink(imgUrl)){
					JsoupUtil.getInstance().base64ToLink(imgUrl, user.getAccount());
					
				}
				//将base64位的图片保存在数据在本地
				/*String path = "F:/upload";
				File file = new File(path);
				System.out.println("==="+path);
				if(!file.exists()){
					file.mkdir();
				}
				String account = user != null ? user.getAccount() : "admin";
				String filePath = path + "/" + account +"_"+System.currentTimeMillis()+ (int)Math.random()*1000 +".png";
				System.out.println(ImageUtil.convertBase64ToImage(filePath, imgUrl));*/
				
			}
			
			blog.setCreateUserId(user.getId());
			blog.setCreateTime(new Date());
			
			if(hasDigest){
				digest = JsoupUtil.getInstance().getDigest(content, 0, 100);
			}else{
				digest = JsonUtil.getStringValue(json, "digest");
			}
			
			blog.setDigest(digest);
			message.putAll(blogService.addBlog(blog));   
			printWriter(message, response);
			return null;
		}catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 删除博客
	 * @return
	 */
	@RequestMapping("/deleteBlog")
	public String deleteBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(blogService.deleteById(getJsonFromMessage(message), request, getUserFromMessage(message)));
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获得分页的Blog
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try{
			checkParams(message, request);
			
			JSONObject json = getJsonFromMessage(message);
			int pageSize = JsonUtil.getIntValue(json, "pageSize"); //每页的大小
			int lastId = JsonUtil.getIntValue(json, "last_id");
			int firstId = JsonUtil.getIntValue(json, "first_id");
			
			//执行的方式，现在支持：uploading(向上刷新)，lowloading(向下刷新)，firstloading(第一次刷新)
			String method = JsonUtil.getStringValue(json, "method");
			List<Map<String,Object>> r = new ArrayList<Map<String,Object>>();
			StringBuffer sql = new StringBuffer();
			System.out.println("执行的方式是："+method +",pageSize:"+pageSize+",lastId:"+lastId+",firstId:"+firstId);
			//下刷新
			if(method.equalsIgnoreCase("lowloading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time");
				sql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ?  and b.id < ? order by b.id desc limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
				
			//上刷新
			}else if(method.equalsIgnoreCase("uploading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ? and b.id > ?  limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
				
			//第一次刷新
			}else if(method.equalsIgnoreCase("firstloading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
				sql.append(" , b.digest, b.froms, b.create_user_id, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ?  order by b.id desc limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, pageSize);
			}else{
				message.put("isSuccess", false);
				message.put("message", "目前暂不支持的操作方法");
				printWriter(message, response);
				return null;
			}
			
			System.out.println("数量："+r.size());
			if(r.size() == 5){
				System.out.println("开始ID:"+r.get(0).get("id"));
				System.out.println("结束ID:"+r.get(4).get("id"));
			}
			message.put("isSuccess", true);
			message.put("message", r);
			printWriter(message, response);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获取博客的内容
	 * @return
	 */
	@RequestMapping("/getContent")
	public String getContent(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		System.out.println("dddd");
		try{
			checkParams(message, request);
			String blogId = request.getParameter("blog_id");
			if(StringUtil.isNull(blogId)) {
				printWriter(message, response);
				return null;
			}
			int blog_id = Integer.parseInt(blogId);
			
			if(blog_id < 1){
				printWriter(message, response);
				return null;
			}
			//int blog_id = 1;
			String sql = "select content, read_number from "+DataTableType.博客.value+" where status = ? and id = ?";
			List<Map<String,Object>> r = blogService.executeSQL(sql, ConstantsUtil.STATUS_NORMAL, blog_id);				
			if(r.size() == 1){
				Map<String,Object> map = r.get(0);
				//更新读取数量
				int readNum = StringUtil.changeObjectToInt(map.get("read_number"));
				blogService.updateReadNum(blog_id, readNum + 1);
				if(map.containsKey("content")){
					request.setAttribute("content", map.get("content"));
					return "content-page";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("content", "抱歉,服务器获取博客失败");
		return "content-page";
		
	}
	
	/**
	 * 根据博客ID获取一条博客信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getOneBlog")
	public String getOneBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try{
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			JSONObject json = getJsonFromMessage(message);
			String id = JsonUtil.getStringValue(json, "blog_id");
			if(StringUtil.isNull(id)){
				message.put("message", "博客ID不能为空");
			}else{
				List<Map<String,Object>> ls = blogService.getOneBlog(Integer.parseInt(id));
				if(ls.size() == 0){
					message.put("message", "该博客不存在");
				}else if(ls.size() == 1){				
					int readNum = StringUtil.changeObjectToInt(ls.get(0).get("read_number"));
					int b_id = StringUtil.changeObjectToInt(ls.get(0).get("id"));
					int i = blogService.updateReadNum(b_id, readNum + 1);
					
					message.put("message", ls);
					message.put("isSuccess", true);
					printWriter(message, response);
					return null;
				}else{
					message.put("message", "数据库数据有误");
				}
			}
			printWriter(message, response);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获得最热门的n条记录
	 * @return
	 */
	@RequestMapping("/getHotestBlogs")
	public String getHotestBlogs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ls = this.blogService.getHotestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获得最新的n条记录
	 * @return
	 */
	@RequestMapping("/getNewestBlogs")
	public String getNewestBlogs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ls = this.blogService.getNewestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获得推荐的n条博客
	 * @return
	 */
	@RequestMapping("/getRecommendBlogs")
	public String getRecommendBlogs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ls = this.blogService.getHotestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 获取轮播图片信息
	 * @return
	 */
	@RequestMapping("/getCarouselImgs")
	public String getCarouselImgs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try{
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			JSONObject json = getJsonFromMessage(message);
			int num = JsonUtil.getIntValue(json, "num"); //获取图片的数量
			//执行的方式，现在支持：simple(取最新)，hostest(取最热门的)
			String method = JsonUtil.getStringValue(json, "method");
			
			List<Map<String,Object>> r = new ArrayList<Map<String,Object>>();
			String sql = "";
			System.out.println("执行的方式是："+method +",获取的数量:"+num);
			//普通获取，取最新的图片信息，按照create_time倒序排列
			if(method.equalsIgnoreCase("simple")){
				sql = "select id,img_url,title from "+DataTableType.博客.value+" where status = " + ConstantsUtil.STATUS_NORMAL + " and img_url != '' order by create_time desc,id desc limit 0,?";
				r = blogService.executeSQL(sql, num);
				
			//按照热度，取最热门的图片信息，按照id倒序排序
			}else if(method.equalsIgnoreCase("hostest")){
				sql = "select id,img_url,title from "+DataTableType.博客.value+" where status = " + ConstantsUtil.STATUS_NORMAL + "  and img_url != '' and NOW() < DATE_ADD(create_time,INTERVAL 7 DAY) order by (comment_number*0.45 + transmit_number*0.25 + share_number*0.2 + zan_number*0.1 + read_number*0.1) desc,id desc limit 0,?";
				r = blogService.executeSQL(sql, num);	
			}else{
				message.put("isSuccess", false);
				message.put("message", "目前暂不支持的操作方法");
				printWriter(message, response);
				return null;
			}
			
			message.put("isSuccess", true);
			message.put("message", r);
			printWriter(message, response);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	/**
	 * 添加标签
	 * @return
	 */
	@RequestMapping("/addTag")
	public String addTag(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response);
				return null;
			}
			message.putAll(blogService.addTag(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response);
		return null;
	}
	
	@RequestMapping("/managerBlog")
	public String managerBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		printWriter(message, response);
		return null;
	}
}
