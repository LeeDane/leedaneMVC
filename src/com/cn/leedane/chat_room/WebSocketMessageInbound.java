package com.cn.leedane.chat_room;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.leedane.handler.ChatSquareHandler;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.SpringUtil;

public class WebSocketMessageInbound extends MessageInbound implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ChatSquareHandler chatSquareHandler;

	//当前连接的用户名称  
    private final String id;  
  
    public WebSocketMessageInbound(String id) {  
        this.id = id; 
        if(chatSquareHandler == null)
        	chatSquareHandler = (ChatSquareHandler) SpringUtil.getBean("chatSquareHandler");
    }  
  
    public String getId() {  
        return this.id;  
    }  
  
    //建立连接的触发的事件  
    @Override  
    protected void onOpen(WsOutbound outbound) { 
    	System.out.println(DateUtil.DateToString(new Date()) +"onOpen"+ outbound);
        // 触发连接事件，在连接池中添加连接  
        /*JSONObject result = new JSONObject();  
        result.element("type", "user_join");  
        result.element("id", this.id);  */
        //向所有在线用户推送当前用户上线的消息  
        //WebSocketMessageInboundPool.sendMessage(result.toString());  
          
       /* result = new JSONObject();  
        result.element("type", "get_online_user");  
        result.element("list", chatSquareHandler.getOnlineUser());  */
        //向连接池添加当前的连接对象  
        try {
			chatSquareHandler.addMessageInbound(this);
		} catch (IOException e) {
			e.printStackTrace();
		}  
        //向当前连接发送当前在线用户的列表  
        //WebSocketMessageInboundPool.sendMessageToUser(this.user, result.toString());  
    }  
  
    /**
     * redis过期自动维护，暂时不需要做什么
     */
    @Override  
    protected void onClose(int status) {
    	// 触发关闭事件，在连接池中移除连接  
    	System.out.println(DateUtil.DateToString(new Date()) +"onClose"+ status); 
        JSONObject result = new JSONObject();  
        result.element("type", "user_leave");  
        result.element("id", this.id);
        result.element("content", "用户断开连接");
        //向在线用户发送当前用户退出的消息  
        //WebSocketMessageInboundPool.sendMessage(result.toString()); 
         
    }  
  
    @Override  
    protected void onBinaryMessage(ByteBuffer message) throws IOException { 
    	System.out.println("onBinaryMessage"+ message);
        throw new UnsupportedOperationException("Binary message not supported.");  
    }  
  
    //客户端发送消息到服务器时触发事件  
    @Override  
    protected void onTextMessage(CharBuffer message) throws IOException { 
    	try {
    		System.out.println("onTextMessage"+ message);
            //向所有在线用户发送消息  
    		chatSquareHandler.sendMessage(message.toString());  
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
}
