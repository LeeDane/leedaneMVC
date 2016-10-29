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
	
	String bid = request.getParameter("bid");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>文章详情</title>
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
		.btn-group{
			margin-top: 10px;
		}
		.row-content{
			font-family: "华文宋体";
			font-size: 14px;
		}
	</style>
</head>
<body>
<%@ include file="/page/common.jsp" %>
<script type="text/javascript" src="other/layui/layui.js"></script>
<script type="text/javascript" src="other/layui/lay/dest/layui.all.js"></script>
<script src="<%=basePath %>page/js/detail.js"></script>

</body>

<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<div class="btn-group">
				    <button type="button" class="btn btn-default">按钮 1</button>
				    <button type="button" class="btn btn-default">按钮 2</button>
				    <button type="button" class="btn btn-default">按钮 3</button>
				</div>
			</div>
		</div>
	   <div class="row row-content">
	      <div class="col-lg-4" id="column-01">
	      	<img alt="" src="http://d.hiphotos.baidu.com/exp/w=500/sign=bbcb94714b086e066aa83f4b32097b5a/f31fbe096b63f624683231438044ebf81a4ca306.jpg?t=2003">
	      </div>
	      <div class="col-lg-4" id="column-02">
	      
	      </div>     
	      <div class="col-lg-4" id="column-03">
	      
	      </div>
	   </div> 
	   </div>

	<script type="text/javascript">
		var winW = $(window).width(); 
		$(function(){
			var bid = '<%=bid %>';
			if(isEmpty(bid)){
				layer.msg("文章不存在");
				return;
			}
			getInfo(bid);
			getContent(bid);
		});
		
		//获取博客的基本信息(没有内容)
		function getInfo(bid){
			$.ajax({
				type : "post",
				data : {blog_id: bid, t: Math.random()},
				url : getBasePath() +"leedane/blog/getInfo.action",
				dataType: 'json',
				beforeSend:function(){
				},
				success : function(data) {
					if(data.isSuccess){
						document.title = data.message.title;
					}else{
						layer.msg(data.message);
					}
				},
				error : function() {
					layer.msg("网络请求失败");
				}
			});
		}
		
		//获取博客的详细内容
		function getContent(bid){
			var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
			$.ajax({
				type : "post",
				data : {blog_id: bid, t: Math.random()},
				url : getBasePath() +"leedane/blog/getContent.action",
				beforeSend:function(){
				},
				success : function(data) {
					layer.close(loadi);
					$(".row-content").html(data);
				},
				error : function() {
					layer.close(loadi);
					layer.msg("网络请求失败");
				}
			});
		}
	</script>
</html>