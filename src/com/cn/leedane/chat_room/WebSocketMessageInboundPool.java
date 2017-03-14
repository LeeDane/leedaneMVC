package com.cn.leedane.chat_room;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageInboundPool {
	//保存连接的MAP容器  
    private static final Map<String,WebSocketMessageInbound > connections = new HashMap<String,WebSocketMessageInbound>();  
      
    //向连接池中添加连接  
    public void addMessageInbound(String chatKey, WebSocketMessageInbound inbound){  
        //添加连接  
        connections.put(chatKey, inbound);  
    } 
    
    /**
     * 获取单个连接对象
     * @param key
     */
    public WebSocketMessageInbound getWebSocketMessageInbound(String key){
    	return connections.get(key);
    }
      
    /**
     * 获取所有的在线用户  
     * @return
     */
    public static Set<String> getAllUser(){  
        return connections.keySet();  
    }  
      
    public void removeMessageInbound(WebSocketMessageInbound inbound){  
        //移除连接  
        connections.remove(inbound.getId());  
    } 
}
