package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.SqlUtil;
import com.cn.leedane.utils.StringUtil;
import com.cn.leedane.mapper.ScoreMapper;
import com.cn.leedane.model.AttentionBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.ScoreBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.ScoreService;
/**
 * 积分service的实现类
 * @author LeeDane
 * 2016年7月12日 下午2:08:16
 * Version 1.0
 */
@Service("scoreService")
public class ScoreServiceImpl implements ScoreService<ScoreBean>{
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private ScoreMapper scoreMapper;
	
	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;

	@Override
	public int getTotalScore(int userId) {
		return SqlUtil.getTotalByList(scoreMapper.getTotalByUser(DataTableType.积分.value, userId));
	}

	@Override
	public Map<String, Object> getLimit(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("ScoreServiceImpl-->getLimit():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		String method = JsonUtil.getStringValue(jo, "method", "firstloading"); //操作方式
		int pageSize = JsonUtil.getIntValue(jo, "pageSize", ConstantsUtil.DEFAULT_PAGE_SIZE); //每页的大小
		int lastId = JsonUtil.getIntValue(jo, "last_id"); //开始的页数
		int firstId = JsonUtil.getIntValue(jo, "first_id"); //结束的页数
		StringBuffer sql = new StringBuffer();
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		//只有登录用户才能
		/*if(user == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先登录.value));
			message.put("responseCode", EnumUtil.ResponseCode.请先登录.value);
	        return message;
		}*/
		
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		//查找该用户所有的积分历史列表(该用户必须是登录用户)
		if("firstloading".equalsIgnoreCase(method)){
			sql.append("select s.id, s.score_desc, s.total_score, s.score, s.status, date_format(s.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from "+DataTableType.积分.value+" s where s.create_user_id = ? ");
			sql.append(" order by s.id desc limit 0,?");
			rs = scoreMapper.executeSQL(sql.toString(), user.getId(), pageSize);
		//下刷新
		}else if("lowloading".equalsIgnoreCase(method)){
			sql.append("select s.id, s.score_desc, s.total_score, s.score, s.status, date_format(s.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from "+DataTableType.积分.value+" s where s.create_user_id = ? ");
			sql.append(" and s.id < ? order by s.id desc limit 0,? ");
			rs = scoreMapper.executeSQL(sql.toString(), user.getId(), lastId, pageSize);
		//上刷新
		}else if("uploading".equalsIgnoreCase(method)){
			sql.append("select s.id, s.score_desc, s.total_score, s.score, s.status, date_format(s.create_time,'%Y-%c-%d %H:%i:%s') create_time ");
			sql.append(" from "+DataTableType.积分.value+" s where s.create_user_id = ? ");
			sql.append(" and s.id > ? limit 0,?  ");
			rs = scoreMapper.executeSQL(sql.toString() , user.getId(), firstId, pageSize);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取积分记录").toString(), "getLimit()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("message", rs);
		message.put("isSuccess", true);
		return message;		
	}

	@Override
	public boolean save(ScoreBean t) {
		logger.info("ScoreServiceImpl-->save():jsonObject=" +t);
		return scoreMapper.save(t) > 0;
	}
}
