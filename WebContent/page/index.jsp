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
	
	//是否隐藏头部
	String noHeaderStr1 = request.getParameter("noHeader");
	boolean noHeader1 = false;
	if(StringUtil.isNotNull(noHeaderStr1)){
		noHeader1 = StringUtil.changeObjectToBoolean(noHeaderStr1);
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>LeeDane官网首页</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<style type="text/css">
		.clearFloat{
			clear: both;
		}
		.main{
			<% if(!noHeader1){ %>
			margin-top: 50px;
			<% }%>
			background-color: #f5f5f5;
		}
		.main_bg{
			width: 100%;
			height: 200px;
		}
		<% if(!noHeader1){ %>
		.container{
			margin-top: -80px;
		}
		<% }%>
		.row{
			margin-top: 15px;
		}
		.panel-footer button{
			margin-top: 5px;
			margin-right: 5px;
		}
		.tag{
			margin-right: 5px;
		}
		#main-container .page-header{
			padding-bottom: 0px !important;
			margin: 0 0 0 !important;
			border-bottom: 0px !important;
		}
		#main-container .breadcrumb{
			margin-bottom: 10px !important;
		}
		#main-container .panel{
			margin-bottom: 2px !important;
		}
		#main-container .panel-info{
			border-color: #f5f5f5 !important;
		}
		#main-container .has-img-panel-info{
			min-height: 324px;
		}
		#main-container .panel-footer{
			background-color: #fff !important;
		}
		#main-container .panel-info>.panel-heading{
			background-color: #fff !important;
			border-color: #f5f5f5 !important;
		}
		.col-padding-eight{
			padding-right: 8px !important;
			padding-left: 8px !important;
		}
		#main-container .panel-body{
			overflow: hidden;
		}
		
		#report-blog{
			margin-top: 60px;
		}
		.checkbox-inline{
			padding-left: 0px !important;
		}
		.background-white{
			background-color: #fff !important;
		}
	</style>
</head>
<body>
<%@ include file="common.jsp" %>
<script src="<%=basePath %>page/js/index.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
<div class="main clearFloat">
	<% if(!noHeader1){ %>
	<div class="main_bg"></div>
	<%} %>
	<div class="container" id="main-container">
	   <!-- <div class="row">
	      <div class="col-lg-4">
	      	  	<img width="100%" height="100%" class="img-rounded" alt="" src="images/main_content_img.jpg">
	      </div>
	      <div class="col-lg-8" style="height:424px;">
				<div class="panel panel-info">
					<div class="panel-heading">
						<div class="page-header">
						    <h1>问个人博客的重要性问个人博客的重要性问个人博客的重要性
						        <small>
						        	
								</small>
						    </h1>
						    <ol class="breadcrumb">
						    	<li>文章</li>
								<li class="active">2016-10-10 10:10:08</li>
								<li>10</li>
								<li class="active">18</li>
							</ol>
							<span class="label label-default">默认标签</span>
							<span class="label label-primary">主要标签</span>
							<span class="label label-success">成功标签</span>
						</div>
					</div>
					<div class="panel-body">
						母亲说如今社会基本不可能喝上手工茶了，我该学会如何好好珍惜与品味… 母亲是在我要出发广州工作的离家前一天制作好的新茶，虽说不是特意为我准备的，但这份茶情却浓而不烈，绵延不断。我也不确保多年以后是否还能享受这份浓情，于是珍惜当下才是重要的！
					</div>
					<div class="panel-footer">
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-heart"></span> 关注
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-edit"></span> Edit
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-trash"></span> Delete
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-phone"></span> Android
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-user"></span> LeeDane
										</button>
						<button type="button" class="btn btn-primary">阅读全文</button>
						<div class="btn-group">
							<button type="button" class="btn btn-primary dropdown-toggle btn-default" 
									data-toggle="dropdown">
								原始 <span class="caret"></span>
							</button>
							<ul class="dropdown-menu" role="menu">
								<li><a href="#">功能</a></li>
								<li><a href="#">另一个功能</a></li>
								<li><a href="#">其他</a></li>
								<li class="divider"></li>
								<li><a href="#">分离的链接</a></li>
							</ul>
						</div>
					</div>
				</div>
	      		
	      </div>      
	   </div>
	   <div class="row">
	      <div class="col-lg-12">
	      	  	<div class="panel panel-info">
					<div class="panel-heading">
						<div class="page-header">
						    <h1>问个人博客的重要性问个人博客的重要性问个人博客的重要性
						        <small>
						        	
								</small>
						    </h1>
						    <ol class="breadcrumb">
						    	<li>文章</li>
								<li class="active">2016-10-10 10:10:08</li>
								<li>10</li>
								<li class="active">18</li>
							</ol>
							<span class="label label-default">默认标签</span>
							<span class="label label-primary">主要标签</span>
							<span class="label label-success">成功标签</span>
						</div>
					</div>
					<div class="panel-body">
						母亲说如今社会基本不可能喝上手工茶了，我该学会如何好好珍惜与品味… 母亲是在我要出发广州工作的离家前一天制作好的新茶，虽说不是特意为我准备的，但这份茶情却浓而不烈，绵延不断。我也不确保多年以后是否还能享受这份浓情，于是珍惜当下才是重要的！
					</div>
					<div class="panel-footer">
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-heart"></span> 关注
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-edit"></span> Edit
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-trash"></span> Delete
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-phone"></span> Android
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-user"></span> LeeDane
										</button>
						<button type="button" class="btn btn-primary">阅读全文</button>
						
					</div>
				</div>
	      </div>     
	   </div>
	   <div class="row">
	      <div class="col-lg-4">
	      	  	<img width="100%" height="100%" class="img-rounded" alt="" src="images/main_content_img.jpg">
	      </div>
	      <div class="col-lg-8" style="height:424px;">
				<div class="panel panel-info">
					<div class="panel-heading">
						<div class="page-header">
						    <h1>问个人博客的重要性问个人博客的重要性问个人博客的重要性
						        <small>
						        	
								</small>
						    </h1>
						    <ol class="breadcrumb">
						    	<li>文章</li>
								<li class="active">2016-10-10 10:10:08</li>
								<li>10</li>
								<li class="active">18</li>
							</ol>
							<span class="label label-default">默认标签</span>
							<span class="label label-primary">主要标签</span>
							<span class="label label-success">成功标签</span>
						</div>
					</div>
					<div class="panel-body">
						母亲说如今社会基本不可能喝上手工茶了，我该学会如何好好珍惜与品味… 母亲是在我要出发广州工作的离家前一天制作好的新茶，虽说不是特意为我准备的，但这份茶情却浓而不烈，绵延不断。我也不确保多年以后是否还能享受这份浓情，于是珍惜当下才是重要的！
					</div>
					<div class="panel-footer">
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-heart"></span> 关注
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-edit"></span> Edit
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-trash"></span> Delete
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-phone"></span> Android
										</button>
						<button type="button" class="btn btn-primary btn-default ">
							  			<span class="glyphicon glyphicon-user"></span> LeeDane
										</button>
						<button type="button" class="btn btn-primary">阅读全文</button>
					</div>
				</div>
	      		
	      </div>      
	   </div> -->
	</div>
	<!-- <i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop" style="font-size: 30px; color: #1E9FFF;">&#xe63e;</i>  --> 
