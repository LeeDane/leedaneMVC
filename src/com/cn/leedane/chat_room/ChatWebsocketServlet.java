package com.cn.leedane.chat_room;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatWebsocketServlet extends WebSocketServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String account = null;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0) {
		// TODO Auto-generated method stub
		return new WebSocketMessageInbound(account);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		account = req.getParameter("account");
		account = URLDecoder.decode(account, "UTF-8");
		super.doGet(req, resp);
	}

}
