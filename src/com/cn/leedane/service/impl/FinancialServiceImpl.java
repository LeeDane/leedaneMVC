package com.cn.leedane.service.impl;

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
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cn.leedane.mapper.FinancialMapper;
import com.cn.leedane.model.FinancialBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.FinancialService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
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
	public Map<String, Object> save(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->save():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		FinancialBean financialBean = getValue(jo, "data", user);
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(financialBean == null){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
		
		boolean result = financialMapper.save(financialBean) > 0;
		if(result){
			message.put("isSuccess", true);
			message.put("message", financialBean.getId());
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"新添一条记账,ID为：", financialBean.getId(), StringUtil.getSuccessOrNoStr(result)).toString(), "save()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}
	
	@Override
	public Map<String, Object> update(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->update():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		FinancialBean financialBean = getValue(jo, "data", user);
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(financialBean == null || financialBean.getId() < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
		
		boolean result = financialMapper.update(financialBean) > 0;
		if(result){
			message.put("isSuccess", true);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据更新失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据更新失败.value);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"更新一条记账，ID为", financialBean.getId(), StringUtil.getSuccessOrNoStr(result)).toString(), "update()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}
	
	@Override
	public Map<String, Object> delete(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->delete():jsonObject=" +jo.toString() +", user=" +user.getAccount());
		int fid = JsonUtil.getIntValue(jo, "fid");
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(fid < 1){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
		
		boolean result = financialMapper.deleteById(FinancialBean.class, fid) > 0;
		if(result){
			message.put("isSuccess", true);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据删除失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据删除失败.value);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"删除一条记账,Id为：", fid, StringUtil.getSuccessOrNoStr(result)).toString(), "delete()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}
	
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
	private List<AppFinancialBean> getListValue(JSONObject object, String key){				
		if(JsonUtil.hasKey(object, key)){
			String s = String.valueOf(object.get(key));
			if(StringUtil.isNotNull(s)){
				return (List<AppFinancialBean>) JSONArray.parseArray(s, AppFinancialBean.class);
			}
			return new ArrayList<AppFinancialBean>();
		}
		return new ArrayList<AppFinancialBean>();
	}
	
	/**
	 * 从json对象中获取key对应的值，没有该key返回[]
	 * @param object json对象
	 * @param key  键名称
	 * @return
	 */
	private FinancialBean getValue(JSONObject object, String key, UserBean user){				
		if(JsonUtil.hasKey(object, key)){
			String s = String.valueOf(object.get(key));
			if(StringUtil.isNotNull(s)){
				FinancialBean financialBean = JSON.parseObject(s, FinancialBean.class);
				if(financialBean != null){
					financialBean.setCreateUserId(user.getId());
					financialBean.setCreateTime(new Date());
					financialBean.setModifyUserId(user.getId());
					financialBean.setModifyTime(new Date());
				}
				return financialBean;
			}
			return null;
		}
		return null;
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

	@Override
	public Map<String, Object> getByYear(JSONObject jsonObject,
			UserBean user, HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->getByYear():jsonObject=" +jsonObject.toString() +", user=" +user.getAccount());
		
		int year = JsonUtil.getIntValue(jsonObject, "year"); //年份
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(year < 1990){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.系统不支持查找该年份的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.系统不支持查找该年份的数据.value);
			return message;
		}
		
		List<Map<String, Object>> list = financialMapper.getLimit(year, ConstantsUtil.STATUS_NORMAL, user.getId());
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取", year, "年的记账数据").toString(), "getByYear()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("message", list);
		message.put("isSuccess", true);
		return message;
	}
	
	@Override
	public Map<String, Object> getAll(JSONObject jsonObject,
			UserBean user, HttpServletRequest request) {
		logger.info("FinancialServiceImpl-->getAll():jsonObject=" +jsonObject.toString() +", user=" +user.getAccount());
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		List<Map<String, Object>> list = financialMapper.getAll(ConstantsUtil.STATUS_NORMAL, user.getId());
		
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取全部的记账数据").toString(), "getAll()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("message", list);
		message.put("isSuccess", true);
		return message;
	}
/*	
	public static void main(String[] args) {
		String string = "{\"one_level\":\"食品酒水\",\"two_level\":\"早午晚餐\",\"longitude\":0,\"money\":36,\"synchronous\":false,\"has_img\":false,\"create_user_id\":1,\"financial_desc\":\"图库\",\"status\":2,\"id\":0,\"latitude\":0,\"local_id\":0,\"model\":1,\"create_time\":\"2016-07-26 18:08:41\"}";
		FinancialBean financialBean = JSON.parseObject(string, FinancialBean.class);
		System.out.println(financialBean.getCreateUserId());
		System.out.println(DateUtil.DateToString(financialBean.getCreateTime()));
	}*/
}