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
	boolean isLoginUser = false;
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
	}else{
		String bp = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/";
		response.sendRedirect(bp +"page/login.jsp?ref="+request.getRequestURL()+"&t="+UUID.randomUUID().toString());
	}
	String uid = request.getParameter("uid");
	
	if(StringUtil.isNull(uid) && userBean != null)
		uid = String.valueOf(userBean.getId());
	
	if(userBean != null && StringUtil.isNotNull(uid))
		isLoginUser = (Integer.parseInt(uid) == userBean.getId());
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><%=account %>的个人中心</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_733gvu39kn265hfr.css">
	
	<script src="js/base.js"></script>
	<style type="text/css">
		
		.mainContainer{
			padding-top: 60px;
			width: 1300px !important;
		}
		. mainContainer .row{
			margin-top: 15px;
		}
		.mainContainer .row img{
			cursor: pointer;
		}
		body {
			position: relative;
		}
		.mainContainer ul.nav-pills {
			top: 80px;
			position: fixed;
		}
		div.col-sm-9 div {
			height: 250px;
			font-size: 28px;
		}
		#section1 {color: #fff; background-color: #1E88E5;}
		#section2 {color: #fff; background-color: #673ab7;}
		#section3 {color: #fff; background-color: #ff9800;}
		#section41 {color: #fff; background-color: #00bcd4;}
		#section42 {color: #fff; background-color: #009688;}

		@media screen and (max-width: 810px) {
			#section1, #section2, #section3, #section41, #section42  {
				margin-left: 150px;
			}
		}
		
		#user_img{
			 text-align:center;
			 margin-top: 10px;
		}
		#user_img img{
			vertical-align:middle;
			border: 5px double #eee;
		}
		#user_desc{
			text-align:center;
			padding-bottom: 20px;
		}
		
		#edit-user-info{
			padding-top:150px;
		}
		#float-month li{
			text-align: center;
		}
		.cursor{
			cursor: pointer;
		}
		.location{
			color: #666;
			font-family: "华文宋体";
			font-size: 10px;
			margin: 8px 0 8px 0 !important;
		}
		.zan_user{
			margin: 8px 0 8px 0;
		}
		.list-group-item-operate > button{
			margin-right: 5px;
		}
		#operate-item-list{
			padding-top: 150px;
		}
		.tip-list{
			padding-bottom: 3px;
		}
		.tip-list span{
			padding: .3em .6em .3em !important;
			margin-right: 5px;
		}
		#comment-or-transmit-list{
			padding-top: 150px;
		}
		.commentOrTransmitUser{
			width: 60px;
			white-space:nowrap; 
			overflow:hidden; 
			text-overflow:ellipsis;
			cursor: pointer;
			text-align: center;
			margin-top: 5px;
		}
		#comment-or-transmit-item{
			height: 400px !important;
			overflow: auto !important;
		}
		#comment-or-transmit-item .badge{
			background-color: #3071a9 !important;
			margin-right: 5px;
		}
	</style>
