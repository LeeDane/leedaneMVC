<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

	String basePath = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/"; 
	String content = String.valueOf(request.getAttribute("content"));
	String imgs = String.valueOf(request.getAttribute("imgs"));
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>全文阅读</title>

<!-- 百度JQUERYCDN -->
<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
</head>
<body>
	<div id="content" style="width:100%;z-index: 999;margin-top: 1px;" onclick="showOnclickMessage()"><%=content %></div>
</body>

<script type="text/javascript">

	$(function(){
	});
	
	function clickImg(obj, index){
		var width = parseInt($(obj).width());
		var height = parseInt($(obj).height());
		webview.clickImg("<%=imgs %>", index, width, height);
	}
	
</script>
</html>