package com.cn.leedane.chat_room;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cn.leedane.handler.UserHandler;
import com.cn.leedane.model.ChatSquareBean;
import com.cn.leedane.model.ScoreBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.ChatSquareService;
import com.cn.leedane.service.ScoreService;
import com.cn.leedane.service.UserService;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.JsoupUtil;
import com.cn.leedane.utils.SpringUtil;
import com.cn.leedane.utils.StringUtil;

import net.sf.json.JSONObject;

@Component
public class WebSocketMessageInboundPool {
	//保存连接的MAP容器  
    private static final Map<String,WebSocketMessageInbound > connections = new HashMap<String,WebSocketMessageInbound>();  
      
    //向连接池中添加连接  
    public void addMessageInbound(WebSocketMessageInbound inbound){  
        //添加连接  
        System.out.println("user : " + inbound.getId() + " join..");  
        connections.put(inbound.getId(), inbound);  
    }  
      
    /**
     * 获取所有的在线用户  
     * @return
     */
    public static Set<String> getOnlineUser(){  
        return connections.keySet();  
    }  
      
    public void removeMessageInbound(WebSocketMessageInbound inbound){  
        //移除连接  
        System.out.println("user : " + inbound.getId() + " exit..");  
        connections.remove(inbound.getId());  
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
            WebSocketMessageInbound inbound = connections.get(user);  
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
     */
    public void sendMessage(final String message){  
        try {  
            Set<String> keySet = connections.keySet();  
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
        	
        	//弹屏类型需要扣积分
        	if(StringUtil.isNotNull(type) && "PlayScreen".equals(type)){
        		dealPlayScreen(type, jsonObject, userId, keySet);
        	}else{
        		sendToAll(type, jsonObject, userId, keySet);
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
     */
    @SuppressWarnings("unchecked")
	private void dealPlayScreen(String type, JSONObject jsonObject, int userId, Set<String> keySet) throws IOException{
    	//获取文字
		String screenText = JsoupUtil.getInstance().getContentNoTag(jsonObject.getString("content"));
		if(StringUtil.isNotNull(screenText) && screenText.length() > 30){
			jsonObject.put("content", "抱歉，超过30个文字，无法使用弹屏功能！");
			jsonObject.put("type", "error");
			sendToSelf(jsonObject, userId, keySet);
			return;
		}
		
    	ScoreService<ScoreBean> scoreService = (ScoreService<ScoreBean>) SpringUtil.getBean("scoreService");
    	UserService<UserBean> userService = (UserService<UserBean>) SpringUtil.getBean("userService");
		int score = scoreService.getTotalScore(userId);
		if(score < 1){
			jsonObject.put("content", "积分不足，无法发送弹屏！");
			jsonObject.put("type", "error");
			sendToSelf(jsonObject, userId, keySet);
			return;
		}else{
			//扣除积分
			Map<String, Object> map= scoreService.reduceScore(1, "发弹屏扣积分", "", 0, userService.findById(userId));
			if(StringUtil.changeObjectToBoolean(map.get("isSuccess"))){
				
				jsonObject.put("screenText", screenText); //弹屏内容，纯文本
				sendToAll(type, jsonObject, userId, keySet);
				return;
			}else{
				jsonObject.put("content", "扣除积分失败");
				jsonObject.put("type", "error");
				sendToSelf(jsonObject, userId, keySet);
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
     */
    private void sendToSelf(JSONObject jsonObject, int userId, Set<String> keySet) throws IOException{
    	for (String key : keySet) {
            WebSocketMessageInbound inbound = connections.get(key);  
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
     */
    private void sendToAll(String type, JSONObject jsonObject, int userId, Set<String> keySet) throws IOException{
    	boolean welcome = StringUtil.isNotNull(type) && "welcome".equals(type);
    	for (String key : keySet) {
            WebSocketMessageInbound inbound = connections.get(key);  
            if(inbound != null){
            	//如果是欢迎并且是自己就不发送了
            	if(welcome && userId == StringUtil.changeObjectToInt(inbound.getId().split("UNION")[0])){
            		continue;
            	}
            	
                System.out.println("send message to user : " + key + " ,message content : " + jsonObject.toString());  
                inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonObject.toString()));  
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
}
