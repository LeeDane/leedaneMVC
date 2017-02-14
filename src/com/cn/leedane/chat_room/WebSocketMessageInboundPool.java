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
import com.cn.leedane.service.ChatSquareService;
import com.cn.leedane.utils.DateUtil;
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
    public void sendMessageToUser(String user,String message){  
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
        	Map<String, Object> userInfo = userHandler.getBaseUserInfo(userId);
        	jsonObject.put("user_pic_path", userInfo.get("user_pic_path"));
        	jsonObject.put("account", userInfo.get("account"));
        	jsonObject.put("id", userId);
        	String type = null;
        	if(jsonObject.has("type")){
        		type = jsonObject.getString("type");
        	}else{
        		if(userId > 0)
	        		new Thread(new Runnable() {
						
						@Override
						public void run() {
							@SuppressWarnings("unchecked")
							ChatSquareService<ChatSquareBean> chatSquareService = (ChatSquareService<ChatSquareBean>) SpringUtil.getBean("chatSquareService");
							chatSquareService.addChatSquare(userId, message);
						}
					}).start();
        	}
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
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }    
}
