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
import org.springframework.transaction.annotation.Transactional;

import com.cn.leedane.exception.ErrorException;
import com.cn.leedane.utils.Base64ImageUtil;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EmailUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.MD5Util;
import com.cn.leedane.utils.StringUtil;
import com.cn.leedane.cache.SystemCache;
import com.cn.leedane.enums.NotificationType;
import com.cn.leedane.handler.CommentHandler;
import com.cn.leedane.handler.FanHandler;
import com.cn.leedane.handler.FriendHandler;
import com.cn.leedane.handler.SignInHandler;
import com.cn.leedane.handler.TransmitHandler;
import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.mapper.FilePathMapper;
import com.cn.leedane.mapper.UserMapper;
import com.cn.leedane.message.ISendNotification;
import com.cn.leedane.message.SendNotificationImpl;
import com.cn.leedane.message.notification.Notification;
import com.cn.leedane.model.FilePathBean;
import com.cn.leedane.model.OperateLogBean;
import com.cn.leedane.model.ScoreBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.OperateLogService;
import com.cn.leedane.service.ScoreService;
import com.cn.leedane.service.UserService;

/**
 * 用户service实现类
 * @author LeeDane
 * 2016年7月12日 下午2:18:19
 * Version 1.0
 */

@Service("userService")
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class UserServiceImpl implements UserService<UserBean> {
	
	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private FilePathMapper filePathMapper;
	
	@Autowired
	private FilePathService<FilePathBean> filePathService;
	
	@Autowired
	private OperateLogService<OperateLogBean> operateLogService;
	
	@Autowired
	private ScoreService<ScoreBean> scoreService;

	@Autowired
	private CommentHandler commentHandler;
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private TransmitHandler transmitHandler;
	
	@Autowired
	private FanHandler fanHandler;
	
	@Autowired
	private FriendHandler friendHandler;

	@Autowired
	private SignInHandler signInHandler;
	
	@Autowired
	private SystemCache systemCache;

	
	@Override
	public UserBean findById(int uid) {
		return userMapper.findById(UserBean.class, uid);
	}

	@Override
	public UserBean loginUser(String condition, String password) {	
		logger.info("UserServiceImpl-->loginUser():condition="+condition+",password="+password);
		return userMapper.loginUser(condition, MD5Util.compute(password));
	}

	@Override
	public Map<String,Object> saveUser(UserBean user) throws Exception{	
		logger.info("UserServiceImpl-->saveUser():user="+user.toString());
		Map<String,Object> message = new HashMap<String,Object>();
		UserBean findUser = null;	
		if(user.getId() == 0){ //没有登录/没有注册的用户
			findUser = userMapper.getBeans("select * from t_user  where account=? or email=?", user.getAccount(), user.getAccount()).get(0);
		}else{ //已经登录的用户/或已经注册的用户
			findUser = userMapper.getBeans("select * from t_user  where id=? ", user.getId()).get(0);
		}			
		//return list.size() > 0 ? list.get(0) : null;
		if(findUser!=null){ //已经有用户存在了
			if(user.getAccount()==null){
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该邮箱已被占用.value));
				message.put("responseCode", EnumUtil.ResponseCode.该邮箱已被占用.value);
				message.put("isAccount", false);
			}else{
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该用户已被占用.value));
				message.put("responseCode", EnumUtil.ResponseCode.该用户已被占用.value);
				message.put("isAccount", true);
			}
			message.put("isSuccess", false);
		}else{
			boolean isSave = userMapper.save(user) > 0;
			if(isSave){
				saveRegisterScore(user);
				message.put("isSuccess", true);
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.请先验证邮箱.value));
				message.put("responseCode", EnumUtil.ResponseCode.请先验证邮箱.value);
				afterRegister(user);
			}else{
				message.put("isSuccess", false);
				message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.注册失败.value));
				message.put("responseCode", EnumUtil.ResponseCode.注册失败.value);
			}
		}
		return message;
	}
	
	/**
	 * 对注册的用户获取系统奖励的积分
	 * @param u
	 */
	private void saveRegisterScore(UserBean u){
		Object object = systemCache.getCache("first-sign-in"); 
		int score = 0;
		if(object != null){
			score = StringUtil.changeObjectToInt(object);
		}
		
		//分数等于0直接不保存
		if(score == 0 )
			return;
		//更新积分
		ScoreBean scoreBean = new ScoreBean();
		scoreBean.setTotalScore(score);
		scoreBean.setScore(score);
		scoreBean.setCreateTime(new Date());
		scoreBean.setCreateUserId(u.getId());
		scoreBean.setScoreDesc("用户注册");
		scoreBean.setStatus(ConstantsUtil.STATUS_NORMAL);
		scoreBean.setTableId(u.getId());
		scoreBean.setTableName(DataTableType.用户.value);
		boolean isSave = scoreService.save(scoreBean);
		//标记为已经添加
		if(isSave){
			signInHandler.addHistorySignIn(u.getId());
		}
	}
	
	//@Transactional(propagation=Propagation.SUPPORTS, readOnly=true) 
	@Override
	public boolean updateCheckRegisterCode(String registerCode) {
		logger.info("UserServiceImpl-->updateCheckRegisterCode():registerCode="+registerCode);
		if(registerCode == null){
			return false;
		}else{
			UserBean user = userMapper.checkRegisterCode(registerCode);
			 if(user != null){
				 user.setStatus(ConstantsUtil.STATUS_NORMAL);
				 return updateUserState(user);
			 }else
				 return false;
		}
	}

	@Override
	public boolean updateUserState(UserBean user) {		
		logger.info("UserServiceImpl-->updateUserState()");
		return userMapper.update(user) > 0;
	}

	@Override
	public void sendEmail(UserBean user) throws Exception{
		logger.info("UserServiceImpl-->sendEmail()");
		afterRegister(user);	
	}

	/**
	 * 实现发送邮件的方法
	 * @throws Exception
	 */
	private void afterRegister(UserBean user2) throws Exception{
		UserBean user = new UserBean();
		
		String content = "欢迎您："+user2.getAccount()+"感谢注册！<a href = '"+ConstantsUtil.SYSTEM_SERVER_URL+ "/leedane/user/completeRegister.action?registerCode="+user2.getRegisterCode()+"'>点击完成注册</a>"
				+ "<p>请勿回复此邮件，有事联系客服QQ"+ConstantsUtil.DEFAULT_USER_FROM_QQ+"</p>";
		user.setAccount(ConstantsUtil.DEFAULT_USER_FROM_ACCOUNT);
		user.setChinaName(ConstantsUtil.DEFAULT_USER_FROM_CHINANAME);
		user.setEmail(ConstantsUtil.DEFAULT_USER_FROM_EMAIL);
		user.setQq(ConstantsUtil.DEFAULT_USER_FROM_QQ);
		user.setStr1(ConstantsUtil.DEFAULT_USER_FROM_QQPSW);
		
		Set<UserBean> set = new HashSet<UserBean>();		
		set.add(user2);	
		
		EmailUtil emailUtil = EmailUtil.getInstance(user, set, content, ConstantsUtil.WEBSIT_NAME +"注册验证");
		try {
			emailUtil.sendMore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Override
	public boolean updateField(String name, Object value, String table,int id) {	
		logger.info("UserServiceImpl-->updateField():name="+name+",value="+value+",table="+table+",id="+id);
		return userMapper.updateField(name, value, table,id);
	}*/

	@Override
	public void findPassword(String account, String type, String value,
			String findPswCode) {
		
		logger.info("UserServiceImpl-->findPassword():account="+account+",type="+type+",value="+value+",findPswCode="+findPswCode);
	}

	@Override
	public List<Map<String, Object>> find4MoreUser(String conditions,
			Object... params) {
		logger.info("UserServiceImpl-->find4MoreUser():conditions="+conditions+",params="+params);
		return this.userMapper.find4MoreUser(conditions, params);
	}
	
	@Override
	public int total(String tableName, String field, String where) {
		logger.info("UserServiceImpl-->total():tableName="+tableName+",field="+field+",where="+where);
		return this.userMapper.total(tableName, field, where);
	}

	@Override
	public List<Map<String, Object>> statisticsUserAge() {
		logger.info("UserServiceImpl-->statisticsUserAge()");
		return this.userMapper.statisticsUserAge();
	}

	@Override
	public List<Map<String, Object>> statisticsUserAgeRang() {
		logger.info("UserServiceImpl-->statisticsUserAgeRang()");
		return this.userMapper.statisticsUserAgeRang();
	}

	@Override
	public List<Map<String, Object>> statisticsUserRegisterByYear() {
		logger.info("UserServiceImpl-->statisticsUserRegisterByYear()");
		return this.userMapper.statisticsUserRegisterByYear();
	}

	@Override
	public List<Map<String, Object>> statisticsUserRegisterByMonth() {
		logger.info("UserServiceImpl-->statisticsUserRegisterByMonth()");
		return this.userMapper.statisticsUserRegisterByMonth();
	}

	@Override
	public List<Map<String, Object>> statisticsUserRegisterByNearWeek() {
		logger.info("UserServiceImpl-->statisticsUserRegisterByNearWeek()");
		return this.userMapper.statisticsUserRegisterByNearWeek();
	}

	@Override
	public List<Map<String, Object>> statisticsUserRegisterByNearMonth() {
		logger.info("UserServiceImpl-->statisticsUserRegisterByNearMonth()");
		return this.userMapper.statisticsUserRegisterByNearMonth();
	}

	@Override
	public UserBean getUserByNoLoginCode(String account, String noLoginCode) {
		if(StringUtil.isNull(account) || StringUtil.isNull(noLoginCode)) return null;
		logger.info("UserServiceImpl-->getUserByNoLoginCode():account="+account+",noLoginCode="+noLoginCode);
		return this.userMapper.getUserByNoLoginCode(account, noLoginCode, ConstantsUtil.STATUS_NORMAL);
	}

	@Override
	public String getHeadBase64StrById(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		
		logger.info("UserServiceImpl-->获取用户头像字符串:jo="+jo.toString()+",user_Account="+user.getAccount());
		String filePath = getHeadFilePathStrById(jo, user, request);;
		//根据路径获取base64字符串
		if(!StringUtil.isNull(filePath)){
			filePath = ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER + "file//" + filePath;
			//保存操作日志
			operateLogService.saveOperateLog(user, request, null, user.getAccount()+"获取头像base64字符串", "getHeadBase64StrById", ConstantsUtil.STATUS_NORMAL, 0);
			return Base64ImageUtil.convertImageToBase64(filePath, null);
		}
		
		return "";
	}

	@Override
	public boolean uploadHeadBase64StrById(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		logger.info("UserServiceImpl-->用户上传头像():jo="+jo.toString()+",user_Account="+user.getAccount());
		String base64 = JsonUtil.getStringValue(jo, "base64");
		if(StringUtil.isNull(base64))
			return false;
		
		return filePathService.saveEachFile(0, base64, user, String.valueOf(user.getId()), DataTableType.用户.value);
	}

	@Override
	public String getHeadFilePathStrById(JSONObject jo, UserBean user,
			HttpServletRequest request) throws Exception {
		logger.info("UserServiceImpl-->获取用户头像路径:jo="+jo.toString()+",user_Account="+user.getAccount());
		int uid = JsonUtil.getIntValue(jo, "uid");
		String size = JsonUtil.getStringValue(jo, "pic_size");
		String sql = "select (case when is_upload_qiniu = 1 then qiniu_path else path end) path from "+DataTableType.文件.value+" where pic_size = ? and table_name = ? and table_uuid = ? and status = ?";
		List<Map<String, Object>> list = filePathMapper.executeSQL(sql, size, DataTableType.用户.value, uid, ConstantsUtil.STATUS_NORMAL);
		
		if(list != null && list.size() > 0){
			return String.valueOf(list.get(0).get("path"));
		}
		return "";
	}

	@Override
	public Map<String,Object> checkAccount(JSONObject jo, HttpServletRequest request,
			UserBean user) {
		logger.info("UserServiceImpl-->checkAccount():jo="+jo.toString());
		String account = JsonUtil.getStringValue(jo, "account");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		boolean result = false;
		
		if(StringUtil.isNull(account)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		result = userMapper.executeSQL("select id from "+DataTableType.用户.value+" where account = ?", account).size() >0;
		if(result){
			message.put("isSuccess", result);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}
	
	@Override
	public boolean checkMobilePhone(JSONObject jo, HttpServletRequest request,
			UserBean user) {
		logger.info("UserServiceImpl-->checkPhone():jo="+jo.toString());
		String mobilePhone = JsonUtil.getStringValue(jo, "mobilePhone");
				
		if(StringUtil.isNull(mobilePhone)){
			return false;
		}
		return userMapper.executeSQL("select id from "+DataTableType.用户.value+" where mobile_phone = ?", mobilePhone).size() >0;
	}
	
	@Override
	public boolean checkEmail(JSONObject jo, HttpServletRequest request,
			UserBean user) {
		logger.info("UserServiceImpl-->checkEmail():jo="+jo.toString());
		String email = JsonUtil.getStringValue(jo, "email");
		
		boolean result = false;
		
		if(StringUtil.isNull(email)){
			return result;
		}
		return userMapper.executeSQL("select id from "+DataTableType.用户.value+" where email = ?", email).size() >0;
	}

	@Override
	public Map<String, Object> getPhoneRegisterCode(JSONObject jo,
			HttpServletRequest request) {
		logger.info("UserServiceImpl-->getPhoneRegisterCode():jo="+jo.toString());
		String mobilePhone = JsonUtil.getStringValue(jo, "mobilePhone");
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		boolean result = false;
		
		if(StringUtil.isNull(mobilePhone) || mobilePhone.length() != 11){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.手机号为空或者不是11位数.value));
			message.put("responseCode", EnumUtil.ResponseCode.手机号为空或者不是11位数.value);
			return message;
		}
		
		//检验手机是否存在
		if(checkMobilePhone(jo, request, null)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该手机号已被注册.value));
			message.put("responseCode", EnumUtil.ResponseCode.该手机号已被注册.value);
			return message;
		}
		
		//保存操作日志
		try {
			operateLogService.saveOperateLog(null, request, null, "手机号码："+mobilePhone+"用户获取注册验证码", "getPhoneRegisterCode", ConstantsUtil.STATUS_NORMAL, 0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		List<String> list = RedisUtil.getInstance().getMap("register_"+mobilePhone, "validationCode", "createTime", "endTime");
		if(list.size() > 0){
			if(!"null".equalsIgnoreCase(list.get(0))){
				String createTime = list.get(1);
				//有记录并且在一分钟以内的直接返false
				if(DateUtil.isInOneMinute(DateUtil.stringToDate(createTime), new Date())){
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作过于频繁.value));
					message.put("responseCode", EnumUtil.ResponseCode.操作过于频繁.value);
					return message;
				}
			}
		}
		
		//执行创建新的验证码
		String validationCode = StringUtil.build6ValidationCode();
		if(validationCode != null && validationCode.length() ==6){
			Map<String, String> map = new HashMap<String, String>();
			map.put("validationCode", validationCode);
			Date createTime = new Date();
			map.put("createTime", DateUtil.DateToString(createTime));
			try {
				map.put("endTime", DateUtil.DateToString(DateUtil.getOverdueTime(createTime, "1小时")));
			} catch (ErrorException e) {
				e.printStackTrace();
			}
			RedisUtil.getInstance().addMap("register_"+mobilePhone, map);
			result = true;
		}
		if(result){
			message.put("isSuccess", result);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
			
		return message;
	}
	@Override
	public Map<String, Object> getPhoneLoginCode(JSONObject jo, HttpServletRequest request) {
		logger.info("UserServiceImpl-->getPhoneLoginCode():jo="+jo.toString());
		String mobilePhone = JsonUtil.getStringValue(jo, "mobilePhone");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		boolean result = false;
		
		if(StringUtil.isNull(mobilePhone) || mobilePhone.length() != 11){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.手机号为空或者不是11位数.value));
			message.put("responseCode", EnumUtil.ResponseCode.手机号为空或者不是11位数.value);
			return message;
		}
		
		//保存操作日志
		try {
			operateLogService.saveOperateLog(null, request, null, "手机号码："+mobilePhone+"用户获取手机登录验证码", "getPhoneLoginCode", ConstantsUtil.STATUS_NORMAL, 0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		List<String> list = RedisUtil.getInstance().getMap("login_"+mobilePhone, "validationCode", "createTime", "endTime");
		if(list.size() > 0){
			if(list.get(0) != null){
				String createTime = list.get(1);
				//有记录并且在一分钟以内的直接返false
				if(!StringUtil.isNull(createTime) && DateUtil.isInOneMinute(DateUtil.stringToDate(createTime), new Date())){
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.操作过于频繁.value));
					message.put("responseCode", EnumUtil.ResponseCode.操作过于频繁.value);
					return message;
				}
			}
		}
		
		//执行创建新的验证码
		String validationCode = StringUtil.build6ValidationCode();
		if(validationCode != null && validationCode.length() ==6){
			Map<String, String> map = new HashMap<String, String>();
			map.put("validationCode", validationCode);
			Date createTime = new Date();
			map.put("createTime", DateUtil.DateToString(createTime));
			try {
				map.put("endTime", DateUtil.DateToString(DateUtil.getOverdueTime(createTime, "1小时")));
			} catch (ErrorException e) {
				e.printStackTrace();
			}
			RedisUtil.getInstance().addMap("login_"+mobilePhone, map);
			ISendNotification sendNotification = new SendNotificationImpl();
			Notification notification = new Notification();
			notification.setType(NotificationType.LOGIN_VALIDATION.value);
			UserBean toUser = new UserBean();
			toUser.setMobilePhone(mobilePhone);
			notification.setToUser(toUser);
			sendNotification.Send(notification);
			result = true;
		}
		if(result){
			message.put("isSuccess", result);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
			message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		}
		return message;
	}
	@Override
	public UserBean registerByPhone(JSONObject jo, HttpServletRequest request) {
		logger.info("UserServiceImpl-->registerByPhone():jo="+jo.toString());
		//{'validationCode':253432,'mobilePhone':172636634664,'account':'leedane','email':'825711424@qq.com','password':'123'}
		String validationCode = JsonUtil.getStringValue(jo, "validationCode");
		String mobilePhone = JsonUtil.getStringValue(jo, "mobilePhone");
		String account = JsonUtil.getStringValue(jo, "account");
		String email = JsonUtil.getStringValue(jo, "email");
		String password = JsonUtil.getStringValue(jo, "password");
		UserBean userBean = new UserBean();
		if(StringUtil.isNull(validationCode) || StringUtil.isNull(mobilePhone) || 
				StringUtil.isNull(account) || StringUtil.isNull(email)|| StringUtil.isNull(password))	{
			return userBean;
		}
		
		//检查手机是否已经注册
		//if(phone)
		
		
		//保存操作日志
		try {
			operateLogService.saveOperateLog(null, request, null, "手机号码："+mobilePhone+"用户获取注册验证码", "getPhoneRegisterCode", ConstantsUtil.STATUS_NORMAL, 0);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		userBean.setAccount(account);
		userBean.setMobilePhone(mobilePhone);
		userBean.setEmail(email);
		userBean.setPassword(MD5Util.compute(password));
		userBean.setRegisterTime(new Date());
		userBean.setStatus(ConstantsUtil.STATUS_NORMAL);//直接注册成功，暂时没有邮箱验证
		
		//保存成功就返回对象，否则返回空
		if(userMapper.save(userBean) > 0){
			saveRegisterScore(userBean);
			return userBean;
		}
		return null;
	}
	
	@Override
	public UserBean loginByPhone(JSONObject jo, HttpServletRequest request) {
		logger.info("UserServiceImpl-->loginByPhone():jo="+jo.toString());
		//{'validationCode':253432,'mobilePhone':172636634664}
		String validationCode = JsonUtil.getStringValue(jo, "validationCode");
		String mobilePhone = JsonUtil.getStringValue(jo, "mobilePhone");
		UserBean resultUser =  new UserBean();
		if(StringUtil.isNull(validationCode) || StringUtil.isNull(mobilePhone)){
			return resultUser;
		}
		
		//校验验证码
		List<String> list = RedisUtil.getInstance().getMap("login_"+mobilePhone, "validationCode", "createTime", "endTime");
		if(list.size() > 0){
			if(list.get(0) != null){
				String endTime = list.get(2);
				//有记录并且在
				if(!StringUtil.isNull(endTime)){
					Date create = new Date();
					Date end = DateUtil.stringToDate(endTime);
					//没有过期
					if(create.before(end)){
						//保存操作日志
						UserBean user = userMapper.loginUserByPhone(mobilePhone);
						operateLogService.saveOperateLog(user, request, null, "手机号码："+mobilePhone+"用户通过手机号码登录系统", "手机号码登录", ConstantsUtil.STATUS_NORMAL, 0);
						return user;
					}
				}
			}
		}
		return null;
	}

	@Override
	public UserBean loginByWeChat(String FromUserName) {
		return this.userMapper.loginByWeChat(FromUserName, ConstantsUtil.STATUS_NORMAL);
	}

	@Override
	public UserBean bindByWeChat(String FromUserName, String account,
			String password) {
		
		//先对已经绑定的记录进行解绑
		wechatUnBind(FromUserName);
		
		UserBean userBean = loginUser(account, MD5Util.compute(password));
		//先登录用户
		if(userBean != null && userBean.getStatus() == ConstantsUtil.STATUS_NORMAL){
			userBean.setWechatUserName(FromUserName);
			return update(userBean) ? userBean: null;
		}
		return null;
	}

	@Override
	public boolean wechatUnBind(String FromUserName) {
		try {
			UserBean userBean = loginByWeChat(FromUserName);
			if(userBean != null ){
				userBean.setWechatUserName("");
				return userMapper.update(userBean) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public List<Map<String, Object>> getAllUserId() {
		return this.userMapper.executeSQL("select id from "+DataTableType.用户.value);
	}

	@Override
	public int getUserIdByName(String username) {
		List<Map<String, Object>> list = this.userMapper.executeSQL("select id from "+DataTableType.用户.value+" where status=? and account=? limit 1", ConstantsUtil.STATUS_NORMAL, username);
		return list != null && list.size() == 1? StringUtil.changeObjectToInt(list.get(0).get("id")) : 0;
	}

	@Override
	public Map<String, Object> getUserInfoData(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("UserServiceImpl-->getUserInfoData():jo="+jo.toString());
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scores", scoreService.getTotalScore(user.getId()));	
		map.put("comments", commentHandler.getComments(user.getId()));
		map.put("transmits", transmitHandler.getTransmits(user.getId()));
		map.put("userId", user.getId());
		Set<String> fans = fanHandler.getMyFans(user.getId());
		if(fans == null)
			map.put("fans", 0);
		else
			map.put("fans", fans.size());
		rs.add(map);
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr(user.getAccount(),"获取自己的基本数据").toString(), "getUserInfoData()", ConstantsUtil.STATUS_NORMAL, 0);
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> registerByPhoneNoValidate(JSONObject jo,
			HttpServletRequest request) {
		logger.info("UserServiceImpl-->registerByPhoneNoValidate():jo="+jo.toString());
		String account = JsonUtil.getStringValue(jo, "account");
		String password = JsonUtil.getStringValue(jo, "password");
		String confirmPassword = JsonUtil.getStringValue(jo, "confirmPassword");
		String phone = JsonUtil.getStringValue(jo, "mobilePhone");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(StringUtil.isNull(account)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.用户名不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.用户名不能为空.value);
			return message;
		}
		
		if(StringUtil.isNull(password) || StringUtil.isNull(confirmPassword)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.密码不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.密码不能为空.value);
			return message;
		}
		
		if(!password.equals(confirmPassword)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.两次密码不匹配.value));
			message.put("responseCode", EnumUtil.ResponseCode.两次密码不匹配.value);
			return message;
		}
		
		if(StringUtil.isNull(phone) || phone.length() != 11){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.手机号为空或者不是11位数.value));
			message.put("responseCode", EnumUtil.ResponseCode.手机号为空或者不是11位数.value);
			return message;
		}
		
		//检查账号是否被占用
		if(userMapper.executeSQL("select id from "+DataTableType.用户.value+" where account = ?", account).size() > 0){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该用户已被占用.value));
			message.put("responseCode", EnumUtil.ResponseCode.该用户已被占用.value);
			return message;
		}
		
		//检查手机已经被注册
		
		if(checkMobilePhone(jo, request, null)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.该手机号已被注册.value));
			message.put("responseCode", EnumUtil.ResponseCode.该手机号已被注册.value);
			return message;
		}
		
		UserBean user = new UserBean();
		user.setAccount(account);
		user.setPassword(MD5Util.compute(password));
		user.setAdmin(false);
		user.setAge(0);
		user.setChinaName(account);
		user.setStatus(ConstantsUtil.STATUS_NORMAL);
		user.setMobilePhone(phone);
		user.setRegisterTime(new Date());
		int firstScore = 0;
		if(systemCache != null){
			firstScore = StringUtil.changeObjectToInt(systemCache.getCache("first-sign-in"));
		}
		
		user.setScore(firstScore);
		
		boolean result = userMapper.save(user) > 0;
		if(result){
			saveRegisterScore(user);
			message.put("isSuccess", result);
			message.put("message", "恭喜您注册成功,请登录");
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
			
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr("注册账号为", account , ",手机号码为：", StringUtil.getSuccessOrNoStr(result)).toString(), "registerByPhoneNoValidate()", ConstantsUtil.STATUS_NORMAL, 0);	
		
		return message;
	}

	@Override
	public Map<String, Object> search(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("UserServiceImpl-->search():jo="+jo.toString());
		String searchKey = JsonUtil.getStringValue(jo, "searchKey");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		
		if(StringUtil.isNull(searchKey)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.检索关键字不能为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.检索关键字不能为空.value);
			return message;
		}
		
		
		List<Map<String, Object>> rs = userMapper.executeSQL("select id, account, personal_introduction introduction, date_format(birth_day,'%Y-%c-%d') birth_day, mobile_phone phone, sex, email, qq, date_format(register_time,'%Y-%c-%d %H:%i:%s') create_time from "+DataTableType.用户.value+" where status=? and account like '%"+searchKey+"%' order by create_time desc limit 25", ConstantsUtil.STATUS_NORMAL);
		if(rs != null && rs.size() > 0){
			int id = 0;
			for(int i = 0; i < rs.size(); i++){
				id = StringUtil.changeObjectToInt(rs.get(i).get("id"));
				rs.get(i).putAll(userHandler.getBaseUserInfo(id));
				if(id != user.getId()){
					rs.get(i).put("is_fan", fanHandler.inAttention(user.getId(), id));
					rs.get(i).put("is_friend", friendHandler.inFriend(user.getId(), id));
				}
					
			}
		}
		message.put("isSuccess", true);
		message.put("message", rs);
		return message;
	}

	@Override
	public Map<String, Object> updateUserBase(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("UserServiceImpl-->updateUserBase():jo="+jo.toString());
		String sex = JsonUtil.getStringValue(jo, "sex");
		String mobilePhone = JsonUtil.getStringValue(jo, "mobile_phone");
		String qq = JsonUtil.getStringValue(jo, "qq");
		String email = JsonUtil.getStringValue(jo, "email");
		String birthDay = JsonUtil.getStringValue(jo, "birth_day");
		String educationBackground = JsonUtil.getStringValue(jo, "education_background");
		String personalIntroduction = JsonUtil.getStringValue(jo, "personal_introduction");
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		user.setSex(sex);
		user.setMobilePhone(mobilePhone);
		user.setQq(qq);
		user.setEmail(email);
		if(StringUtil.isNotNull(birthDay)){
			Date day = DateUtil.stringToDate(birthDay, "yyyy-MM-dd");
			user.setBirthDay(day);
		}
		
		user.setEducationBackground(educationBackground);
		user.setPersonalIntroduction(personalIntroduction);
		
		boolean result = userMapper.update(user) > 0;
		if(result){
			message.put("isSuccess", result);
			
			//把Redis缓存的信息删除掉
			userHandler.deleteUserDetail(user.getId());
			message.put("userinfo", userHandler.getUserInfo(user, true));
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
			
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr("账号为", user.getAccount() , "用户更新基本信息", StringUtil.getSuccessOrNoStr(result)).toString(), "updateUserBase()", ConstantsUtil.STATUS_NORMAL, 0);	
		
		return message;
	}

	@Override
	public Map<String, Object> updatePassword(JSONObject jo, UserBean user,
			HttpServletRequest request) {
		logger.info("UserServiceImpl-->updatePassword():jo="+jo.toString());
		
		//都是经过第一次MD5加密后的字符串
		String password = JsonUtil.getStringValue(jo, "password"); //原来的密码
		String newPassword = JsonUtil.getStringValue(jo, "new_password"); //后来的密码
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("isSuccess", false);
		if(StringUtil.isNull(password) || StringUtil.isNull(newPassword)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
			message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
			return message;
		}
		
		if(!user.getPassword().equals(MD5Util.compute(password))){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.原密码错误.value));
			message.put("responseCode", EnumUtil.ResponseCode.原密码错误.value);
			return message;
		}
		
		if(password.equals(newPassword)){
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.要修改的密码跟原密码相同.value));
			message.put("responseCode", EnumUtil.ResponseCode.要修改的密码跟原密码相同.value);
			return message;
		}
		
		user.setPassword(MD5Util.compute(newPassword));
		
		boolean result = userMapper.update(user) > 0;
		if(result){
			message.put("isSuccess", result);
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.新密码修改成功.value));
			message.put("responseCode", EnumUtil.ResponseCode.新密码修改成功.value);
		}else{
			message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.数据库保存失败.value));
			message.put("responseCode", EnumUtil.ResponseCode.数据库保存失败.value);
		}
			
		//保存操作日志
		operateLogService.saveOperateLog(user, request, null, StringUtil.getStringBufferStr("账号为", user.getAccount() , "用户更改登录密码", StringUtil.getSuccessOrNoStr(result)).toString(), "updatePassword()", ConstantsUtil.STATUS_NORMAL, 0);	
		
		return message;
	}
	
	@Override
	public UserBean findUserBeanByWeixinName(String fromUserName) {
		logger.info("UserServiceImpl-->findUserBeanByWeixinName():fromUserName="+fromUserName);
		List<UserBean> users = null;
		
		if(StringUtil.isNull(fromUserName)){
			return null;
		}
		
		users = userMapper.getBeans("select * from t_user where wechat_user_name='"+fromUserName+"'");

		return users != null && users.size() > 0 ? users.get(0): null;
	}

	@Override
	public List<Map<String, Object>> executeSQL(String sql, Object... params) {
		return userMapper.executeSQL(sql, params);
	}

	@Override
	public boolean save(UserBean t) {
		return userMapper.save(t) > 0;
	}

	@Override
	public boolean update(UserBean t) {
		return userMapper.update(t) > 0;
	}

	@Override
	public boolean delete(UserBean t) {
		return userMapper.deleteById(UserBean.class, t.getId()) > 0;
	}

}