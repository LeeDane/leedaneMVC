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
  	<p>
	    关于我们：<br/>这是一款集生活、娱乐、社区互动为一体的综合系统，主要提供了Web端和Android客户端，目的是为了个人学习开发，没有任何商业目的。<br/>主要功能：<br/>包括博客、心情、记账等等主模块。<br/><br/>
	</p>
	<p style="display:none;">
	    <br/>
	</p>
<body>
</html>  