</div>
<!-- 模态框举报列表 -->
<div class="modal fade" id="report-blog" tabindex="-1" role="dialog" aria-labelledby="reportBlogModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="reportBlogModalLabel">
					举报
				</h4>
			</div>
			<div class="modal-body" id="report-modal-body">
				<div class="row">
					<div class="col-lg-12">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-primary report-btn">
								<input type="radio" name="options" id="option1" type-value="1"> 色情低俗
							</label>
							<label class="btn btn-primary report-btn">
								<input type="radio" name="options" id="option2" type-value="2"> 广告骚扰
							</label>
							<label class="btn btn-primary report-btn">
								<input type="radio" name="options" id="option3" type-value="3"> 政治敏感
							</label>
							<label class="btn btn-primary report-btn">
								<input type="radio" name="options" id="option3" type-value="4"> 违法
							</label>
							<label class="btn btn-primary report-btn">
								<input type="radio" name="options" id="option3" type-value="5"> 倾诉投诉
							</label>
							<label class="btn btn-primary report-btn default-report-btn active">
								<input type="radio" name="options" id="option3" type-value="0"> 其他
							</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="form-group">
						  <label for="report-reason">描述信息<font color="red">*</font></label>
						  <textarea class="form-control" name="reason" placeholder="请输入详细的举报内容"></textarea>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="form-group">
						  	<label for="anonymous">是否匿名</label>
						  	<div>
								<label class="checkbox-inline">
									<input type="radio" name="anonymous" value="true" checked="checked"> 匿名举报
								</label>
								<label class="checkbox-inline">
									<input type="radio" name="anonymous" value="false"> 实名举报
								</label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary " onclick="doReport(this);">
					举报
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
</body>
	<script type="text/javascript">
	var isLogin = <%=isLogin %>; //是否已经登录
	var loginUserId = <%=loginUserId %>; //获取登录用户Id
	var isAdmin = <%=isAdmin %>; //是否是管理员
	</script>
</html>