package com.cn.leedane.handler;

import java.io.IOException;
import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.cn.leedane.chat_room.Util;
import com.cn.leedane.chat_room.WebSocketMessageInbound;
import com.cn.leedane.chat_room.WebSocketMessageInboundPool;
import com.cn.leedane.model.ChatSquareBean;
import com.cn.leedane.model.ScoreBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.redis.config.RedisConfig;
import com.cn.leedane.redis.util.RedisUtil;
import com.cn.leedane.service.ChatSquareService;
import com.cn.leedane.service.ScoreService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.FilterUtil;
import com.cn.leedane.utils.JsoupUtil;
import com.cn.leedane.utils.SpringUtil;
import com.cn.leedane.utils.StringUtil;

/**
 * 聊天广场处理类
 * @author LeeDane
 * 2017年2月17日 下午4:29:23
 * Version 1.0
 */
@Component
public class ChatSquareHandler implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RedisUtil redisUtil = RedisUtil.getInstance();
	
	private WebSocketMessageInboundPool pool = new WebSocketMessageInboundPool();

	/**
	 * 添加聊天广场的链接到Redis缓存中
	 * @param userKey
	 * @return
	 * @throws IOException 
	 */
	public boolean addMessageInbound(WebSocketMessageInbound inbound) throws IOException{
		String chatKey = getChatKey(inbound.getId());
		//添加连接  
        System.out.println("user : " + inbound.getId() + " join.."); 
        pool.addMessageInbound(chatKey, inbound);
		
		//System.out.println(redisUtil.setSerialize(chatKey.getBytes(), SerializeUtil.serializeObject(inbound)));
		redisUtil.addString(chatKey, chatKey);
		redisUtil.expire(chatKey, null, StringUtil.changeObjectToInt(RedisConfig.properties.get("chatSquareTime")));
		return true;
	}
	
	/**
	 * 获取单个WebSocketMessageInbound对象
	 * @param chatKey  必须是添加过前缀的key
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public WebSocketMessageInbound getWebSocketMessageInbound(String chatKey){
        //inbound = (WebSocketMessageInbound) SerializeUtil.deserializeObject(value, WebSocketMessageInbound.class);
         return pool.getWebSocketMessageInbound(chatKey);
	}
	
	/**
	 * 获取所有用户的key
	 * @return
	 */
	public Set<String> getAllUserKeys(){
		return redisUtil.keys(ConstantsUtil.CHAT_SQUARE_REDIS+"*");
	}
	
	/**
     * 向特定的用户发送消息
     * @param user
     * @param message
     */
    public void sendMessageToUser(String user, String message){  
        try {  
            //向特定的用户发送数据  
            System.out.println("send message to user : " + user + " ,message content : " + message);
            WebSocketMessageInbound inbound = getWebSocketMessageInbound(getChatKey(user));  
            if(inbound != null){  
                inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));  
            } 
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
    }  
	
    /**
     * 向所有的用户发送消息  
     * @param message
     * @throws ClassNotFoundException 
     */
    public void sendMessage(final String message) throws ClassNotFoundException{  
        try {
            JSONObject jsonObject = JSONObject.fromObject(message);
        	jsonObject.put("time", DateUtil.DateToString(new Date()));
        	UserHandler userHandler = (UserHandler) SpringUtil.getBean("userHandler");
        	String keyString = jsonObject.getString("id");
        	final int userId = StringUtil.changeObjectToInt(keyString.split("UNION")[0]);
        	Map<String, Object> userInfo = new HashMap<String, Object>();
        	
        	//非访客身份将不进行进一步的操作
        	if(userId == Integer.parseInt(Util.VISITORS_ID))
        		return;
        	
    		userInfo = userHandler.getBaseUserInfo(userId);
    		jsonObject.put("user_pic_path", userInfo.get("user_pic_path"));
        	jsonObject.put("account", userInfo.get("account"));
        	
        	jsonObject.put("id", userId);
        	String type = null;
        	if(jsonObject.has("type")){
        		type = jsonObject.getString("type");
        	}
        	
        	Set<String> sensitiveWords = FilterUtil.getFilter(jsonObject.getString("content"));
        	//进行敏感词过滤和emoji过滤
    		if(sensitiveWords != null && sensitiveWords.size() > 0){
    			type = "error";
            	jsonObject.put("type", type);
            	StringBuffer words = new StringBuffer();
            	words.append("您发送的内容有【");
            	for(String sensitiveWord: sensitiveWords){
            		words.append(sensitiveWord + "、");
            	}
            	words.deleteCharAt(words.length() -1);
            	words.append("】等敏感词，请核实！");
            	jsonObject.put("content", words.toString());
            	sendToSelf(jsonObject, userId);
            	return;
    		}
        	
        	//type = "error";
        	//jsonObject.put("type", type);
        	
        	//弹屏类型需要扣积分
        	if(StringUtil.isNotNull(type) && "PlayScreen".equals(type)){
        		dealPlayScreen(type, jsonObject, userId);
        	}else{
        		sendToAll(type, jsonObject, userId);
        	}
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    } 
	
    /**
     * 处理弹屏
     * @param jsonObject
     * @param userId
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unchecked")
	private void dealPlayScreen(String type, JSONObject jsonObject, int userId) throws IOException, ClassNotFoundException{
    	//获取文字
		String screenText = JsoupUtil.getInstance().getContentNoTag(jsonObject.getString("content"));
		if(StringUtil.isNotNull(screenText) && screenText.length() > 30){
			jsonObject.put("content", "抱歉，超过30个文字，无法使用弹屏功能！");
			jsonObject.put("type", "error");
			sendToSelf(jsonObject, userId);
			return;
		}
		
    	ScoreService<ScoreBean> scoreService = (ScoreService<ScoreBean>) SpringUtil.getBean("scoreService");
    	UserService<UserBean> userService = (UserService<UserBean>) SpringUtil.getBean("userService");
		int score = scoreService.getTotalScore(userId);
		if(score < 1){
			jsonObject.put("content", "积分不足，无法发送弹屏！");
			jsonObject.put("type", "error");
			sendToSelf(jsonObject, userId);
			return;
		}else{
			//扣除积分
			Map<String, Object> map= scoreService.reduceScore(1, "发弹屏扣积分", "", 0, userService.findById(userId));
			if(StringUtil.changeObjectToBoolean(map.get("isSuccess"))){
				
				jsonObject.put("screenText", screenText); //弹屏内容，纯文本
				sendToAll(type, jsonObject, userId);
				return;
			}else{
				jsonObject.put("content", "扣除积分失败");
				jsonObject.put("type", "error");
				sendToSelf(jsonObject, userId);
				return;
			}
			
			
		}
    }
    
    /**
     * 发送信息给自己
     * @param jsonObject
     * @param userId
     * @param keySet
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void sendToSelf(JSONObject jsonObject, int userId) throws IOException, ClassNotFoundException{
    	for (String key : getAllUserKeys()) {
            WebSocketMessageInbound inbound = getWebSocketMessageInbound(key);  
            if(inbound != null){
            	if(userId == StringUtil.changeObjectToInt(inbound.getId().split("UNION")[0])){
            		inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonObject.toString()));
            	}
            }  
        }  
    }
    
    /**
     * 发送信息给所有的用户
     * @param type
     * @param jsonObject
     * @param userId
     * @param keySet
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void sendToAll(String type, JSONObject jsonObject, int userId){
    	boolean welcome = StringUtil.isNotNull(type) && "welcome".equals(type);
    	for (String key : getAllUserKeys()) {
            WebSocketMessageInbound inbound = getWebSocketMessageInbound(key);  
            if(inbound != null){
            	//如果是欢迎并且是自己就不发送了
            	if(welcome && userId == StringUtil.changeObjectToInt(inbound.getId().split("UNION")[0])){
            		continue;
            	}
            	
                System.out.println("send message to user : " + key + " ,message content : " + jsonObject.toString());  
                try {
					inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonObject.toString()));
				} catch (IOException e) {
					//报错的话说明连接已经取消，就把连接清除掉
					pool.removeMessageInbound(inbound);
					continue;
				}  
            }  
        } 
    	
    	if(!welcome){
    		saveRecore(userId, jsonObject);
    	}
    }
    
    /**
     * 保存记录
     * @param userId
     * @param jsonObject
     */
    private void saveRecore(final int userId, final JSONObject jsonObject){
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				@SuppressWarnings("unchecked")
				ChatSquareService<ChatSquareBean> chatSquareService = (ChatSquareService<ChatSquareBean>) SpringUtil.getBean("chatSquareService");
				chatSquareService.addChatSquare(userId, jsonObject.toString());
			}
		}).start();
    }
    
	/**
	 * 获取赞在redis的key
	 * @param id
	 * @return
	 */
	public static String getChatKey(String userKey){
		return ConstantsUtil.CHAT_SQUARE_REDIS +userKey;
	}
}
