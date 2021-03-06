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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.model.BlogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.rabbitmq.SendMessage;
import com.cn.leedane.rabbitmq.send.AddReadSend;
import com.cn.leedane.rabbitmq.send.ISend;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.JsoupUtil;
import com.cn.leedane.utils.OptionUtil;
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
		long start = System.currentTimeMillis();
		try{
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			JSONObject json = getJsonFromMessage(message);
			
			//获取登录用户，获取不到用管理员账号
			UserBean user = getUserFromMessage(message);
			if(user == null)
				user = OptionUtil.adminUser;
			
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
			
			//非管理员发布的文章需要审核
			int status = JsonUtil.getIntValue(json, "status");
			if(!user.isAdmin() && status == ConstantsUtil.STATUS_NORMAL){
				status = ConstantsUtil.STATUS_AUDIT;
			}
			blog.setStatus(status);
			
			blog.setCanComment(JsonUtil.getBooleanValue(json, "can_comment"));
			blog.setCanTransmit(JsonUtil.getBooleanValue(json, "can_transmit"));
			blog.setCategory(JsonUtil.getStringValue(json, "category"));
			
			String imgUrl = JsonUtil.getStringValue(json, "img_url");
			String digest = "";
			if(hasImg){
				
						
				//判断是否有指定的图片，没有的话会自动解析内容中的第一张图片的src的值作为地址
				if(StringUtil.isNull(imgUrl)){
					Document h = Jsoup.parse(content);
					Elements a = h.getElementsByTag("img");
					if(a != null && a.size() > 0)
						imgUrl= a.get(0).attr("src");
				}
				
				if(StringUtil.isNotNull(imgUrl))
					//获取主图信息
					blog.setHasImg(hasImg);
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
			
			blog.setCreateUserId(JsonUtil.getIntValue(json, "create_user_id", user.getId()));
			blog.setCreateTime(new Date());
			
			if(hasDigest){
				digest = JsoupUtil.getInstance().getDigest(content, 0, 100);
			}else{
				digest = JsonUtil.getStringValue(json, "digest");
			}
			
			blog.setDigest(digest);
			if(JsonUtil.getIntValue(json, "bid") > 0){
				blog.setId(JsonUtil.getIntValue(json, "bid"));
			}
			
			message.putAll(blogService.addBlog(blog, user));   
			printWriter(message, response, start);
			return null;
		}catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 删除博客
	 * @return
	 */
	@RequestMapping("/deleteBlog")
	public String deleteBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.deleteById(getJsonFromMessage(message), request, getUserFromMessage(message)));
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
	 * 获得分页的Blog
	 * @return
	 */
	@RequestMapping("/paging")
	public String paging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
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
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time");
				sql.append(" , b.digest, b.froms, b.create_user_id, b.category, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ?  and b.id < ? order by b.id desc limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, lastId, pageSize);
				
			//上刷新
			}else if(method.equalsIgnoreCase("uploading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time ");
				sql.append(" , b.digest, b.froms, b.create_user_id, b.category, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ? and b.id > ?  limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, firstId, pageSize);
				
			//第一次刷新
			}else if(method.equalsIgnoreCase("firstloading")){
				sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time ");
				sql.append(" , b.digest, b.froms, b.create_user_id, b.category, u.account ");
				sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
				sql.append(" where b.status = ?  order by b.id desc limit 0,?");
				r = blogService.executeSQL(sql.toString(), ConstantsUtil.STATUS_NORMAL, pageSize);
			}else{
				message.put("isSuccess", false);
				message.put("message", "目前暂不支持的操作方法");
				printWriter(message, response, start);
				return null;
			}
			
			System.out.println("数量："+r.size());
			if(r.size() == 5){
				System.out.println("开始ID:"+r.get(0).get("id"));
				System.out.println("结束ID:"+r.get(4).get("id"));
			}
			message.put("isSuccess", true);
			message.put("message", r);
			printWriter(message, response, start);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 获取博客的内容
	 * @return
	 */
	@RequestMapping("/getContent")
	public String getContent(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try{
			checkParams(message, request);
			String blog_id = request.getParameter("blog_id");
			String device_width = request.getParameter("device_width");
			if(StringUtil.isNull(blog_id)) {
				printWriter(message, response, start);
				return null;
			}
			int blogId = StringUtil.changeObjectToInt(blog_id);
			
			if(blogId < 1){
				printWriter(message, response, start);
				return null;
			}
			//int blog_id = 1;
			String sql = "select content, read_number from "+DataTableType.博客.value+" where status = ? and id = ?";
			List<BlogBean> r = blogService.getBlogBeans(sql, ConstantsUtil.STATUS_NORMAL, blog_id);				
			if(r.size() == 1){
				final BlogBean bean = r.get(0);
				
				//把更新读的信息提交到Rabbitmq队列处理
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ISend send = new AddReadSend(bean);
						SendMessage sendMessage = new SendMessage(send);
						sendMessage.sendMsg();
					}
				}).start();
				
				if(StringUtil.isNotNull(bean.getContent())){
					String content = bean.getContent();
					Document contentHtml = Jsoup.parse(content);
					StringBuffer imgs = new StringBuffer();
					if(contentHtml != null){ 
						Elements elements = contentHtml.select("img");
						String imgUrl = null;
						int i = 0;
						for(Element element: elements){
							imgUrl = element.attr("src");
							imgs.append(imgUrl +";");
							//element.removeAttr("src");
							
							//添加网络链接
							if(StringUtil.isLink(imgUrl)){
								//element.attr("src", "http://7xnv8i.com1.z0.glb.clouddn.com/click_to_look_picture.png");
								element.attr("onclick", "clickImg(this, "+i+");");
								if(StringUtil.isNotNull(device_width)){
									String style = element.attr("style");
									int deviceWidth = Integer.parseInt(device_width);
									if(StringUtil.isNotNull(style)){
										Map<String, String> mapStyles = JsoupUtil.styleToMap(style);
										int oldHeight = 0, oldWidth = 0;
										if(mapStyles.containsKey("height")){
											oldHeight = StringUtil.changeObjectToInt(mapStyles.get("height").replaceAll("px", ""));
										}
										
										if(mapStyles.containsKey("width")){
											oldWidth = StringUtil.changeObjectToInt(mapStyles.get("width").replaceAll("px", ""));
										}
										
										if(oldWidth < 1){
											oldWidth = deviceWidth;
										}
										
										if(oldHeight > 0 && oldHeight < oldWidth){
											oldHeight = oldWidth / oldHeight * deviceWidth;
										}
										
										if(oldHeight < 1){
											oldHeight = deviceWidth;
										}
										
										//style样式存在，但是同时也没有宽高，系统给它一个适配屏幕的宽高
										mapStyles.put("width", "100%");
										mapStyles.put("height", oldHeight +"px");
										element.attr("style", JsoupUtil.mapToStyle(mapStyles));
									}else if(StringUtil.isNull(style)){
										int height = 0;
										String heightString = element.attr("height");
										String widthString = element.attr("width");
										int oldWidth = 0;
										if(StringUtil.isNotNull(widthString)){
											widthString = widthString.replaceAll("px", "");
											oldWidth = StringUtil.changeObjectToInt(widthString);
											if(oldWidth < 1){
												oldWidth = deviceWidth;
											}
										}else{
											oldWidth = deviceWidth;
										}
										
										int oldHeight = 0;
										if(StringUtil.isNotNull(heightString)){
											heightString = heightString.replaceAll("px", "");
											oldHeight = StringUtil.changeObjectToInt(heightString);
										}else{
											oldHeight = deviceWidth;
										}
										
										
										if(oldWidth > 0 && oldHeight >= oldWidth)
											height = oldHeight / oldWidth * deviceWidth;
										else {
											height = oldHeight;
										}
										//图片没有限制宽高，系统给它一个适配屏幕的宽高
										element.removeAttr("width");
										element.removeAttr("height");
										element.attr("style", "width: 100%;height:"+height+"px");
									}
								}
								i++;
							}
							
						}
						content = contentHtml.html();
					}
					if(imgs.toString().endsWith(";")){
						request.setAttribute("imgs", imgs.toString().substring(0, imgs.toString().length() -1));
					}else{
						request.setAttribute("imgs", imgs.toString());
					}
					request.setAttribute("device_width", device_width);
					request.setAttribute("content", content);
					blogService.updateReadNum(blogId, bean.getReadNumber());
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
	 * 获取博客的的基本信息(不包括内容)
	 * @return
	 */
	@RequestMapping("/getInfo")
	public String getInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.getInfo(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 根据博客ID获取一条博客信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getOneBlog")
	public String getOneBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try{
			checkParams(message, request);			
			message.putAll(blogService.getOneBlog(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, start);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 获得最热门的n条记录
	 * @return
	 */
	@RequestMapping("/getHotestBlogs")
	public String getHotestBlogs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			List<Map<String, Object>> ls = this.blogService.getHotestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
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
	 * 获得最新的n条记录
	 * @return
	 */
	@RequestMapping("/getNewestBlogs")
	public String getNewestBlogs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			List<Map<String, Object>> ls = this.blogService.getNewestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
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
	 * 获得推荐的n条博客
	 * @return
	 */
	@RequestMapping("/getRecommendBlogs")
	public String getRecommendBlogs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			List<Map<String, Object>> ls = this.blogService.getHotestBlogs(5);
			message.put("isSuccess", true);
			message.put("message", ls);
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
	 * 获取轮播图片信息
	 * @return
	 */
	@RequestMapping("/getCarouselImgs")
	public String getCarouselImgs(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try{
			if(!checkParams(message, request)){
				printWriter(message, response, start);
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
				printWriter(message, response, start);
				return null;
			}
			
			message.put("isSuccess", true);
			message.put("message", r);
			printWriter(message, response, start);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 添加标签
	 * @return
	 */
	@RequestMapping("/addTag")
	public String addTag(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.addTag(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	
	@RequestMapping("/managerBlog")
	public String managerBlog(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		printWriter(message, response, start);
		return null;
	}
	
	/**
	 * 查看草稿列表
	 * @return
	 */
	@RequestMapping("/draftList")
	public String draftList(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.draftList(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 编辑文章
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.edit(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 未审核文章列表
	 * @return
	 */
	@RequestMapping("/noCheckPaging")
	public String noCheckPaging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.noCheckPaging(getJsonFromMessage(message), getUserFromMessage(message), request));
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
	 * 审核文章（管理员）
	 * @return
	 */
	@RequestMapping("/check")
	public String check(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(blogService.check(getJsonFromMessage(message), getUserFromMessage(message), request));
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
}
