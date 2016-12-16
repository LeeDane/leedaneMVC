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
	String basePath = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/"; 
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
		//已经登录，直接跳转
		if(StringUtil.isNull(ref)){
			basePath = request.getScheme()+"://"+request.getServerName()
					+":"+request.getServerPort()+request.getContextPath()+"/";
			//跳转回到首页
			ref = basePath +"page/index.jsp?&t="+UUID.randomUUID().toString();
		}
		response.sendRedirect(ref);
	}
	
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
	<script type="text/javascript" src="<%=basePath %>page/js/comet4j.js"></script>
    <style type="text/css">
    	body {
		  padding-top: 40px;
		  padding-bottom: 40px;
		  background-color: #f5f5f5;
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
		.login-btn{
			margin-top: 10px;
		}
		
		#load-qr-code{
			padding-top:150px;
		}
		#load-qr-code-body-ul li {
			list-style-type:none;
		}
    </style>
  </head>
  

  <body onload="init();">

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
	        <button type="button" class="btn btn-info" id="show-login-qr-code-btn">二维码登录</button>
	        <button class="btn btn-lg btn-primary btn-block login-btn" type="button">Sign in</button>
	      </form>

    </div> <!-- /container -->
    
    <!-- 模态框发布心情列表 -->
<div class="modal fade" id="load-qr-code" tabindex="-1" role="dialog" aria-labelledby="LoadQrCodeModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="LoadQrCodeModalLabel">
					请用LeeDane官方App扫描下方二维码
				</h4>
			</div>
			<div class="modal-body">
				提示：
				<ul class="list-group" id="load-qr-code-body-ul">
				    <li>请将app取景框对准下面的二维码</li>
				    <li>使二维码全部包含在取景框内</li>
				    <li>保留一定的距离</li>
				</ul>
				<p></p>
				<div style="text-align: center;">
					<img id="modal-qr-code-img" alt="二维码" src="" width="200px" height="200px">
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
  </body>
  <script type="text/javascript">
  var connId = ""; //当前页面的连接ID
  $(function () {	  
      $(".login-btn").on("click", function(){
    	  doLogin();
      });
      
      $("#show-login-qr-code-btn").on("click", function(){
    	  loadQRCode();
    	  $("#load-qr-code").modal("show");
      });
      
  });
  
  function doLogin(){
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
  }
  
  function init(){
      JS.Engine.on({  
    	  scan_login : function(data){
    		 	data = eval('(' + data + ')');
    			if(data.isSuccess){
    				if(data.message && "cancel" == data.message){
    					//window.close();
    					window.open("about:blank","_self").close();
    					//window.open("","_self").close()
    				}else
    					window.location.reload();
    			}
      			//alert("返回的数据是："+data);
          }  
      });  
      JS.Engine.start('/leedaneMVC/conn?channel=scan_login'); 
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
				data : "cid=" + cid+"&channel=scan_login",
				url : "<%=basePath %>destroyedComet4jServlet",
				async: false,
				//dataType : "json",
				timeout:1000,
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
  window.onunload = onunload_handler;
  function onbeforeunload_handler() {//页面关闭执行
  	if(connId != ""){
  		$.ajax({
  			type : "post",
  			data : "cid=" + connId +"&channel=scan_login",
  			url : "<%=basePath %>destroyedComet4jServlet",
  			async: false,
  			//dataType : "json",
  			timeout:1000,
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
  	//return connId;
  }
  function onunload_handler() {//页面关闭执行
		<%-- if(connId != ""){
			$.ajax({
				type : "post",
				data : "cid=" + connId+"&channel=scan_login",
				url : "<%=basePath %>destroyedComet4jServlet",
				async: false,
				//dataType : "json",
				timeout:1000,
				cache : false,
				beforeSend : function() {
				},
				success : function(data) {
					console.log("移除id"+cId);
				},
				error : function() {
				}
			});
		} --%>
		//return connId;
	}
  
  function loadQRCode(){
	  var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	  $.ajax({
			type : "post",
			data : "cid="+connId,
			url : "<%=basePath %>loginQrCode",
			dataType: 'json', 
			beforeSend:function(){
			},
			success : function(data) {
				layer.close(loadi);
				if(data.isSuccess){
					$("#modal-qr-code-img").attr("src", data.message);
				}else{
					layer.msg(data.message);
				}
				console.log(data);
			},
			error : function() {
				layer.close(loadi);
				layer.msg("网络请求失败");
			}
		});
  }
  </script>
</html>