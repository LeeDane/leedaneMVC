<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cn.leedane.controller.UserController"%>
<%
	Object obj = session.getAttribute(UserController.USER_INFO_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE HTML>
<html>
 <head>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link rel="stylesheet" href="<%=basePath %>page/other/bootstrap-3.3.0/css/bootstrap.min.css">
   <style type="text/css">
   </style>
 </head>
<body>
  	<blockquote>
	  <p>欢迎您使用leedane后台管理系统！</p>
	  <footer>新的一天，从心开始<cite title="Source Title">Come on!</cite></footer>
	</blockquote>
	
<body>
</html>  