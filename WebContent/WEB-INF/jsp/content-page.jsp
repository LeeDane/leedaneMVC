<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

	String basePath = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/"; 
	String content = String.valueOf(request.getAttribute("content"));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>全文阅读</title>
<script type="text/javascript" src="<%=basePath %>js/others/jquery-1.9.1.min.js"></script>
</head>
<body>
	<div id="content" style="width:100%;z-index: 999;margin-top: 1px;" ><%=content %></div>
</body>
</html>