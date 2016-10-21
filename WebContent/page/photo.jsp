<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.UUID"%>
<%@page import="com.cn.leedane.model.UserBean"%>
<%@page import="com.cn.leedane.controller.UserController"%>
<%
	Object obj = session.getAttribute(UserController.USER_INFO_KEY);
	UserBean userBean = null;
	String account = "";
	boolean isLogin = obj != null;
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
	}else{
		String bp = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/";
		response.sendRedirect(bp +"page/login.jsp?ref="+request.getRequestURL()+"&t="+UUID.randomUUID().toString());
	}	
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><%=account %>的相册</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<script src="js/base.js"></script>
	<style type="text/css">
		
		.container{
			margin-top: 50px;
		}
		.row{
			margin-top: 15px;
		}
		.panel-footer button{
			margin-top: 5px;
			margin-right: 5px;
		}
	</style>
</head>
<body>
<%@ include file="/page/common.jsp" %>
<script type="text/javascript" src="other/layui/layui.js"></script>
<script type="text/javascript" src="other/layui/lay/dest/layui.all.js"></script>
<script src="<%=basePath %>page/js/photo.js"></script>

</body>

<div class="container">
	   <div class="row">
	      <div class="col-lg-4" id="column-01">
	      
	      </div>
	      <div class="col-lg-4" id="column-02">
	      
	      </div>     
	      <div class="col-lg-4" id="column-03">
	      
	      </div>
	   </div> 
	   </div>

	<script type="text/javascript">
		$(function(){
			$(".nav-blog").removeClass("active");
			$(".nav-photo").addClass("active");
		});
	</script>
</html>