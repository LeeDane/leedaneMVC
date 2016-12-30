<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
 	
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <style type="text/css">
   		.main-row {
		    margin-top: 16px;
		    border: 1px solid #ddd;
		    background-color: #fff !important;
		    border-radius: 4px;
		    padding-bottom: 8px;
		    padding-top: 8px;
		}
		
		.padding-five{
			padding: 5px;
		}
		
   </style>
 </head>
<body>
	<%@ include file="../adminCommon.jsp" %>
  	<div class="container mainContainer clearFloat">
	    <div class="row main-row">
	    	<div class="col-lg-12">
	    		 <div class="row">
    		 		<div class="col-lg-12">
    		 			<div class="row">
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="account">账号<font color="red"> *</font></label>
	    		 					<input type="text" class="form-control" name="account" placeholder="请输入账号名称">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="account">真实姓名</label>
	    		 					<input type="text" class="form-control" name="real_name" placeholder="请输入真实姓名">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="account">中文名</label>
	    		 					<input type="text" class="form-control" name="china_name" placeholder="请输入中文名">
	    		 				</div>
	    		 			</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="password">登录密码<font color="red"> *</font></label>
	    		 					<input type="password" class="form-control" name="password" placeholder="请输入登录密码">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="confirm_password">登录密码<font color="red"> *</font></label>
	    		 					<input type="password" class="form-control" name="confirm_password" placeholder="请再次输入登录密码">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="address">地址</label>
	    		 					<input type="text" class="form-control" name="address" placeholder="请输入地址">
	    		 				</div>
	    		 			</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="account">出生日期</label>
	    		 					<input type="date" class="form-control" name="birth_day" placeholder="出生日期">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="sex">性别</label>
	    		 					<select class="form-control" name="sex">
	    		 						<option value="男">男</option>
	    		 						<option value="女">女</option>
	    		 						<option value="未知" selected="selected">未知</option>
	    		 					</select>
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="nation">籍贯</label>
	    		 					<select class="form-control" name="nation">
	    		 						<option value="中国" selected="selected">中国</option>
	    		 						<option value="外国">外国</option>
	    		 					</select>
	    		 				</div>
	    		 			</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="qq">QQ</label>
	    		 					<input type="number" class="form-control" name="qq" placeholder="请输入QQ号码">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="mobile_phone">手机号码<font color="red"> *</font></label>
	    		 					<input type="number" class="form-control" name="mobile_phone" placeholder="请输入11位的号码">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="register_time">注册时间</label>
	    		 					<input type="datetime-local" class="form-control" name="register_time" placeholder="注册时间">
	    		 				</div>
	    		 			</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="email">邮件</label>
	    		 					<input type="email" class="form-control" name="email" placeholder="请输入邮件地址">
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="education_background">教育背景</label>
	    		 					<select class="form-control" name="education_background">
	    		 						<option value="博士后">博士后</option>
	    		 						<option value="博士">博士</option>
	    		 						<option value="硕士">硕士</option>
	    		 						<option value="本科">本科</option>
	    		 						<option value="大专">大专</option>
	    		 						<option value="中专">中专</option>
	    		 						<option value="高中">高中</option>
	    		 						<option value="初中">初中</option>
	    		 						<option value="小学">小学</option>
	    		 						<option value="文盲" selected="selected">文盲</option>
	    		 					</select>
	    		 				</div>
	    		 			</div>
	    		 			<div class="col-lg-4">
	    		 				<div class="form-group">
	    		 					<label for="native_place">出生地</label>
	    		 					<input type="text" class="form-control" name="native_place" placeholder="请输入出生地">
	    		 				</div>
	    		 			</div>
    		 			</div>
    		 			<div class="row">
    		 				<div class="col-lg-12">
	    		 				<div class="form-group">
								  <textarea class="form-control" name="personal_introduction" placeholder="请输入个人简介"></textarea>
								</div>
							</div>
    		 			</div>
    		 		</div>
	    		 </div>
	    	</div>
	    </div>
	    <div class="row">
	    	<div class="col-lg-12 col-sm-12" style="text-align: center;margin-top: 10px;">
	    		<button type="button" class="btn btn-primary" style="min-width: 200px;" onclick="add(this);">提交</button>
	    	</div>
	    </div>
	</div>
	

<body>
<script type="text/javascript">

	$(function(){
		$('[name="account"]').focus();
		$('[name="register_time"]').val(formatDateTime());
	});
	
	/**
	*	执行添加操作
	*/
	function add(obj){
		var account = $('[name="account"]').val();
		if(isEmpty(account)){
			$('[name="account"]').focus();
			layer.msg("请先输入账号！");
			return;
		}
		
		var password = $('[name="password"]').val();
		if(isEmpty(password)){
			$('[name="password"]').focus();
			layer.msg("请先输入登录密码！");
			return;
		}
		
		var confirmPassword = $('[name="confirm_password"]').val();
		if(isEmpty(confirmPassword)){
			$('[name="confirm_password"]').focus();
			layer.msg("请再次输入登录密码！");
			return;
		}
		
		if(password != confirmPassword){
			$('[name="confirm_password"]').focus();
			layer.msg("两次输入的登录密码不一致！");
			return;
		}
		
		var mobilePhone = $('[name="mobile_phone"]').val();
		if(isEmpty(mobilePhone) || mobilePhone.length != 11){
			$('[name="mobile_phone"]').focus();
			layer.msg("请输入11位的手机号码！");
			return;
		}
		
		
		var formControl = $(".form-control");
		var params = {};
		formControl.each(function(index){
			var name = $(this).attr("name");
			params[name] = $(this).val();
		});
		console.log(params);
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : params,
			url : getBasePath() +"leedane/user/addUser.action",
			dataType: 'json', 
			beforeSend:function(){
			},
			success : function(data) {
				layer.close(loadi);
				if(data.isSuccess){
					layer.msg(data.message +",1秒钟后自动刷新");
					setTimeout("window.location.reload();", 1000);
				}else{
					layer.msg(data.message);
				}
			},
			error : function() {
				layer.close(loadi);
				layer.msg("网络请求失败");
			}
		});
	}
	
	</script>
</html>  