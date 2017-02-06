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
		body{
			background-color: #f5f5f5 !important;
		}
		.mainContainer{
			padding-top: 60px;
		}
		
		.user-account{
			text-align: center;
			margin-top: 5px;
			cursor: pointer;
		}
		.mood-list-row {
		    margin-top: 16px;
		    border: 1px solid #ddd;
		    background-color: #fff !important;
		    border-radius: 4px;
		    padding-bottom: 8px;
		    padding-top: 8px;
		}
		
		.blog-list-row {
		    margin-top: 16px;
		    border: 1px solid #ddd;
		    background-color: #fff !important;
		    border-radius: 4px;
		    padding-bottom: 8px;
		    padding-top: 8px;
		}
	</style>
</head>
<body>
<%@ include file="common.jsp" %>
<script src="<%=basePath %>page/js/search.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
<div class="container mainContainer">
	   <div class="row">
	   		<div class="col-lg-5">
	   			<div class="input-group">
		   			<input type="text" id="common-search-text" class="form-control">
	                <span class="input-group-btn">
	                    <button class="btn btn-primary btn-default" type="button" onclick="searchCommon(this);">Go!</button>
	                </span>
                </div>
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12" style="margin-top: 10px;">
	   			提示：最多展示最符合条件的10个用户、10条心情、10篇文章，本次检索耗时<span id="search-need-time" class="color-red">1000</span>s.
	   		</div>
	   </div>
	   
	   <div class="row">
	   		<div class="col-lg-12" style="margin-top: 10px;">
	   			本次检索到<span class="color-blue">用户</span>有&nbsp;<span id="search-user_number" class="color-red">100</span>&nbsp;个：
	   		</div>
	   </div>
	   
	   <div class="row">
	   		<div class="col-lg-12 col-sm-12" style="margin-top: 10px;">
	   			<div class="row">
	   				<div class="col-lg-1 col-sm-1">
    		 			<img class="img-circle" alt="" width="100%" height="60" src="http://7xnv8i.com1.z0.glb.clouddn.com/leedane_app_upload_ec5da83b-4fd9-4924-ad04-3264dfc52a9843d8c1ec-95d3-4cc7-aa12-9379a2196df2_Screenshot_20161216-122520_01.png">
    		 			<div class="user-account cut-text" title="LeeDane随时随地多多对对对">LeeDane随时随地多多对对对</div>
    		 		</div>
    		 		<div class="col-lg-1 col-sm-1">
    		 			<img class="img-circle" alt="" width="100%" height="60" src="http://7xnv8i.com1.z0.glb.clouddn.com/leedane_app_upload_ec5da83b-4fd9-4924-ad04-3264dfc52a9843d8c1ec-95d3-4cc7-aa12-9379a2196df2_Screenshot_20161216-122520_01.png">
    		 			<div class="user-account cut-text" title="LeeDane"><a href="JavaScript:void(0);" onclick="linkToMy(1)">LeeDane</a></div>
    		 		</div>
    		 		<div class="col-lg-1 col-sm-1">
    		 			<img class="img-circle" alt="" width="100%" height="60" src="http://7xnv8i.com1.z0.glb.clouddn.com/leedane_app_upload_ec5da83b-4fd9-4924-ad04-3264dfc52a9843d8c1ec-95d3-4cc7-aa12-9379a2196df2_Screenshot_20161216-122520_01.png">
    		 			<div class="user-account cut-text" title="LeeDane">LeeDane</div>
    		 		</div>
    		 		<div class="col-lg-1 col-sm-1">
    		 			<img class="img-circle" alt="" width="100%" height="60" src="http://7xnv8i.com1.z0.glb.clouddn.com/leedane_app_upload_ec5da83b-4fd9-4924-ad04-3264dfc52a9843d8c1ec-95d3-4cc7-aa12-9379a2196df2_Screenshot_20161216-122520_01.png">
    		 			<div class="user-account cut-text" title="LeeDane">LeeDane</div>
    		 		</div>
    		 	</div>
	   		</div>
	   	</div>
	   	
	   <div class="row">
	   		<div class="col-lg-12" style="margin-top: 10px;">
	   			本次检索到<span class="color-blue">心情</span>有&nbsp;<span class="color-red">100</span>&nbsp;个：
	   		</div>
	   </div>
	   
	   <div class="row mood-list-row">
	   		<div class="col-lg-1" style="text-align: center;margin-top: 10px;">
				<img width="60" height="60" class="img-circle hand" alt="" src=""  onclick="showSingleImg(this);"/>
			</div>
			<div class="col-lg-10">
				<div class="row" style="font-family: '微软雅黑'; font-size: 15px; margin-top: 10px;">
					<div class="col-lg-12">
						<span class="mood_user_name">leedane</span>   
						<span class="mood_create_time">2017-01-01 00:00</span>   
						<span class="mood_froms">android</span>
					</div>
				</div>
				<div class="row" style="font-family: '微软雅黑'; font-size: 17px;margin-top: 5px;">
					<div class="col-lg-12">今天天气不错啊！</div>
				</div>
				<div class="row" style="font-family: '宋体'; font-size: 12px;margin-top: 5px; color: gray; margin-bottom: 10px;">
					<div class="col-lg-12">
						<button type="button" class="btn btn-primary btn-sm" onclick="goToReadFull(366)">查看详细</button>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-sm-4">
						<img width="100%" height="180" class="img-circle hand" alt="" src=""  onclick="showImg(this);"/>
					</div>
					<div class="col-lg-4 col-sm-4">
						<img width="100%" height="180" class="img-circle hand" alt="" src=""  onclick="showImg(this);"/>
					</div>
					<div class="col-lg-4 col-sm-4">
						<img width="100%" height="180" class="img-circle hand" alt="" src=""  onclick="showImg(this);"/>
					</div>
				</div>
			</div>
	</div>
	
	<div class="row">
	   		<div class="col-lg-12" style="margin-top: 10px;">
	   			本次检索到<span class="color-blue">文章</span>有&nbsp;<span class="color-red">100</span>&nbsp;篇：
	   		</div>
	   </div>
	   
	   <div class="row blog-list-row">
	   		<div class="col-lg-1" style="text-align: center;margin-top: 10px;">
				<img width="60" height="60" class="img-circle hand" alt="" src=""  onclick="showSingleImg(this);"/>
			</div>
			<div class="col-lg-10">
				<div class="row" style="font-family: '微软雅黑'; font-size: 15px; margin-top: 10px;">
					<div class="col-lg-12">
						<span class="mood_user_name">leedane</span>   
						<span class="mood_create_time">2017-01-01 00:00</span>   
						<span class="mood_froms">android</span>
					</div>
				</div>
				<div class="row" style="font-family: '微软雅黑'; font-size: 17px;margin-top: 5px;">
					<div class="col-lg-12">文/清澈的蓝挽一缕月光照亮夜晚的轩窗微凉的风摇曳丝丝岸柳似少女般漫妙轻舒弄影( 文章阅读网：www.sanwen.net)飘舞在迷人的曉风琬月下凝眸远望是江南的一帘秋水长天*轻移莲步採一朵清新的茉莉花</div>
				</div>
				<div class="row">
					<div class="col-lg-12 col-sm-12">
						<span class="label label-default tag" style="font-size: 13px;">MX-5</span>
						<span class="label label-primary tag" style="font-size: 13px;">马自达</span>
						<span class="label label-success tag" style="font-size: 13px;">敞篷跑车</span>
					</div>
				</div>
				<div class="row" style="font-family: '宋体'; font-size: 12px;margin-top: 5px; color: gray; margin-bottom: 10px;">
					<div class="col-lg-12">
						<button type="button" class="btn btn-primary btn-sm" onclick="goToReadFull(366)">查看详细</button>
					</div>
				</div>
		</div>
	</div>
</div>
</body>
	<script type="text/javascript">
	var isLogin = <%=isLogin %>; //是否已经登录
	var loginUserId = <%=loginUserId %>; //获取登录用户Id
	var isAdmin = <%=isAdmin %>; //是否是管理员
	var searchKey;
	$(function(){
		$(".common-search").remove();
		searchKey = getURLParam(decodeURI(window.location.href), "q");
		if(isEmpty(searchKey)){
			layer.msg("获取不到您要检索的关键字！");
			return;
		}
		
		$("#common-search-text").val(searchKey);
	});
	</script>
</html>