package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cn.leedane.mapper.FinancialMapper;
import com.cn.leedane.model.FinancialBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.FinancialService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;
/**
 * 记账相关service的实现类
 * @author LeeDane
 * 2016年7月22日 上午9:20:42
 * Version 1.0
 */
@Service("financialService")
public class FinancialServiceImpl implements FinancialService<FinancialBean>{
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private FinancialMapper financialMapper;
	
	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;

	@Override
	public Map<String, Object> synchronous(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->synchronous():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		List<AppFinancialBean> appFinancialBeans = getListValue(jo, "datas");
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(CollectionUtils.isEmpty(appFinancialBeans)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
		
		List<Map<String, Integer>> inserts = new ArrayList<Map<String,Integer>>();//此次同步插入的数据ID
		Set<Integer> updates = new HashSet<Integer>(); //此次同步发现数据有更新的数据ID
		Set<Integer> deletes = new HashSet<Integer>();//此次同步发现数据被删的数据ID
		int total = 0; //此次同步总共处理的数量
		
		int fId = 0;
		FinancialBean webBean;
		for(AppFinancialBean appFinancialBean: appFinancialBeans){
			fId = appFinancialBean.getId();
			//ID大于0说明是服务器上的数据
			if(fId > 0){
				webBean = financialMapper.findById(FinancialBean.class, fId);
				if(webBean == null){
					deletes.add(fId);
				}else{
					if(hasChange(appFinancialBean, webBean)){
						updates.add(fId);
					}
				}
			}else{
				FinancialBean financialBean = (FinancialBean)appFinancialBean;
				financialBean.setCreateTime(new Date());
				if(financialMapper.save(financialBean) > 0){
					Map<String, Integer> map = new HashMap<String, Integer>();
					map.put("localId", appFinancialBean.getId());
					map.put("id", financialBean.getId());
					inserts.add(map);
					total = total + 1;
				}
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("inserts", inserts);
		jsonObject.put("updates", updates);
		jsonObject.put("deletes", deletes);
		jsonObject.put("total", total);
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"同步记账数据，").toString(), "synchronous()", ConstantsUtil.STATUS_NORMAL, 0);
		//message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据同步成功.value));
		message.put("message", jsonObject);
		message.put("isSuccess", true);
		return message;
	}

	@Override
	public Map<String, Object> force(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->force():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		
		int type = JsonUtil.getIntValue(jo, "type"); //1表示强制以客户端数据为主，2表示强制以服务器端数据为主
		List<AppFinancialBean> appFinancialBeans = getListValue(jo, "datas");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(type == 0 || CollectionUtils.isEmpty(appFinancialBeans)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
				
		int fId = 0;
		List<AppFinancialBean> returnBeans = new ArrayList<FinancialServiceImpl.AppFinancialBean>();
		AppFinancialBean newAppFinancialBean = null;
		FinancialBean webBean;
		for(AppFinancialBean appFinancialBean: appFinancialBeans){
			fId = appFinancialBean.getId();
			//ID大于0说明是服务器上的数据
			if(fId > 0){
				if(type == 1){
					webBean = new FinancialBean();
					webBean = appFinancialBean;
					webBean.setModifyTime(new Date());
					//以客户端为主
					if(financialMapper.update(webBean)>0){
						newAppFinancialBean = appFinancialBean;
					}
				}else{ //以服务器的为主
					
					webBean = financialMapper.findById(FinancialBean.class, fId);
					newAppFinancialBean = (AppFinancialBean) webBean;
					newAppFinancialBean.setLocalId(appFinancialBean.getLocalId());
				}
				returnBeans.add(newAppFinancialBean);
			}
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"强制更新数据").toString(), "force()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("message", returnBeans);
		message.put("isSuccess", true);
		return message;
	}
	
	/**
	 * 从json对象中获取key对应的值，没有该key返回[]
	 * @param object json对象
	 * @param key  键名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<AppFinancialBean> getListValue(JSONObject object, String key){				
		if(JsonUtil.hasKey(object, key)){
			String s = String.valueOf(object.get(key));
			if(StringUtil.isNotNull(s)){
				return (List<AppFinancialBean>) JSONArray.toCollection(JSONArray.fromObject(s));
			}
			return new ArrayList<AppFinancialBean>();
		}
		return new ArrayList<AppFinancialBean>();
	}
	
	/**
	 * 判断数据是否改变
	 * @param app
	 * @param web
	 * @return
	 */
	private boolean hasChange(FinancialBean app, FinancialBean web){
		boolean result = true;
		if(app.getId() == web.getId()
				&& app.getModel() == web.getModel()
				&& app.isHasImg() == web.isHasImg()
				&& app.getCreateUserId() == web.getCreateUserId()
				&& app.getFinancialDesc().equals(web.getFinancialDesc())
				&& app.getLocation().equals(web.getLocation())
				&& app.getMoney() == web.getMoney()
				&& app.getCreateUserId() == web.getCreateUserId()
				&& app.getStatus() == web.getStatus()
				&& app.getOneLevel() == web.getOneLevel()
				&& app.getTwoLevel() == web.getTwoLevel()){
			result = false;
		}
		
		return result;
	}
	
	class AppFinancialBean extends FinancialBean{
		private static final long serialVersionUID = 1555187356356786403L;
		private int localId;

		public int getLocalId() {
			return localId;
		}

		public void setLocalId(int localId) {
			this.localId = localId;
		}
	}
}
