package com.cn.leedane.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cn.leedane.model.BlogBean;
import com.cn.leedane.model.IDBean;
import com.cn.leedane.model.UserBean;

/**
 * 博客service接口类
 * @author LeeDane
 * 2016年7月12日 上午11:30:31
 * Version 1.0
 */
public interface BlogService <T extends IDBean>{
	
	/**
	 * 执行SQL对应字段的List<Map<String,Object>
	 * @param sql sql语句,参数直接写在语句中，存在SQL注入攻击de风险，慎用
	 * @param params ?对应的值
	 * @return
	 */
	public List<Map<String, Object>> executeSQL(String sql, Object ...params);
	
	public  Map<String,Object> addBlog(BlogBean blog) throws Exception;	
	
	/**
	 * 根据条件查询记录，where语句，"where"需要用户自己写
	 * @param conditions 条件的字符串
	 * @return
	 */
	public List<Map<String, Object>> searchBlog(String conditions);
	
	/**
	 * 查找一条博客信息(跟findById的区别是字段可以自定义显示，去掉冗余字段)
	 * @param id
	 */
	public List<Map<String,Object>> getOneBlog(int id);
	public Map<String,Object> getIndexBlog(int start,int end,String showType);
	public List<BlogBean> managerAllBlog();	
	/**
	 * 获得全部的博客总数
	 * @return
	 */
	public int getTotalNum();
	
	public int getZanNum(int Bid);//获得赞的总数
	
	public int getCommentNum(int Bid);//获得评论的总数
	
	public int getSearchBlogTotalNum(String conditions,String conditionsType);  //按照条件和条件的类型搜索符合条件的博客的数量
	public List<BlogBean> SearchBlog(int start,int end ,String conditions,String conditionsType); //按照条件和条件的类型分页搜索符合条件的博客
	
	
	public void addReadNum(BlogBean blog);//此处不必有返回值
	public int getReadNum(int Bid);//根据博客Id获得该博客的阅读次数
	
	/**
	 * 根据num的值，如5表示获取最新5条，负数表示获得全部
	 * @param start,@param end
	 * @return
	 */
	public List<BlogBean> getLatestBlog(int start , int end);
	
	/**
	 * 根据最后一条博客的ID取得指定num条数的博客
	 * @param lastBlogId 上次去的博客结果集中最后一条博客的ID,注意：当值小于1将取最新的num条
	 * @param num  需要获取的博客条数
	 * @return
	 */
	public List<Map<String, Object>> getLatestBlogById(int lastBlogId,int num);
	
	/**
	 * 获得最热门的size条记录
	 * @param size 条数
	 * @return
	 */
	public List<Map<String, Object>> getHotestBlogs(int size);
	
	/**
	 * 获得最热门博客的size条记录
	 * @param size  条数,默认是5条
	 * @return
	 */
	public List<Map<String, Object>> getNewestBlogs(int size);
	
	/**
	 * 获得推荐博客的size条记录
	 * @param size  条数,默认是5条
	 * @return
	 */
	public List<Map<String, Object>> getRecommendBlogs(int size);
	
	/**
	 * 更新阅读的数量
	 * @param num 数量
	 * @param id 博客的id
	 * @return
	 */
	public int updateReadNum(int id, int num);
	

	/**
	 * 通过Id删除相应的博客
	 * @param jo
	 * @param request
	 * @param user
	 * @return
	 */
	public Map<String, Object> deleteById(JSONObject jo, HttpServletRequest request, UserBean user);

	/**
	 * 搜索博客
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> search(JSONObject jo, UserBean user, HttpServletRequest request);

	/**
	 * 添加标签
	 * @param jo
	 * @param user
	 * @param request
	 * @return
	 */
	public Map<String, Object> addTag(JSONObject jo, UserBean user, HttpServletRequest request);
}
