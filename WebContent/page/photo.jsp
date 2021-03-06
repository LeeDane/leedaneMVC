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
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
	}else{
		String bp = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/";
		response.sendRedirect(bp +"page/login.jsp?ref="+ CommonUtil.getFullPath(request)+"&t="+UUID.randomUUID().toString());
	}	
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><%=account %>的相册</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<style type="text/css">
		body{
			background-color: #f5f5f5 !important;
		}
		.container{
			margin-top: 50px;
			background-color: #fff !important;
		}
		.row{
			margin-top: 15px;
		}
		.row img{
			cursor: pointer;
		}
		.panel-footer button{
			margin-top: 5px;
			margin-right: 5px;
		}
		
		.add-link{
			z-index: 2000;
			position: absolute;
			right: 0;
		}
		.right-col{
			margin-top: 12px;
		}
		#add-grallery{
			padding-top:200px;
		}
		img{
			transition: .1s transform;
  			transform: translateZ(0);
  			z-index: 0;
		}
		img:hover{
			transform: scale(1.1, 1.1);
  			transition: .3s transform;
  			z-index: 999;
		}
		
	</style>
</head>
<body>
<%@ include file="/page/common.jsp" %>
<script src="<%=basePath %>page/js/photo.js"></script>

<div class="container">
	   <div class="row">
	      <div class="col-lg-3 col-sm-12" id="column-01">
	      
	      </div>
	      <div class="col-lg-3 col-sm-12" id="column-02">
	      
	      </div>     
	      <div class="col-lg-3 col-sm-12" id="column-03">
	      
	      </div>
	      <div class="col-lg-3 col-sm-12 right-col">
	      		<div class="panel panel-default">
				    <div class="panel-heading">
				        <div class="panel-title">温馨提示</div>
				    </div>
				    <div class="panel-body">
				        	本站为免费交流社区。意图为大家提供一个心灵交互的方式，根据国家的法律法规，禁止传播一切色情，暴力，反动等等违法的信息。欢迎加入我们的大家庭！
				    </div>
				</div>
				
				<div class="panel panel-default">
					<div class="panel-body text-center">
						<button class="btn btn-success" data-toggle="modal" data-target="#add-grallery">添加图库</button>
					</div>
				</div>
	      </div>
	   </div> 
</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="add-grallery" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					添加图库
				</h4>
			</div>
			<div class="modal-body">
				<div class="input-group">
				  <span class="input-group-addon">Link</span>
				  <input type="text" class="form-control gallery-link" placeholder="请输入七牛服务器的图片链接">
				</div>
				<br>
				<div class="input-group">
				  <span class="input-group-addon">Desc</span>
				  <input type="text" class="form-control gallery-desc" placeholder="请输入对该图片的描述信息">
				</div>
				<br>
				<div class="input-group">
				  <span class="input-group-addon">Width</span>
				  <input type="number" class="form-control gallery-width" placeholder="请输入该图片的宽度(不知道或不清楚请不要填写)">
				</div>
				<br>
				<div class="input-group">
				  <span class="input-group-addon">Height</span>
				  <input type="number" class="form-control gallery-height" placeholder="请输入该图片的高度(不知道或不清楚请不要填写)">
				</div>
				<br>
				<div class="input-group">
				  <span class="input-group-addon">Length</span>
				  <input type="number" class="form-control gallery-length" placeholder="请输入该图片的长度,k为单位(不知道或不清楚请不要填写)">
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary add-to-gallery">
					添加
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
</body>
	<script type="text/javascript">
		$(function(){			
			$(".navbar-nav .nav-main-li").each(function(){
				$(this).removeClass("active");
			});
			$(".nav-photo").addClass("active");
			$(".add-to-gallery").on("click", function(){
				var link = $(".gallery-link").val();
				if(isEmpty(link)){
					layer.msg("请输入图片的链接");
					$(".gallery-link").focus();
					return;
				}
				
				if(!isLink(link)){
					layer.msg("该图片的链接不合法");
					$(".gallery-link").focus();
					return;
				}
				var params = {path: $(".gallery-link").val(), desc: $(".gallery-desc").val(), width: $(".gallery-width").val(), height: $(".gallery-height").val(), length: $(".gallery-length").val(), t: Math.random()};
				addLink(params);
			});
		});
	</script>
</html>