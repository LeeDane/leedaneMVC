<%@page import="com.cn.leedane.utils.CommonUtil"%>
<%@page import="com.cn.leedane.utils.StringUtil"%>
<%@page import="com.cn.leedane.model.UserBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cn.leedane.controller.UserController"%>
<%
	Object o = session.getAttribute(UserController.USER_INFO_KEY);
	String ac = "";
	boolean login = o != null;
	UserBean loginUser = null;
	if(login){
		loginUser = (UserBean)o;
		ac = loginUser.getAccount();
	}
	String basePath = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/"; 

	String url = CommonUtil.getFullPath(request);
	
	//是否隐藏头部
	String noHeaderStr = request.getParameter("noHeader");
	boolean noHeader = false;
	if(StringUtil.isNotNull(noHeaderStr)){
		noHeader = StringUtil.changeObjectToBoolean(noHeaderStr);
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>公共部分</title>
	<!-- <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
	百度JQUERYCDN
	<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
	<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
	<link rel="stylesheet" href="<%=basePath %>page/other/bootstrap-3.3.0/css/bootstrap.min.css">
	
	<!-- 百度JQUERYCDN -->
	<script type="text/javascript" src="<%=basePath %>page/other/jquery-1.9.1.min.js"></script>
	<script src="<%=basePath %>page/other/bootstrap-3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="other/layui/layui.js"></script>
	<script type="text/javascript" src="other/layui/lay/dest/layui.all.js"></script>
	<style type="text/css">
		::-webkit-scrollbar {
			  width: 5px;
		} /* 这是针对缺省样式 (必须的) */
		::-webkit-scrollbar-track {
		  background-color: #f5f5f5;
		} /* 滚动条的滑轨背景颜色 */
	
		::-webkit-scrollbar-thumb {
			  background-color: rgba(0, 0, 0, 0.2); 
		} /* 滑块颜色 */
	
		::-webkit-scrollbar-button {
			  background-color: #7c2929;
		} /* 滑轨两头的监听按钮颜色 */
	
		::-webkit-scrollbar-corner {
			  background-color: black;
		} /* 横向滚动条和纵向滚动条相交处尖角的颜色 */
		
		
		.side-open{
			width: 25px;
			height: 20px;
			margin-top: 15px;
			margin-right: 10px;
		}
		.left-sider-bg{
			width: 100%;
			height: 100%;
			background-color: #999;
			position: absolute;
			left: 0;
			top: 0;
			bottom: 0;
			right: 0;
			z-index: 1500;
			opacity:0.5;
			filter:alpha(opacity=50); 
		}
		.left-sider{
			position: absolute;
			left: -200px;
			top: 0;
			width: 200px;
			height: 100%;
			z-index: 2000;
			
		}
		.side-open{
			margin-right: 10px;
			margin-top: 15px;
		}
		
		.hand{
			cursor: pointer;
		}
	</style>
</head>
<input type="hidden" value="<%=basePath%>" id="basePath"/>
<% if(!noHeader){ %>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class=" ">
    <div class="navbar-header">
        <span class="side-open glyphicon glyphicon-align-justify"></span>
    </div>
    <div>
        <ul class="nav navbar-nav">
            <li class="active nav-blog nav-main-li"><a href="<%=basePath %>page/index.jsp">博客</a></li>
            <li class="nav-photo nav-main-li"><a href="<%=basePath %>page/photo.jsp">相册</a></li>
            <% if(login) { %>
            	<li class="nav-my nav-main-li"><a href="<%=basePath %>page/my.jsp">个人中心</a></li>
            	<li class="nav-financial nav-main-li"><a href="<%=basePath %>page/financial.jsp">记账</a></li>
            <%} %>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                		<% if(login) { %>
                			<%=ac %>
                		<%}else{ %>
                			我
                		<%} %>
                    	 <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                <% if(login) { %>
                	<li><a href="<%=basePath %>page/publish-blog.jsp">记一博</a></li>
               	<%} %>
                    <li><a href="#">关于我</a></li>
                    <li><a href="<%=basePath %>page/chat-square.jsp">聊天室</a></li>
                    <li><a href="<%=basePath %>page/download.jsp">app下载</a></li>
               	<% if(loginUser != null && loginUser.isAdmin()){ %>
               		<li><a href="<%=basePath %>page/admin/index.jsp">系统后台</a></li>
               	<%} %>
               	<% if(login) { %>
                	<li><a href="javascript:void(0);" onclick="logout()">退出</a></li>
               	<%}else{ %>
                	<li><a href="<%=basePath %>page/login.jsp?ref=<%=url %>">登录</a></li>
               	<%} %>
                </ul>
            </li>
        </ul>
    </div>
    </div>
</nav>
<%} %>
<%-- <div class="left-sider-bg" onclick="sideClick()"></div>
<div class="left-sider">
	 <iframe width="100%" height="100%" src="<%=basePath %>page/jsp/index-sider.jsp"></iframe> 
</div> --%>

<script type="text/javascript">
	
	jQuery(function(){
		
		console.log($("body").height());
		resetSideHeight();
		$(".left-sider-bg").hide();
		
		//打开左侧菜单
		$(".side-open").on("click", function(){
			$(".left-sider-bg").show();
			$('.left-sider').animate({
		    	'left': '0px'
		    }, 500);
		});
		
	});
	
	/**
	*获得全局的项目主路径
	*/
	function getBasePath(){
		return document.getElementById("basePath").value;
	}
	
	//退出登录
	function logout(){
		var params = {t: Math.random()};
		$.ajax({
			type : "post",
			data : params,
			dataType: 'json',
			url : "<%=basePath %>leedane/user/logout.action",
			beforeSend:function(){
			},
			success : function(data) {
				layer.msg(data.message);
				if(data.isSuccess)
					//刷新当前页面
					window.location.reload();					
			},
			error : function() {
				layer.msg("网络请求失败");
			}
		});
	}
	
	function sideClick(){
		$(".left-sider-bg").hide();
		$('.left-sider').animate({
	    	'left': '-200px'
	    }, 500);
	}
	
	function resetSideHeight(){
		var height = $("body").height();
		if(height > window.innerHeight)
			height = height + 100;
		else
			height = window.innerHeight;
		
		$(".left-sider-bg").height(height);
	}
</script>
</html>