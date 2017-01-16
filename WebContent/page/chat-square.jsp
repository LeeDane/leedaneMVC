<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cn.leedane.controller.UserController"%>
<%
	Object obj = session.getAttribute(UserController.USER_INFO_KEY);
	boolean isLogin = obj != null;
	int loginUserId = 0;
	boolean isAdmin = false;
	if(isLogin){
		UserBean user = (UserBean)obj;
		loginUserId = user.getId();
		isAdmin = user.isAdmin();
	}
	
	String bp = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/"; 
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>聊天广场</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<script src="js/base.js"></script>
	<script type="text/javascript" src="<%=bp %>page/js/comet4j.js"></script>
	<style type="text/css">
		#main-container{
			margin-top: 60px;
		}
		
		#content{
			height: 400px;
			background-color: blue;
			margin-bottom: 30px;
			overflow-y: scroll; 
		}
		#send{
			height: 30px; 
			position: fixed;
			bottom: 0;
			background-color: red;
		}
	</style>
</head>
<body onload="init();">
<%@ include file="common.jsp" %>
<script src="<%=basePath %>page/js/chart-square.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
<div class="main clearFloat">
	<div class="container" id="main-container">
	   <div class="row">
			<div class="col-lg-3">
				eee
			</div>
			<div class="col-lg-6">
				<div class="row" id="content">
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					<p>jjjjj</p>
					fgmg
				</div>
				<div class="row" id="send">
					rrrrrr
				</div>
			</div>
			<div class="col-lg-3"> 
				ttt
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	var isLogin = <%=isLogin %>; //是否已经登录
	var loginUserId = <%=loginUserId %>; //获取登录用户Id
	var isAdmin = <%=isAdmin %>; //是否是管理员
	var connId = ""; //当前页面的连接ID
   	$(function () {	  
	  $(".navbar-nav .nav-main-li").each(function(){
			$(this).removeClass("active");
		});
	  
	  var height = $(document).height();
	  console.log("llll"+ height);
      
  	});
  
  	function init(){
      JS.Engine.on({  
    	  scan_login : function(data){
    		 	data = eval('(' + data + ')');
    			if(data.isSuccess){
    				
    			}
      			alert("返回的数据是："+data);
          }  
      });  
      JS.Engine.start('/leedaneMVC/conn?channel=chat_room'); 
      JS.Engine.on('start',function(cid){
    	connId = cid;
      	console.log("长链接连接"+cid);
      });
      JS.Engine.on('stop',function(cause, cid, url, engine){//页面刷新执行
      	console.log("长链接已经断连接"+cid);
      	connId = cid;
      	//移除id
      	$.ajax({
				type : "post",
				data : "cid=" + cid+"&channel=chat_room",
				url : "<%=basePath %>destroyedComet4jServlet",
				async: false,
				//dataType : "json",
				timeout:2000,
				cache : false,
				beforeSend : function() {
				},
				success : function(data) {
					console.log("移除id"+cid);
				},
				error : function() {
				}
		});
      });
	}
	  
	//回车执行登录
	document.onkeydown=function(event){
	    var e = event || window.event || arguments.callee.caller.arguments[0];
	    if(e && e.keyCode==13){ // enter 键
	    	doLogin();
	   }
	}
	 
	//页面关闭和刷新执行方法
	window.onbeforeunload = onbeforeunload_handler;
	function onbeforeunload_handler() {//页面关闭执行
		if(connId != ""){
			$.ajax({
				type : "post",
				data : "cid=" + connId +"&channel=chat_room",
				url : "<%=basePath %>destroyedComet4jServlet",
				async: false,
				//dataType : "json",
				timeout:2000,
				cache : false,
				beforeSend : function() {
				},
				success : function(data) {
					console.log("移除id"+cId);
				},
				error : function() {
				}
			});
		}
	}
	</script>
</html>