</head>
<body data-spy="scroll" data-target="#myScrollspy" data-offset="90">
<%@ include file="/page/common.jsp" %>
<script type="text/javascript" src="other/layui/layui.js"></script>
<script type="text/javascript" src="other/layui/lay/dest/layui.all.js"></script>
<script src="<%=basePath %>page/js/my.js"></script>
<div class="container mainContainer">
	   <div class="row">
		   <div class="col-lg-3">
		   		<div class="row" id="user_img">
		   			<!-- <img src="images/main_content_img.jpg" width="200px" height="200px" class="img-circle"> -->
		   		</div>
		   		<div class="row" id="user_desc">
		   			<!-- <div class="h3">
		   				LeeDane
			   			<button type="button" class="btn btn-primary btn-xs">
						  <span class="glyphicon glyphicon-pencil"></span> 编辑个人资料
						</button>
		   			</div>
		   			<div class="h4" style="height: 38px;overflow-y:auto;">leeDane很懒，啥也没写leeDane很懒，啥也没写leeDane很懒，啥也没写leeDane很懒，啥也没写</div>
		   		 -->
		   		</div>
		   		<div class="row" id="user_info">
		   		</div>
		   </div>
	      <div class="col-lg-6 scrollspy-example" id="mood-container" data-spy="scroll" data-target="#navbarExample" data-offset="50">
	      
		      	<!-- <div class="list-group">
				    <div class="list-group-item active">
				    	<div class="row">
				    		<div class="col-lg-8">
				    			<span class="list-group-item-heading">
						            	来自：小米手机5s plus
						        </span>
				    		</div>
				    		<div class="col-lg-4">
				    			<span class="list-group-item-heading" style="margin-right: 5px;">
						        	2016-10-10 10:00:01
						        </span>
				    			<span class="list-group-item-heading glyphicon glyphicon-chevron-down cursor">
						        </span>
						        
				    		</div>
				    	</div>
				        
				    </div>
				    <div class="list-group-item">
				        <div class="list-group-item-text">
				            	今天解决心情列表到最底部下滑时候程序奔溃的问题，原因是：viewfooter通过匿名方式加载进来没有赋值给mViewFooter变量所致。
				        </div>
				        <p class="location">	
				        	位置:郑州市二七路110号郑州市公安局
				        </p>
				        <div class="row">
				          	<div class="col-lg-4 col-sm-4">
				          		<img src="images/main_content_img.jpg" width="100%" height="180px" class="img-responsive">
				          	</div>
				          	<div class="col-lg-4 col-sm-4">
				          		<img src="images/main_content_img.jpg" width="100%" height="180px" class="img-responsive">
				          	</div>
				          	<div class="col-lg-4 col-sm-4">
				          		<img src="images/main_content_img.jpg" width="100%" height="180px" class="img-responsive">
				          	</div>
				        </div>
				        <div class="zan_user">leedane、hehe等觉得很赞</div>
				    </div>
				    <div class="list-group-item list-group-item-operate">
				         <button type="button" class="btn btn-primary btn-sm">评论(10)</button>
				         <button type="button" class="btn btn-primary btn-sm" disabled="disabled">转发(10)</button>
				         <button type="button" class="btn btn-primary btn-sm">查看详细</button>
				    </div>
				</div>
		
				<div id="section2"> 
					<h1>Section 2</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>        
				<div id="section3">         
					<h1>Section 3</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>   
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>   
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>   
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>   
				<div id="section6" onclick="testClick(this)">         
					<h1>sectionuuuuyy</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section888</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section6">         
					<h1>section6</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>  
				<div id="section41">         
					<h1>Section 4-1</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div>      
				<div id="section42">         
					<h1>Section 4-2</h1>
					<p>Try to scroll this section and look at the navigation list while scrolling!</p>
				</div> -->
	      </div>
	      <nav class="col-sm-3" id="navbarExample">
		      	<ul class="nav nav-pills nav-stacked" id="float-month">
					<li>
						<div class="panel-body text-center">
							<button class="btn btn-success" data-toggle="modal" data-target="#add-mood" onclick="shoqSendMoodDialog();">发布心情</button>
						</div>
					</li>
				</ul>
	        <!-- <ul class="nav nav-pills nav-stacked">
				<li class="active"><a href="#section1">Section 1</a></li>
				<li><a href="#section2">Section 2</a></li>
				<li><a href="#section3">Section 3</a></li>
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#">Section 4 <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="#section41">Section 4-1</a></li>
						<li><a href="#section42">Section 4-2</a></li>                     
					</ul>
				</li>
			</ul> -->
	      </nav>
	   </div> 
</div>

