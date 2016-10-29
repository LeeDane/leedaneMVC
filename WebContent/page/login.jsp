<%@page import="com.cn.leedane.utils.StringUtil"%>
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
	String ref = request.getParameter("ref");
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
		//已经登录，直接跳转
		if(StringUtil.isNull(ref)){
			String bp = request.getScheme()+"://"+request.getServerName()
					+":"+request.getServerPort()+request.getContextPath()+"/";
			//跳转回到首页
			ref = bp +"page/index.jsp?&t="+UUID.randomUUID().toString();
		}
		response.sendRedirect(ref);
	}
	String basePath = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/"; 
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>用户登录</title>
    <!-- <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    百度JQUERYCDN
	<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
	<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
	
	<link rel="stylesheet" href="<%=basePath %>page/other/bootstrap-3.3.0/css/bootstrap.min.css">
	<!-- 百度JQUERYCDN -->
	<script type="text/javascript" src="<%=basePath %>page/other/jquery-1.9.1.min.js"></script>
	<script src="<%=basePath %>page/other/bootstrap-3.3.0/js/bootstrap.min.js"></script>
	
	<script src="<%=basePath %>page/js/base.js"></script>
	<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
	<script type="text/javascript" src="<%=basePath %>page/other/layui/layui.js"></script>
	<script type="text/javascript" src="<%=basePath %>page/other/layui/lay/dest/layui.all.js"></script>
    <style type="text/css">
    	body {
		  padding-top: 40px;
		  padding-bottom: 40px;
		  background-color: #eee;
		}
		
		.form-signin {
		  max-width: 330px;
		  padding: 15px;
		  margin: 0 auto;
		}
		.form-signin .form-signin-heading,
		.form-signin .checkbox {
		  margin-bottom: 10px;
		}
		.form-signin .checkbox {
		  font-weight: normal;
		}
		.form-signin .form-control {
		  position: relative;
		  height: auto;
		  -webkit-box-sizing: border-box;
		     -moz-box-sizing: border-box;
		          box-sizing: border-box;
		  padding: 10px;
		  font-size: 16px;
		}
		.form-signin .form-control:focus {
		  z-index: 2;
		}
		.form-signin input[type="email"] {
		  margin-bottom: -1px;
		  border-bottom-right-radius: 0;
		  border-bottom-left-radius: 0;
		}
		.form-signin input[type="password"] {
		  margin-bottom: 10px;
		  border-top-left-radius: 0;
		  border-top-right-radius: 0;
		}
    </style>
  </head>
  

  <body>

    <div class="container">
	      <form class="form-signin" role="form">
	        <h2 class="form-signin-heading">登录</h2>
	        <input type="text" id="account" class="form-control" placeholder="Email address" autofocus>
	        <input type="password" id="password" class="form-control" style="margin-top: 10px;" placeholder="Password" data-placement="bottom">
	        <div class="checkbox">
	          <label>
	            <input type="checkbox" value="remember-me"> Remember me
	          </label>
	        </div>
	        <button class="btn btn-lg btn-primary btn-block" type="button">Sign in</button>
	      </form>

    </div> <!-- /container -->
  </body>
  <script type="text/javascript">
  $(function () {	  
      $(".btn").on("click", function(){
    	  var account = $("#account").val();
    	  if(isEmpty(account)){
    		  layer.msg("请输入账号");
    		  return;
    	  }
    	  
    	  var password = $("#password").val();
    	  if(isEmpty(password)){
    		  layer.msg("请输入密码");
    		  return;
    	  }
    	  var params = {account: account, password: $.md5(password), t: Math.random()};
    	  var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
    	  $.ajax({
    			type : "post",
    			data : params,
    			url : "<%=basePath %>leedane/user/login.action",
    			dataType: 'json',
    			withCredentials:true,
    			beforeSend:function(request){
    			},
    			success : function(data) {
    				layer.close(loadi);
    				layer.msg(data.message);
    				if(data.isSuccess)
    					window.location.href="<%= ref%>";
    			},
    			error : function() {
    				layer.close(loadi);
    				layer.msg("网络请求失败");
    			}
    		});
      });
  });
  </script>
</html>