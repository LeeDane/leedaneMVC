package com.cn.leedane.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		boolean result = false;
		if(financialBean.getId() > 0){//说明是更新
			result = financialMapper.update(financialBean) > 0;
		}else{
			String imei = JsonUtil.getStringValue(jo, "imei");
			if(checkExists(imei, financialBean.getLocalId(), financialBean.getAdditionTime())){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.添加的记录已经存在.value));
				message.put("responseCode", EnumUtil.ResponseCode.添加的记录已经存在.value);
				message.put("isSuccess", false);
				return message;
			}
			result = financialMapper.save(financialBean) > 0;
		}
		
		if(result){
			Map<String, Integer> r = new HashMap<String, Integer>();
			r.put("local_id", financialBean.getLocalId());
			r.put("id", financialBean.getId());
			message.put("isSuccess", true);
			message.put("message", r);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"新添一条记账,ID为：", financialBean.getId(), StringUtil.getSuccessOrNoStr(result)).toString(), "save()", ConstantsUtil.STATUS_NORMAL, 0);
		return message;
	}
	
	/**
	 * 判断记录是否已经存在
	 * @return
	 */
	private boolean checkExists(String imei, int localId, String additionTime){
		
		//暂时对没有imei或者additionTime 不做处理
		if(StringUtil.isNull(imei) || StringUtil.isNull(additionTime)){
			return false;
		}
		
		List<Map<String, Object>> financialBeans = financialMapper.executeSQL("select add_day from  t_financial where imei = ? and local_id =? limit 1", imei, localId);
		if(!CollectionUtils.isEmpty(financialBeans)){
			if(additionTime.substring(0, 10).equals(String.valueOf(financialBeans.get(0).get("add_day")))){
				return true;
			}
		}
		return false;
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
		List<FinancialBean> appFinancialBeans = getListValue(jo, "datas");
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(CollectionUtils.isEmpty(appFinancialBeans)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
		
		//设置imei值
		String imei = JsonUtil.getStringValue(jo, "imei");
		for(FinancialBean fBean: appFinancialBeans){
			fBean.setImei(imei);
		}
		
		List<Map<String, Integer>> inserts = new ArrayList<Map<String,Integer>>();//此次同步插入的数据ID
		List<Map<String, Integer>> updates = new ArrayList<Map<String,Integer>>(); //此次同步发现数据有更新的数据ID
		List<Map<String, Integer>> deletes = new ArrayList<Map<String,Integer>>();//此次同步发现数据被删的数据ID
		int total = 0; //此次同步总共处理的数量
		
		int fId = 0;
		FinancialBean webBean;
		for(FinancialBean appFinancialBean: appFinancialBeans){
			fId = appFinancialBean.getId();
			//ID大于0说明是服务器上的数据
			if(fId > 0){
				webBean = financialMapper.findById(FinancialBean.class, fId);
				if(webBean == null){
					//deletes.add(fId);
					Map<String, Integer> map = new HashMap<String, Integer>();
					map.put("localId", appFinancialBean.getLocalId());
					map.put("id", appFinancialBean.getId());
					deletes.add(map);
				}else{
					if(hasChange(appFinancialBean, webBean)){
						Map<String, Integer> map = new HashMap<String, Integer>();
						map.put("localId", appFinancialBean.getLocalId());
						map.put("id", appFinancialBean.getId());
						if(financialMapper.update(appFinancialBean) > 0){
							updates.add(map);
						}
					}
				}
			}else{
				FinancialBean financialBean = (FinancialBean)appFinancialBean;
				financialBean.setCreateTime(new Date());
				
				//校验记录是否存在
				if(!checkExists(imei, financialBean.getLocalId(), financialBean.getAdditionTime())){
					if(financialMapper.save(financialBean) > 0){
						Map<String, Integer> map = new HashMap<String, Integer>();
						map.put("localId", appFinancialBean.getLocalId());
						map.put("id", financialBean.getId());
						inserts.add(map);
						total = total + 1;
					}
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
		List<FinancialBean> financialBeans = getListValue(jo, "datas");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(type == 0 || CollectionUtils.isEmpty(financialBeans)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.没有要同步的数据.value));
			message.put("responseCode", EnumUtil.ResponseCode.没有要同步的数据.value);
			message.put("isSuccess", true);
			return message;
		}
				
		int fId = 0;
		List<FinancialBean> returnBeans = new ArrayList<FinancialBean>();
		FinancialBean newFinancialBean = null;
		FinancialBean webBean;
		for(FinancialBean FinancialBean: financialBeans){
			fId = FinancialBean.getId();
			//ID大于0说明是服务器上的数据
			if(fId > 0){
				if(type == 1){
					webBean = new FinancialBean();
					webBean = FinancialBean;
					webBean.setModifyTime(new Date());
					//以客户端为主
					if(financialMapper.update(webBean)>0){
						newFinancialBean = FinancialBean;
					}
				}else{ //以服务器的为主
					
					webBean = financialMapper.findById(FinancialBean.class, fId);
					newFinancialBean = (FinancialBean) webBean;
					newFinancialBean.setLocalId(FinancialBean.getLocalId());
				}
				returnBeans.add(newFinancialBean);
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
	private List<FinancialBean> getListValue(JSONObject object, String key){				
		if(JsonUtil.hasKey(object, key)){
			String s = String.valueOf(object.get(key));
			if(StringUtil.isNotNull(s)){
				return JSONArray.parseArray(s, FinancialBean.class);
			}
			return new ArrayList<FinancialBean>();
		}
		return new ArrayList<FinancialBean>();
	}
	
	/*public static Object jsonToObject(String json, Class cls)
			throws JsonGenerationException, JsonMappingException, IOException {
			Object obj = null;
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			obj = mapper.readValue(json, cls);
			return obj;
			}*/
	
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
					financialBean.setImei(JsonUtil.getStringValue(object, "imei"));
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
		/*boolean result = true;
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
		
		return result;*/
		return !app.equals(web);
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
}
