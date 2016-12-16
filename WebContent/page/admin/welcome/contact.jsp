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
<div class="container" style="margin-top:10px;">
	   <div class="row">
	   		<div class="list-group">
				<a href="#" class="list-group-item active">
					<h4 class="list-group-item-heading">
						联系方式
					</h4>
				</a>
				<a href="#" class="list-group-item">
					<h4 class="list-group-item-heading">
						QQ：825711424
					</h4>
				</a>
				<a href="#" class="list-group-item">
					<h4 class="list-group-item-heading">
						QQ邮箱：825711424@qq.com
					</h4>
				</a>
				<a href="http://weibo.com/leedane/home?topnav=1&wvr=5" class="list-group-item">
					<h4 class="list-group-item-heading">
						微博：825711424
					</h4>
				</a>
				<a href="#" class="list-group-item">
					<h4 class="list-group-item-heading">
						微信：leedane
					</h4>
				</a>
				<a href="#" class="list-group-item">
					<h4 class="list-group-item-heading">
						微信公众号：leedane
					</h4>
				</a>
			</div>
	   </div>
</div>
  	
<body>
</html>  