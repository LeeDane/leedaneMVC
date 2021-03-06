package com.cn.leedane.service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.leedane.handler.BlogHandler;
import com.cn.leedane.handler.CommentHandler;
import com.cn.leedane.handler.NotificationHandler;
import com.cn.leedane.handler.TransmitHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.handler.ZanHandler;
import com.cn.leedane.lucene.solr.BlogSolrHandler;
import com.cn.leedane.mapper.BlogMapper;
import com.cn.leedane.model.BlogBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.AdminRoleCheckService;
import com.cn.leedane.service.BlogService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.utils.CollectionUtil;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.EnumUtil.NotificationType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.JsoupUtil;
import com.cn.leedane.utils.LuceneUtil;
import com.cn.leedane.utils.StringUtil;

/**
 * 博客service实现类
 * @author LeeDane
 * 2016年7月12日 下午12:20:48
 * Version 1.0
 */
@Service("blogService")
public class BlogServiceImpl extends AdminRoleCheckService implements BlogService<BlogBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private BlogMapper blogMapper;
	

	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;
	
	public void setOperateLogService(
			OperateLogService<OperateLogBean> operateLogService) {
		this.operateLogService = operateLogService;
	}
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private BlogHandler blogHandler;
	
	@Autowired
	private NotificationHandler notificationHandler;
	
	@Autowired
	private ZanHandler zanHandler;
	
	@Autowired
	private CommentHandler commentHandler;
	
	@Autowired
	private TransmitHandler transmitHandler;
	
	
	@Override
	public Map<String,Object> addBlog(BlogBean blog, UserBean user) throws Exception{	
		logger.info("BlogServiceImpl-->addBlog():blog="+blog);
		Map<String,Object> message = new HashMap<String,Object>();
		message.put("isSuccess",false);
		int result = 0;
		if(blog.getId() > 0 ){
			//获取文章
			BlogBean oldBean = blogMapper.findById(BlogBean.class, blog.getId());
			if(oldBean == null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
				return message;
			}
			
			//获取文章的作者
			int createUserId = oldBean.getCreateUserId();
			if(!checkAdmin(user, createUserId)){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
				message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
				return message;
			}
			result = blogMapper.update(blog);
		}else {
			result = blogMapper.save(blog);
		}
		if(result > 0){
			BlogSolrHandler.getInstance().addBean(blog);
			message.put("isSuccess",true);
			message.put("message","文章发布成功");
		}else{
			message.put("isSuccess",false);
			message.put("message","文章发布失败");
		}
		
		return message;
	}

	@Override
	public List<Map<String, Object>> searchBlog(String conditions) {
		logger.info("BlogServiceImpl-->searchBlog():conditions="+conditions);
		return this.blogMapper.searchBlog(ConstantsUtil.STATUS_NORMAL, conditions);
	}
	


	@Override
	public Map<String, Object> shakeSearch(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->shakeSearch():jo="+jo.toString());
		int blogId = 0; //获取到的博客的ID
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		blogId = blogMapper.shakeSearch(user.getId(), ConstantsUtil.STATUS_NORMAL);
		if(blogId > 0 ){
			message.put("isSuccess", true);
			List<Map<String, Object>> rs = blogHandler.getBlogDetail(blogId, user);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(CollectionUtil.isNotEmpty(rs))
				resultMap = rs.get(0);
			message.put("message", resultMap);
			message.put("responseCode", EnumUtil.ResponseCode.请求返回成功码.value);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有更多数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有更多数据.value);
		}
			
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr("账号为", user.getAccount() , "摇一摇搜索，得到文章Id为"+ blogId, StringUtil.getSuccessOrNoStr(blogId > 0)).toString(), "shakeSearch()", StringUtil.changeBooleanToInt(blogId > 0), 0);	
		
		return message;
	}

	@Override
	public Map<String,Object> getIndexBlog(int start,int end,String showType) {
		//if
		logger.info("BlogServiceImpl-->getIndexBlog():start="+start+",end="+end+",showType="+showType);
		List<BlogBean> blogs = this.blogMapper.getMoreBlog(start, end, showType);
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(blogs.size()>0){
			message.put("isSuccess",true);
			message.put("message",blogs);	
		}else{
			//没有找到相关的文章
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
		}
		return message;
	}

	@Override
	public List<BlogBean> managerAllBlog() {
		logger.info("BlogServiceImpl-->managerAllBlog()");
		return this.blogMapper.managerAllBlog();
	}

	@Override
	public int getTotalNum() {
		logger.info("BlogServiceImpl-->getTotalNum()");
		return this.blogMapper.getTotalNum();
	}

	@Override
	public int getZanNum(int Bid) {
		logger.info("BlogServiceImpl-->getZanNum():Bid="+Bid);
		return this.blogMapper.getZanNum(Bid);
	}

	@Override
	public int getCommentNum(int Bid) {
		logger.info("BlogServiceImpl-->getCommentNum():Bid="+Bid);
		return this.blogMapper.getCommentNum(Bid);
	}

	@Override
	public int getSearchBlogTotalNum(String conditions, String conditionsType) {
		logger.info("BlogServiceImpl-->getSearchBlogTotalNum():conditions="+conditions+",conditionsType="+conditionsType);
		return this.blogMapper.getSearchBlogTotalNum(conditions, conditionsType);
	}

	/*@Override
	public List<BlogBean> SearchBlog(int start, int end, String conditions,
			String conditionsType) {
		logger.info("BlogServiceImpl-->SearchBlog():conditions="+conditions+",conditionsType="+conditionsType+",start="+start+",end="+end);
		return this.blogMapper.SearchBlog(start, end, conditions, conditionsType);
	}*/

	@Override
	public void addReadNum(BlogBean blog) {
		logger.info("BlogServiceImpl-->addReadNum():blog="+blog);
	}

	@Override
	public int getReadNum(int Bid) {
		logger.info("BlogServiceImpl-->getReadNum():Bid="+Bid);
		return this.blogMapper.getReadNum(Bid);
	}

	@Override
	public List<BlogBean> getLatestBlog(int start, int end) {
		logger.info("BlogServiceImpl-->getLatestBlog():start="+start+",end="+end);
		return this.blogMapper.getLatestBlog(start, end);
	}

	@Override
	public List<Map<String, Object>> getLatestBlogById(int lastBlogId, int num) {
		logger.info("BlogServiceImpl-->getLatestBlogById():lastBlogId="+lastBlogId+",num="+num);
		return this.blogMapper.getLatestBlogById(lastBlogId,num);
	}

	@Override
	public List<Map<String, Object>> getHotestBlogs(int size) {
		logger.info("BlogServiceImpl-->getHotestBlogs():size="+size);
		return this.blogMapper.getHotestBlogs(size);
	}

	@Override
	public int updateReadNum(int id, int num) {
		logger.info("BlogServiceImpl-->updateReadNum():id="+id+",num="+num);
		return this.blogMapper.updateSql(EnumUtil.getBeanClass(EnumUtil.getTableCNName(EnumUtil.DataTableType.博客.value)), " set read_number = ? , is_read = true where id = ?", num, id);
	}

	@Override
	public Map<String, Object> deleteById(JSONObject jo, HttpServletRequest request, UserBean user){
		int id = JsonUtil.getIntValue(jo, "b_id");
		logger.info("BlogServiceImpl-->deleteById():id="+id);
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(id < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		//获取该文章
		BlogBean oldBean = blogMapper.findById(BlogBean.class, id);
		if(oldBean == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		//获取该文章的作者
		int createUserId = oldBean.getCreateUserId();
		
		if(!checkAdmin(user, createUserId)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
			return message;
		}
		
		boolean result = this.blogMapper.deleteById(BlogBean.class, id) > 0;
		if(result){
			BlogSolrHandler.getInstance().deleteBean(String.valueOf(id));
			message.put("isSuccess", true);
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作成功.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作成功.value);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.操作失败.value);
		}
		String subject = user.getAccount() + "删除了ID为"+id + "的博客" + StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "deleteById()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}

	@Override
	public List<Map<String, Object>> getNewestBlogs(int size) {
		logger.info("BlogServiceImpl-->getNewestBlogs():size="+size);
		return this.blogMapper.getNewestBlogs(size);
	}

	@Override
	public List<Map<String, Object>> getRecommendBlogs(int size) {
		logger.info("BlogServiceImpl-->getRecommendBlogs():size="+size);
		return this.blogMapper.getRecommendBlogs(size);
	}

	@Override
	public Map<String, Object> search(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->search():jo="+jo.toString());
		String searchKey = JsonUtil.getStringValue(jo, "searchKey");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(StringUtil.isNull(searchKey)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.检索关键字不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.检索关键字不能为空.value);
			return message;
		}
		
		List<Map<String, Object>> rs = blogMapper.executeSQL("select id, img_url, title, has_img, tag, date_format(create_time,'%Y-%m-%d %H:%i:%s') create_time, digest, froms, source, create_user_id from "+DataTableType.博客.value+" where status=? and (digest like '%"+searchKey+"%' or title like '%"+searchKey+"%' or content like '%"+searchKey+"%') order by create_time desc limit 25", ConstantsUtil.STATUS_NORMAL);
		if(rs != null && rs.size() > 0){
			int createUserId = 0;
			for(int i = 0; i < rs.size(); i++){
				createUserId = StringUtil.changeObjectToInt(rs.get(i).get("create_user_id"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(createUserId));
			}
		}
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}
	
	@Override
	public Map<String, Object> addTag(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->addTag():jo="+jo.toString());
		int bid = JsonUtil.getIntValue(jo, "bid");
		String tag = JsonUtil.getStringValue(jo, "tag");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(bid < 1 || StringUtil.isNull(tag)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		if(tag.length() > 5){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.标签长度不能超过5位.value));
			message.put("responseCode", EnumUtil.ResponseCode.标签长度不能超过5位.value);
			return message;
		}
		
		BlogBean blogBean = blogMapper.findById(BlogBean.class, bid);
		
		if(blogBean == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		boolean cut = false;
		String oldTag = blogBean.getTag();
		if(StringUtil.isNotNull(oldTag)){
			String[] oldArray = oldTag.split(",");
			StringBuffer b = new StringBuffer();
			if(oldArray.length > 2){
				cut = true;
				for(int i = 1; i < 3; i++){
					b.append(oldArray[i]);
					b.append(",");
				}
				tag = b.toString() +tag;
			}else{
				tag = oldTag +"," +tag;
			}
		}
		
		blogBean.setTag(tag);
		
		boolean result = blogMapper.update(blogBean) > 0;
		
		String subject = user.getAccount() + "为博客ID为"+bid + "添加标签" +tag + StringUtil.getSuccessOrNoStr(result);
		this.operateLogService.saveOperateLog(user, request, new Date(), subject, "addTag()", ConstantsUtil.STATUS_NORMAL, 0);
		
		if(result){
			if(cut){
				message.put("message", "添加成功，标签数量超过3个，已自动删掉第一个");
			}else{
				BlogSolrHandler.getInstance().updateBean(blogBean);
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.标签添加成功.value));
			}
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		
		message.put("isSuccess", result);
		return message;
	}

	@Override
	public List<Map<String, Object>> executeSQL(String sql, Object... params){
		return blogMapper.executeSQL(sql, params);
	}

	@Override
	public List<BlogBean> getBlogBeans(String sql, Object... params) {
		return  blogMapper.getBeans(sql, params);
	}

	@Override
	public Map<String, Object> getInfo(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->getInfo():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		int blogId = JsonUtil.getIntValue(jo, "blog_id");
		
		if(blogId < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		//String sql = "select content, read_number from "+DataTableType.博客.value+" where status = ? and id = ?";
		StringBuffer sql = new StringBuffer();
		sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time");
		sql.append(" , b.digest, b.froms, b.create_user_id, u.account, b.can_comment, b.can_transmit, b.origin_link, b.source, b.category ");
		sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
		sql.append(" where b.status = ?  and b.id = ? ");
		
		List<BlogBean> r = getBlogBeans(sql.toString(), ConstantsUtil.STATUS_NORMAL, blogId);				
		if(r.size() == 1){
			message.put("message", r.get(0));
			message.put("isSuccess", true);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库对象数量不符合要求.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库对象数量不符合要求.value);
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取文章ID为：", blogId, ",的基本信息", StringUtil.getSuccessOrNoStr(r.size() == 1)).toString(), "getInfo()", ConstantsUtil.STATUS_NORMAL, 0);
		
		return message;
	}

	@Override
	public Map<String, Object> draftList(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->draftList():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String, Object> message = new HashMap<String, Object>();		
		StringBuffer sql = new StringBuffer();
		sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time");
		sql.append(" , b.digest, b.froms, b.content, b.can_comment, b.can_transmit, b.origin_link, b.source, b.category");
		sql.append(" from "+DataTableType.博客.value+" b ");
		sql.append(" where status = ? and create_user_id = ? order by id desc ");
		
		List<Map<String, Object>> r = blogMapper.executeSQL(sql.toString(), ConstantsUtil.STATUS_DRAFT, user.getId());				
		message.put("message", r);
		message.put("isSuccess", true);
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取草稿列表", StringUtil.getSuccessOrNoStr(r.size() == 1)).toString(), "draftList()", ConstantsUtil.STATUS_NORMAL, 0);
		
		return message;
	}

	@Override
	public Map<String, Object> edit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->edit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		
		int blogId = JsonUtil.getIntValue(jo, "blog_id");
		Map<String,Object> message = new HashMap<String,Object>();
		message.put("isSuccess", false);
		if(blogId < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		//获取该文章
		StringBuffer sql = new StringBuffer();
		sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time");
		sql.append(" , b.digest, b.froms, b.content, b.can_comment, b.can_transmit, b.origin_link, b.source, b.category, b.create_user_id");
		sql.append(" from "+DataTableType.博客.value+" b ");
		sql.append(" where id = ? and status=?");
		
		List<Map<String, Object>> r = blogMapper.executeSQL(sql.toString(), blogId, ConstantsUtil.STATUS_NORMAL);
		
		if(r == null || r.size() != 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		//获取该文章的作者
		int createUserId = StringUtil.changeObjectToInt(r.get(0).get("create_user_id"));
		if(!user.isAdmin() && (createUserId < 1 || createUserId != user.getId())){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有操作权限.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有操作权限.value);
			return message;
		}
		
		message.put("message", r);
		message.put("isSuccess", true);
		message.put("responseCode", EnumUtil.ResponseCode.请求返回成功码.value);
		//BlogSolrHandler.getInstance().updateBean(blogBean);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取编辑博客Id为：", blogId, StringUtil.getSuccessOrNoStr(r.size() == 1)).toString(), "edit()", ConstantsUtil.STATUS_NORMAL, 0);
		
		return message;
	}

	@Override
	public Map<String, Object> noCheckPaging(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->noCheckPaging():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String,Object> message = new HashMap<String,Object>();
		message.put("isSuccess", false);
		if(!user.isAdmin()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请用管理员账号登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请用管理员账号登录.value);
			return message;
		}
		
		int pageSize = JsonUtil.getIntValue(jo, "pageSize"); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id");
		int firstId = JsonUtil.getIntValue(jo, "first_id");

		//执行的方式，现在支持：uploading(向上刷新)，lowloading(向下刷新)，firstloading(第一次刷新)
		String method = JsonUtil.getStringValue(jo, "method");
		
		List<Map<String,Object>> r = new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();
		//下刷新
		if(method.equalsIgnoreCase("lowloading")){
			sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time");
			sql.append(" , b.digest, b.froms, b.create_user_id, b.category, u.account ");
			sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
			sql.append(" where b.status = ?  and b.id < ? order by b.id desc limit 0,?");
			r = executeSQL(sql.toString(), ConstantsUtil.STATUS_AUDIT, lastId, pageSize);
			
		//上刷新
		}else if(method.equalsIgnoreCase("uploading")){
			sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time ");
			sql.append(" , b.digest, b.froms, b.create_user_id, b.category, u.account ");
			sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
			sql.append(" where b.status = ? and b.id > ?  limit 0,?");
			r = executeSQL(sql.toString(), ConstantsUtil.STATUS_AUDIT, firstId, pageSize);
			
		//第一次刷新
		}else if(method.equalsIgnoreCase("firstloading")){
			sql.append("select b.id, b.img_url, b.title, b.has_img, b.tag, date_format(b.create_time,'%Y-%m-%d %H:%i:%s') create_time ");
			sql.append(" , b.digest, b.froms, b.create_user_id, b.category, u.account ");
			sql.append(" from "+DataTableType.博客.value+" b inner join "+DataTableType.用户.value+" u on b.create_user_id = u.id ");
			sql.append(" where b.status = ?  order by b.id desc limit 0,?");
			r = executeSQL(sql.toString(), ConstantsUtil.STATUS_AUDIT, pageSize);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.目前暂不支持的操作方法.value));
			message.put("responseCode", EnumUtil.ResponseCode.目前暂不支持的操作方法.value);
			return message;
		}

		message.put("isSuccess", true);
		message.put("message", r);
		message.put("responseCode", EnumUtil.ResponseCode.请求返回成功码.value);

		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取未审核文章列表，查询条件是："+jo.toString(), StringUtil.getSuccessOrNoStr(r.size() == 1)).toString(), "noCheckPaging()", ConstantsUtil.STATUS_NORMAL, 0);	
		return message;
	}

	@Override
	public Map<String, Object> check(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("BlogServiceImpl-->check():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		Map<String,Object> message = new HashMap<String,Object>();
		message.put("isSuccess", false);
		if(!user.isAdmin()){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请用管理员账号登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请用管理员账号登录.value);
			return message;
		}
		
		int blogId = JsonUtil.getIntValue(jo, "blog_id"); //文章ID
		boolean agree = JsonUtil.getBooleanValue(jo, "agree");
		BlogBean bean = blogMapper.findById(BlogBean.class, blogId);
		if(bean == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		if(bean.getStatus() != ConstantsUtil.STATUS_AUDIT){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该文章不需要审核.value));
			message.put("responseCode", EnumUtil.ResponseCode.该文章不需要审核.value);
			return message;
		}
		
		String toUserContent = JsonUtil.getStringValue(jo, "reason");
		if(!agree && StringUtil.isNull(toUserContent)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		if(agree){
			bean.setStatus(ConstantsUtil.STATUS_NORMAL);
			toUserContent = "您的文章《"+ bean.getTitle() +"》已经被管理员审核通过！";
		}else{
			bean.setStatus(ConstantsUtil.STATUS_DELETE);
			toUserContent = "您的文章《"+ bean.getTitle() +"》被管理员审核为不通过，并且已被删除，有异议请直接私信管理员处理！";
		}
		
		boolean result = blogMapper.update(bean) > 0;
		if(result){
			//给用户发送消息
			notificationHandler.sendNotificationById(false, user, bean.getCreateUserId(), toUserContent, NotificationType.通知, "t_blog", bean.getId(), bean);
			message.put("isSuccess", true);
			message.put("message", "审核处理成功！");
			message.put("responseCode", EnumUtil.ResponseCode.请求返回成功码.value);			
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据更新失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据更新失败.value);
		}
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"审核文章Id为", blogId, StringUtil.getSuccessOrNoStr(result)).toString(), "check()", StringUtil.changeBooleanToInt(result), 0);	
		return message;
	}
	
	@Override
	public Map<String, Object> getOneBlog(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		/*logger.info("BlogServiceImpl-->getOneBlog():id="+id);
		return this.blogMapper.getOneBlog(status , id);*/
		logger.info("BlogServiceImpl-->getOneBlog():jsonObject=" +jo.toString());
		Map<String,Object> message = new HashMap<String,Object>();
		message.put("isSuccess", false);
		
		int blogId = JsonUtil.getIntValue(jo, "blog_id");
		if(blogId < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		List<Map<String, Object>> ls = blogMapper.getOneBlog(ConstantsUtil.STATUS_NORMAL, blogId);
		if(CollectionUtil.isEmpty(ls)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该博客不存在.value));
			message.put("responseCode", EnumUtil.ResponseCode.该博客不存在.value);
			return message;
		}
		
		if(ls !=null && ls.size() > 0){
			//为名字备注赋值
			for(int i = 0; i < ls.size(); i++){
				ls.get(i).put("zan_users", zanHandler.getZanUser(blogId, DataTableType.博客.value, user, 6));
				ls.get(i).put("comment_number", commentHandler.getCommentNumber(blogId, DataTableType.博客.value));
				ls.get(i).put("transmit_number", transmitHandler.getTransmitNumber(blogId, DataTableType.博客.value));
				ls.get(i).put("zan_number", zanHandler.getZanNumber(blogId, DataTableType.博客.value));
			}	
		}
		Map<String, Object> blogMap = ls.get(0);
		int readNum = StringUtil.changeObjectToInt(blogMap.get("read_number"));
		try {
			//获取文章的简要中的关键字
			String content = JsoupUtil.getInstance().getContentNoTag(StringUtil.changeNotNull(blogMap.get("content")));
			blogMap.put("keywords", CollectionUtil.getTopKeyword(LuceneUtil.getInstance().extract(content), 2, 5));
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean result = updateReadNum(blogId, readNum + 1) > 0;
		if(result){
			
			int createUserId = StringUtil.changeObjectToInt(blogMap.get("create_user_id"));
			blogMap.put("account", userHandler.getUserName(createUserId));
			message.put("message", ls);
			message.put("isSuccess", true);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr("获取博客ID为", blogId, "详情", StringUtil.getSuccessOrNoStr(result)).toString(), "check()", StringUtil.changeBooleanToInt(result), 0);	
		return message;
	}

}
