<%@page import="com.cn.leedane.utils.CommonUtil"%>
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
	String bp = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/";
	
	String loginLink = "";
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
	}else{
		loginLink = bp +"page/login.jsp?ref="+ CommonUtil.getFullPath(request) +"&t="+UUID.randomUUID().toString();
	}
	
	String bid = request.getParameter("bid");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
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
		.original{
			border-color: #51BAE3;
  			color: #51BAE3;
  			box-sizing: inherit;
  			-webkit-tap-highlight-color: transparent;
  			border: 2px solid;
		    border-radius: 50%;
		    width: 25px;
		    height: 25px;
		    vertical-align: text-bottom;
		    text-align: center;
		    font-size: 16px;
		    line-height: 26px;
		    float: left;
		}
		.inline{
			display: inline-block !important;
		}
		.marginRight{
			margin-right: 12px;
		}
		.comment-list{
			border:1px solid #f5f5f5;
			box-shadow: 1px 0px 5px #888888;
			margin-bottom: 10px;
		}
		.comment-list-padding{
			padding: 5px;
			border-radius: 3px; 
		}
		.comment-list-item{
			background-color: #fff !important;
			border-color: #f5f5f5 !important;
		}
		.publish-time{
			color: #999;
		}
		.text-align-right{
			text-align: right;
		}
		.comment-list:hover {
			border: 1px dashed #999;
		}
		.tag{
			margin-right: 5px;
		}
	</style>
</head>
<body>
<%@ include file="/page/common.jsp" %>
<script type="text/javascript" src="other/layui/layui.js"></script>
<script type="text/javascript" src="other/layui/lay/dest/layui.all.js"></script>
<script src="<%=basePath %>page/js/detail.js"></script>

</body>
	<input type="hidden" name="blogId" value="<%=bid %>">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<div class="inline" style="margin-bottom: -3px;">
					<span class="original">原</span>
				</div>
				<div class="inline" style="margin-bottom: -3px;">
					<span class="original">荐</span>
				</div>
				<div class="h3 inline" id="b-title"></div>
			</div>
			<div class="col-lg-12">
				<a href="JavaScript:void(0);" target="_blank" class="marginRight" id="b-account"></a>
				<span class="marginRight" id="b-create-time"></span>
				<span class="marginRight" id="b-read-time"></span>
				<a href="#comment" class="marginRight" id="b-comment-number"></a>
				<span class="marginRight" id="b-transmit-number"></span>
				<span class="marginRight" id="b-zan-number"></span>
				<span class="marginRight" id="b-share-number"></span>
			</div>
			<div class="col-lg-12" id="tags"></div>
		</div>
	   <div class="row">
	   		<div class="col-lg-12 row-content">
	   		
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12">
	   			<ul class="pager">
				    <li><a href="#">Previous</a></li>
				    <li><a href="#">Next</a></li>
				</ul>
	   		</div>
	   </div>
	    
	   <div class="row">
	   		<div class="col-lg-12" id="keywords">
	   			<span class="marginRight">关键字：</span>
				<a href="#" target="_blank" class="marginRight">leedane</a>
				<a href="#" target="_blank" class="marginRight">leedane</a>
				<a href="#" target="_blank" class="marginRight">leedane</a>
				<a href="#" target="_blank" class="marginRight">leedane</a>
				<a href="#" target="_blank" class="marginRight">leedane</a>
			</div>
	   </div>
	   <div class="row" id="comment">
	   		<% if(!isLogin){ %>
	   		<div class="col-lg-12" style="text-align: center;">
			        您还未登录，无法参与评论 <a type="button" class="btn btn-info" href="<%=loginLink %>">登录</a>
			</div>
			<%}else{ %>
			<div class="col-lg-6" style="text-align: center;">
			     <form class="form-signin" role="form">
				     <fieldset>
					     <textarea class="form-control" name="add-comment" style="min-height: 125px;" autofocus></textarea>
					     <button class="btn btn-lg btn-primary btn-block" type="button" style="margin-top: 10px;" onclick="addComment(this);">发表评论</button>
				     </fieldset>
				 </form>
			</div>
			<%} %>
	   </div>
	   <!-- <div class="row comment-list comment-list-padding">
	   		<div class="col-lg-1">
	   			<img src="http://7xnv8i.com1.z0.glb.clouddn.com/1_1_20160727183009_1469401739017.jpg" width="100%" height="70px" class="img-rounded">
	   		</div>
	   		<div class="col-lg-11">
			       <div class="list-group">
			       		<div class="list-group-item comment-list-item active">
			       			<a href="#" target="_blank">leedane</a>
			       			<span class="marginRight publish-time">发表于:2016-10-10 10:10</span>
			       			<span class="marginRight publish-time">来自:小米手机</span>
			       		</div>
			       		<div class="list-group-item comment-list-item">
			       			<div class="row">
			       				<div class="col-lg-12">nginx 已经做tcp负载，cool</div>
			       			</div>
			       			<div class="row">
			       				<div class="col-lg-offset-11 col-lg-1 text-align-right">
			       					 <button class="btn btn-sm btn-primary btn-block" style="width: 60px;" type="button">回复TA</button>
			       				</div>
			       				
		       					<div class="col-lg-12 reply-container">
		       						<div class="row">
			       						<div class="col-lg-12">
			       							<form class="form-signin" role="form">
											     <fieldset>
												     <textarea class="form-control" placeholder="@leedane" autofocus> </textarea>
											     </fieldset>
											 </form>
			       						</div>
			       						<div class="col-lg-offset-11 col-lg-1 text-align-right" style="margin-top: 10px;">
			       							<button class="btn btn-sm btn-primary btn-block" style="width: 60px;" type="button">评论</button>
			       						</div>
		       						</div>
		       					</div>
			       			</div>
			       		</div>
			       </div>
			</div>
	   </div> -->
	</div>

	<script type="text/javascript">
		var isLogin = <%=isLogin %>;
	</script>
</html>