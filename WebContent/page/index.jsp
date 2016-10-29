<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cn.leedane.controller.UserController"%>
<%
	Object obj = session.getAttribute(UserController.USER_INFO_KEY);
	boolean isLogin = obj != null;
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>LeeDane官网首页</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<script src="js/base.js"></script>
	<style type="text/css">
		.clearFloat{
			clear: both;
		}
		.main{
			margin-top: 50px;
		}
		.main_bg{
			width: 100%;
			height: 200px;
		}
		
		.container{
			margin-top: -80px;
		}
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
	</style>
</head>
<body>
<%@ include file="common.jsp" %>

<script type="text/javascript" src="<%=basePath %>page/other/layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/layui/lay/dest/layui.all.js"></script>
<script src="<%=basePath %>page/js/index.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
<div class="main clearFloat">
	<div class="main_bg"></div>
	
	<div class="container">
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
	<i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop" style="font-size: 30px; color: #1E9FFF;">&#xe63e;</i>  
</div>

</body>
	<script type="text/javascript">
	var isLogin = <%=isLogin %>; //是否已经登录
	</script>
</html>