<!-- 模态框修改用户基本信息 -->
<div class="modal fade" id="edit-user-info" tabindex="-1" role="dialog" aria-labelledby="editUserInfoModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="editUserInfoModalLabel">
					编辑基本信息
				</h4>
			</div>
			<div class="modal-body modal-body-edit-userinfo">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary edit-user-info-btn">
					编辑
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 模态框操作选项列表 -->
<div class="modal fade" id="operate-item-list" tabindex="-1" role="dialog" aria-labelledby="operateItemListModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="operateItemListModalLabel">
					操作列表
				</h4>
			</div>
			<div class="modal-body">
				<ul class="list-group" id="operate-list">
				   
				</ul>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 模态框评论、转发列表 -->
<div class="modal fade" id="comment-or-transmit-list" tabindex="-1" role="dialog" aria-labelledby="commentOrTransmitListModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="commentOrTransmitListModalLabel">
					评论/转发列表
				</h4>
			</div>
			<div class="modal-body" id="comment-or-transmit-item">
				<!-- <div class="list-group">
					<div class="list-group-item">
						<div class="row">
							<div class="col-lg-2 col-sm-2">
								<div>
									<img src="images/main_content_img.jpg" width="60px" height="60px" class="img-circle">
								</div>
								<div class="commentOrTransmitUser text-info">leedanssfffffffdddse</div> 
							</div>
							<div class="col-lg-10 col-sm-10">
								<div class="row">
									<div class="col-lg-8 col-sm-8 text-muted">来自：机器人自动回复 </div>
									<div class="col-lg-4 col-sm-4 text-muted" style="text-align: right;">
										14天前<button type="button" class="btn btn-primary btn-xs" 
											    data-toggle="button" style="margin-left: 5px;">回复
											</button>
									</div>
								</div>
								<div>你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗</div>
							</div>
						</div>
					</div>
				</div> -->
				
				<button class="btn btn-info">加载更多</button>
			</div>
			<div class="modal-footer">
				<!-- <div class="form-group">
					<input type="text" class="form-control">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
				</div> -->
				<div class="input-group">
			      <input type="text" id="comment-or-transmit-text" class="form-control" placeholder="请说点什么吧">
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button" onclick="sendCommentOrTransmit();">发送</button>
			      </span>
			    </div><!-- /input-group -->
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 模态框发布心情列表 -->
<div class="modal fade" id="push-mood" tabindex="-1" role="dialog" aria-labelledby="pushMoodListModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="pushMoodListModalLabel">
					发布心情
				</h4>
			</div>
			<div class="modal-body">
				<ul>
					<li>发布规则</li>
					<li>发布规则</li>
					<li>发布规则</li>
					<li>发布规则</li>
					<li>发布规则</li>
				</ul>
			</div>
			<div class="modal-footer">
				<!-- <div class="form-group">
					<input type="text" class="form-control">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
				</div> -->
				<div class="input-group">
			      <input type="text" id="push-mood-text" class="form-control" placeholder="请说点什么吧">
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button" onclick="sendMood();">发送</button>
			      </span>
			    </div><!-- /input-group -->
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
</body>
	<script type="text/javascript">
		var uid = '<%=uid %>';
		var isLoginUser = <%=isLoginUser %>;//是否是登录用户
		$(function(){			
			$(".navbar-nav .nav-main-li").each(function(){
				$(this).removeClass("active");
			});
			$(".nav-my").addClass("active");
			
			$(window).scroll(function (e) {
				e = e || window.event;
			    if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件             
			        if (e.wheelDelta > 0) { //当滑轮向上滚动时
			            return;
			        }
			    } else if (e.detail) {  //Firefox滑轮事件
			        if (e.detail> 0) { //当滑轮向上滚动时
			            return;
			        }
			    }
			    var scrollT = $(window).scrollTop(); //滚动条top 
			    console.log("scrollT==="+scrollT);
			}); 
			
			loadUserInfo();
			getMoods();
			
			$(".edit-user-info-btn").on("click", function(){
				var json = serializeArrayToJsonObject($(".myForm").serializeArray());
				editUserinfo(json);
			});
		});
		
		
	</script>
</html>