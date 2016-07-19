package com.cn.leedane.mapper;

import java.util.List;

import com.cn.leedane.model.CrawlBean;
/**
 * 爬虫mapper接口类
 * @author LeeDane
 * 2016年7月12日 上午11:09:54
 * Version 1.0
 */
public interface CrawlMapper extends BaseMapper<CrawlBean>{
	
	/**
	 * 判断该url是否已经存在
	 * @param url
	 * @return
	 */
	public boolean isExists(String url);
	
	/**
	 * 找出所有没有爬取的数据链接
	 * @param limit 现在的数量,等于0表示全部
	 * @param source 来源，空表示全部
	 * @return
	 */
	public List<CrawlBean> findAllNotCrawl(int limit, String source);
	
	/**
	 * 找出所有没有爬取的热门数据链接,根据score从大到小
	 * @param limit 现在的数量,等于0表示全部
	 * @return
	 */
	public List<CrawlBean> findAllHotNotCrawl(int limit);
	
	/**
	 * 更新所有的score
	 * @return
	 */
	//public boolean updateAllScore();
}
