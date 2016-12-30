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
   <link rel="stylesheet" href="<%=basePath %>page/other/layui/css/layui.css">
   <!-- 百度JQUERYCDN -->
   <script type="text/javascript" src="<%=basePath %>page/other/jquery-1.9.1.min.js"></script>
   <script src="<%=basePath %>page/other/bootstrap-3.3.0/js/bootstrap.min.js"></script>
   <script type="text/javascript" src="<%=basePath %>page/other/layui/layui.js"></script>
   <script type="text/javascript" src="<%=basePath %>page/other/layui/lay/dest/layui.all.js"></script>
   <script type="text/javascript" src="<%=basePath %>page/js/base.js"></script>
   <style type="text/css">
   		body{
   			background-color: #f5f5f5;
   		}
   </style>
 </head>
<body>
	<input type="hidden" value="<%=basePath%>" id="basePath"/>
<body>
<script type="text/javascript">
	/**
	*获得全局的项目主路径
	*/
	function getBasePath(){
		return document.getElementById("basePath").value;
	}
</script>
</html>  