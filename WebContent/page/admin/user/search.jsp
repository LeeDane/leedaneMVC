<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
 	
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <style type="text/css">
   		.list-row {
		    margin-top: 16px;
		    border: 1px solid #ddd;
		    background-color: #fff !important;
		    border-radius: 4px;
		    padding-bottom: 8px;
		    padding-top: 8px;
		}
		.senior-condition{
			margin-top: 8px;
			display: none;
		}
		.padding-five{
			padding: 5px;
		}
		.modal-body-send-message .active{
			background-color: #5cb85c !important;
    		border-color: #5cb85c !important;
		}
   </style>
 </head>
<body>
	<%@ include file="../adminCommon.jsp" %>
  	<div class="container mainContainer clearFloat">
	   <div class="row list-row">
	   		<div class="col-lg-12">
		   		<div class="row">
		   			<div class="col-lg-4">
		   				 <input type="text" name="searchKey" class="form-control" placeholder="请输入账号或者中文名/真实名" onkeypress="if (event.keyCode == 13) search(this);">
		   			</div>
		   			<div class="col-lg-4">
		   				<button type="button" class="btn btn-primary" onclick="search(this);">搜索</button>
		   				<button type="button" class="btn btn-primary senior-condition-btn">高级条件</button>
		   			</div>
		   		</div>
		   		
		   		<div class="row senior-condition">
		   			<div class="col-lg-6">
		   				<div class="row">
		   					<label for="" class="col-sm-6 control-label">注册开始时间</label>
		   				</div>
		   				<div class="row">
			   				 <div class="form-group">
							    <div class="col-sm-12">
							      <input type="date" name="registerStartTime" class="form-control" placeholder="开始时间">
							   </div>
							 </div>
						 </div>
		   			</div>
		   			<div class="col-lg-6">
		   				<div class="row">
		   					<label for="" class="col-sm-6 control-label">注册结束时间</label>
		   				</div>
		   				<div class="row">
			   				 <div class="form-group">
							    <div class="col-sm-12">
							      <input type="date" name="registerEndTime" class="form-control" placeholder="结束时间">
							   </div>
							 </div>
						 </div>
		   			</div>
		   		</div>
		   		<div class="row senior-condition">
		   			<div class="col-lg-6">
		   				<div class="row">
		   					<label for="" class="col-sm-6 control-label">出生开始时间</label>
		   				</div>
		   				<div class="row">
			   				 <div class="form-group">
							    <div class="col-sm-12">
							      <input type="date" name="birthStartTime" class="form-control" placeholder="开始时间">
							   </div>
							 </div>
						 </div>
		   			</div>
		   			<div class="col-lg-6">
		   				<div class="row">
		   					<label for="" class="col-sm-6 control-label">出生结束时间</label>
		   				</div>
		   				<div class="row">
			   				 <div class="form-group">
							    <div class="col-sm-12">
							      <input type="date" name="birthEndTime" class="form-control" placeholder="结束时间">
							   </div>
							 </div>
						 </div>
		   			</div>
		   		</div>
		   </div>
	   </div>
	    <!-- <div class="row list-row">
	    	<div class="col-lg-12">
	    		 <div class="row">
    		 		<div class="col-lg-3">
    		 			<img class="img-circle" alt="" width="90%" height="130" src="http://7xnv8i.com1.z0.glb.clouddn.com/leedane_app_upload_ec5da83b-4fd9-4924-ad04-3264dfc52a9843d8c1ec-95d3-4cc7-aa12-9379a2196df2_Screenshot_20161216-122520_01.png">
    		 		</div>
    		 		<div class="col-lg-9">
    		 			<div class="row">
	    		 			<div class="col-lg-3">姓名：leedane</div>
	    		 			<div class="col-lg-3">真实姓名：leedane</div>
	    		 			<div class="col-lg-3">中文名：leedane</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-3">出生年月：1990-11-11</div>
	    		 			<div class="col-lg-3">性别：男</div>
	    		 			<div class="col-lg-3">籍贯：中国</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-3">QQ：11111111</div>
	    		 			<div class="col-lg-3">手机号：137*****123</div>
	    		 			<div class="col-lg-3">注册时间：1990-11-11</div>
    		 			</div>
    		 			<div class="row">
	    		 			<div class="col-lg-3">邮件：11111111@qq.com</div>
	    		 			<div class="col-lg-3">教育背景：文盲</div>
	    		 			<div class="col-lg-3">出生地：广东湛江</div>
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
	    </div> -->
	</div>
	
<!-- 模态框编辑 -->
<div class="modal fade" id="edit-user-info" data-id=0 tabindex="-1" role="dialog" aria-labelledby="editUserInfoModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="editUserInfoModalLabel">
					编辑用户信息
				</h4>
			</div>
			<div class="modal-body modal-body-edit-user-info">
				<form role="form" class="myForm">
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group">
							  <label for="account">账号</label>
							  <input type="text" class="form-control" name="account" placeholder="请输入用户名">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="form-group">
							  <label for="personal_introduction">个人简介</label>
							  <textarea class="form-control" name="personal_introduction" placeholder="请输入个人简介"></textarea>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="btn-group" data-toggle="buttons">
								<label class="btn btn-primary status-btn">
									<input type="radio" name="options" id="agree" type-value="1"> 正常
								</label>
								<label class="btn btn-primary status-btn">
									<input type="radio" name="options" id="no-agree" type-value="0"> 禁用
								</label>
								<label class="btn btn-primary status-btn">
									<input type="radio" name="options" id="no-agree" type-value="4"> 禁言
								</label>
								<label class="btn btn-primary status-btn">
									<input type="radio" name="options" id="no-agree" type-value="5"> 注销
								</label>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary" onclick="editUser(this);">
					编辑
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 模态框发送信息 -->
<div class="modal fade" id="send-message" data-id=0 tabindex="-1" role="dialog" aria-labelledby="sendMessageModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="sendMessageModalLabel">
					发送信息
				</h4>
			</div>
			<div class="modal-body modal-body-send-message">
				<div class="row">
					<div class="col-lg-12">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-primary send-message-btn">
								<input type="radio" name="options" id="notification" type-value="1"> 通知
							</label>
							<label class="btn btn-primary send-message-btn">
								<input type="radio" name="options" id="email" type-value="2"> 邮件
							</label>
							<label class="btn btn-primary send-message-btn">
								<input type="radio" name="options" id="self" type-value="3"> 私信
							</label>
							<label class="btn btn-primary send-message-btn disabled">
								<input type="radio" name="options" id="message" type-value="4"> 短信
							</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="form-group">
						  <label for="report-reason">发送信息内容<font color="red">*</font></label>
						  <textarea class="form-control" name="content" placeholder="请输入详细的信息内容"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary" onclick="sendMessage(this);">
					发送
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

<!-- 模态框上传用户头像 -->
<div class="modal fade" id="upload-user-head" tabindex="-1" role="dialog" aria-labelledby="uploadUserHeadModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="uploadUserHeadModalLabel">
					上传用户头像
				</h4>
			</div>
			<div class="modal-footer">
				<div class="input-group">
			      <input type="text" name="link" class="form-control" placeholder="请输入头像链接(最好七牛云存储链接)，不能超过500k">
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button" onclick="uploadHeadLink(this);">发送</button>
			      </span>
			    </div><!-- /input-group -->
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<body>
<script type="text/javascript">

	var users;
	$(function(){
		
		//默认查询操作
		querySearch({});
		
		$(".senior-condition-btn").click(function(){
			$(".senior-condition").toggle("fast");
		});
		
	});
	
	/**
	* 搜索
	*/
	function search(obj){
		var params = {};
		params.t = Math.random();

		var registerStartTime = $('[name="registerStartTime"]').val();
		if(isNotEmpty(registerStartTime)){
			params.register_time_start = registerStartTime;
		}
			
		var registerEndTime = $('[name="registerEndTime"]').val();
		if(isNotEmpty(registerEndTime)){
			params.register_time_end = endTime;
		}
		
		var birthStartTime = $('[name="birthStartTime"]').val();
		if(isNotEmpty(birthStartTime)){
			params.birth_time_start = birthStartTime;
		}
			
		var birthEndTime = $('[name="birthEndTime"]').val();
		if(isNotEmpty(birthEndTime)){
			params.birth_time_end = birthEndTime;
		}
		
		var searchKey = $('[name="searchKey"]').val();
		if(isNotEmpty(searchKey)){
			params.search_key = searchKey;
		}
		querySearch(params);
	}
	
	/**
	 * 搜索获取用户的基本信息
	 * @param uid
	 */
	function querySearch(params){
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : params,
			url : getBasePath() +"leedane/user/websearch.action",
			dataType: 'json', 
			beforeSend:function(){
				//显示列表
				isChart = true;
			},
			success : function(data) {
				layer.close(loadi);
				if(data.isSuccess){
					//清空原来的数据
					$(".each-row").remove();
					
					users = data.message;
					if(users.length == 0){
						layer.msg("无更多数据");
						return;
					}
					
					for(var i = 0; i < data.message.length; i++)
						buildEachRow(data.message[i], i);
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
	
	function buildEachRow(user, index){
		var html = '';
		if( typeof(user.user_pic_path) != 'undefined' && isNotEmpty(user.user_pic_path)){
			html += '<div class="row list-row each-row" index='+ index +' data-id='+user.id+'>'+
				    	'<div class="col-lg-12">'+
							 '<div class="row">'+
						 		'<div class="col-lg-2">'+
						 			'<img class="img-circle" alt="" width="90%" height="130" src="'+ user.user_pic_path +'">'+
						 		'</div>'+
						 		'<div class="col-lg-10">'+
						 			'<div class="row">'+
							 			'<div class="col-lg-3">账号：'+ changeNotNullString(user.account) +'</div>'+
							 			'<div class="col-lg-3">真实姓名：'+ changeNotNullString(user.real_name) +'</div>'+
							 			'<div class="col-lg-3">中文名：'+ changeNotNullString(user.china_name) +'</div>'+
							 			'<div class="col-lg-3" padding-five>'+ 
							 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="showEditUser(this, '+ index +');">编辑</button></div>'+
							 			'</div>'+
						 			'</div>'+
						 			'<div class="row">'+
							 			'<div class="col-lg-3">出生日期：'+ changeNotNullString(user.birth_date) +'</div>'+
							 			'<div class="col-lg-3">性别：'+ changeNotNullString(user.sex) +'</div>'+
							 			'<div class="col-lg-3">籍贯：'+ changeNotNullString(user.nation) +'</div>'+
							 			'<div class="col-lg-3 padding-five">'+ 
							 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="deleteUser(this, '+ index +');">删除</button></div>'+
							 			'</div>'+
						 			'</div>'+
						 			'<div class="row">'+
							 			'<div class="col-lg-3">QQ号码：'+ changeNotNullString(user.qq) +'</div>'+
							 			'<div class="col-lg-3">手机号码：'+ changeNotNullString(user.mobile_phone) +'</div>'+
							 			'<div class="col-lg-3">注册时间：'+ changeNotNullString(user.register_time) +'</div>'+
							 			'<div class="col-lg-3 padding-five">'+ 
							 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="showSendMessage(this, '+ index +');">发信息</button></div>'+
							 			'</div>'+
						 			'</div>'+
						 			'<div class="row">'+
							 			'<div class="col-lg-3">邮件：'+ changeNotNullString(user.email) +'</div>'+
							 			'<div class="col-lg-3">教育背景：'+ changeNotNullString(user.education_background) +'</div>'+
							 			'<div class="col-lg-3">出生地：'+ changeNotNullString(user.native_place) +'</div>'+
							 			'<div class="col-lg-3 padding-five">'+ 
							 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="resetPassword(this, '+ index +');">重置密码</button></div>'+
							 			'</div>'+
						 			'</div>'+
						 			'<div class="row">'+
						 				'<div class="col-lg-9">'+
							 				'<div class="form-group">个人简介：'+ changeNotNullString(user.introduction) +'</div>'+
										'</div>'+
										'<div class="col-lg-3 padding-five">'+ 
							 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="showUploadHeadLink(this, '+ index +');">上传头像</button></div>'+
							 			'</div>'+
						 			'</div>'+
						 		'</div>'+
							 '</div>'+
						'</div>'+
					'</div>';
					
		}else{
			html += '<div class="row list-row each-row" data-id='+user.id+'>'+
	    	'<div class="col-lg-12">'+
				 '<div class="row">'+
			 		'<div class="col-lg-12">'+
			 			'<div class="row">'+
				 			'<div class="col-lg-3">账号：'+ changeNotNullString(user.account) +'</div>'+
				 			'<div class="col-lg-3">真实姓名：'+ changeNotNullString(user.real_name) +'</div>'+
				 			'<div class="col-lg-3">中文名：'+ changeNotNullString(user.china_name) +'</div>'+
				 			'<div class="col-lg-3 padding-five">'+ 
				 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="showEditUser(this, '+ index +');">编辑</button></div>'+
				 			'</div>'+
			 			'</div>'+
			 			'<div class="row">'+
				 			'<div class="col-lg-3">出生日期：'+ changeNotNullString(user.birth_date) +'</div>'+
				 			'<div class="col-lg-3">性别：'+ changeNotNullString(user.sex) +'</div>'+
				 			'<div class="col-lg-3">籍贯：'+ changeNotNullString(user.nation) +'</div>'+
				 			'<div class="col-lg-3 padding-five">'+ 
				 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="deleteUser(this, '+ index +');">删除</button></div>'+
				 			'</div>'+
			 			'</div>'+
			 			'<div class="row">'+
				 			'<div class="col-lg-3">QQ号码：'+ changeNotNullString(user.qq) +'</div>'+
				 			'<div class="col-lg-3">手机号码：'+ changeNotNullString(user.mobile_phone) +'</div>'+
				 			'<div class="col-lg-3">注册时间：'+ changeNotNullString(user.register_time) +'</div>'+
				 			'<div class="col-lg-3 padding-five">'+ 
				 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="showSendMessage(this, '+ index +');">发信息</button></div>'+
				 			'</div>'+
			 			'</div>'+
			 			'<div class="row">'+
				 			'<div class="col-lg-3">邮件：'+ changeNotNullString(user.email) +'</div>'+
				 			'<div class="col-lg-3">教育背景：'+ changeNotNullString(user.education_background) +'</div>'+
				 			'<div class="col-lg-3">出生地：'+ changeNotNullString(user.native_place) +'</div>'+
				 			'<div class="col-lg-3 padding-five">'+ 
				 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="resetPassword(this, '+ index +');">重置密码</button></div>'+
				 			'</div>'+
			 			'</div>'+
			 			'<div class="row">'+
			 				'<div class="col-lg-9">'+
				 				'<div class="form-group">个人简介：'+ changeNotNullString(user.introduction) +'</div>'+
							'</div>'+
							'<div class="col-lg-3 padding-five">'+ 
				 				'<div class="row" style="text-align: right;"><button type="button" class="btn btn-primary btn-sm" onclick="showUploadHeadLink(this, '+ index +');">上传头像</button></div>'+
				 			'</div>'+
			 			'</div>'+
			 		'</div>'+
				 '</div>'+
			'</div>'+
		'</div>';
		}
		
		$(".mainContainer").append(html);
		
	}
	
	/**
	* 展示显示编辑用户的模态框
	*/
	function showEditUser(obj, index){
		var user = users[index];
		$("#edit-user-info").attr("data-id", user.id);
		$("#edit-user-info").find('[name="account"]').val(changeNotNullString(user.account));
		$("#edit-user-info").find('[name="personal_introduction"]').val(changeNotNullString(user.introduction));
		var status = user.status;
		var labels = $(".status-btn");
		for(var i = 0 ; i < labels.length; i++){
			if(parseInt($(labels[i]).find("input").attr("type-value")) == status){
				$(labels[i]).addClass("active");
				break;
			}
		}
		$("#edit-user-info").modal("show");
	}
	/**
	* 执行编辑操作
	*/
	function editUser(obj){
		
		var userInfoObj = $(obj).closest("#edit-user-info");
		var account = userInfoObj.find('[name="account"]').val();
		
		if(isEmpty(account)){
			layer.msg("账号名称不能为空");
			userInfoObj.find('[name="account"]').focus();
			return;
		}
		
		
		var status = 0;
		var statusBtnObjs = $(".status-btn");
		for(var i = 0 ; i < statusBtnObjs.length; i++){
			if($(statusBtnObjs[i]).hasClass("active")){
				status = $(statusBtnObjs[i]).find("input").attr("type-value");
				break;
			}
		}
		
		var personal_introduction = userInfoObj.find('[name="personal_introduction"]').val();
		var id = $("#edit-user-info").attr("data-id");
		var params = {to_user_id: id, account: account, personal_introduction: personal_introduction, status: status, t: Math.random()};
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : params,
			url : getBasePath() +"leedane/user/admin/updateUser.action",
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
	/**
	* 执行删除操作
	*/
	function deleteUser(obj, index){
		if(typeof(users) != 'undefined' && users.length > 0 && users.length > index){
			
			layer.confirm('您要删除该用户吗？注意：这是不可逆行为。', {
				  btn: ['确定','点错了'] //按钮
			}, function(){
				var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
				var id = $(obj).closest(".each-row").attr("data-id");
				$.ajax({
					type : "post",
					data : {to_user_id: id, t: Math.random()},
					url : getBasePath() +"leedane/user/deleteUser.action",
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
						layer.msg("网络请求失败");
						layer.close(loadi);
					}
				});
			}, function(){
			});
		}else{
			layer.msg("页面参数有误，请刷新！");
		}
	}
	
	/**
	* 执行重置密码操作
	*/
	function resetPassword(obj, index){
		if(typeof(users) != 'undefined' && users.length > 0 && users.length > index){
			
			layer.confirm('您要重置该用户的登录密码吗？注意：这是不可逆行为。', {
				  btn: ['确定','点错了'] //按钮
			}, function(){
				var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
				var id = $(obj).closest(".each-row").attr("data-id");
				$.ajax({
					type : "post",
					data : {to_user_id: id, t: Math.random()},
					url : getBasePath() +"leedane/user/admin/resetPassword.action",
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
						layer.msg("网络请求失败");
						layer.close(loadi);
					}
				});
			}, function(){
			});
		}else{
			layer.msg("页面参数有误，请刷新！");
		}
	}
	/**
	* 显示发送信息的模态框
	*/
	function showSendMessage(obj, index){
		var user = users[index];
		//用户没有邮件，不能发送邮件。
		if(isEmpty(user.email)){
			$("#send-message").find("#email").closest("label").addClass("disabled");
		}else{
			$("#send-message").find("#email").closest("label").removeClass("disabled");
		}
		$("#send-message").find(".send-message-btn").removeClass("active");
		$("#send-message").find(".send-message-btn:first-child").addClass("active");
		$("#send-message").attr("data-id", user.id);
		$("#send-message").modal("show");
	}
	
	/**
	* 执行发送信息操作
	*/
	function sendMessage(obj){
		var userInfoObj = $(obj).closest("#send-message");
		var content = userInfoObj.find('[name="content"]').val();
		
		if(isEmpty(content)){
			layer.msg("发送信息内容不能为空");
			userInfoObj.find('[name="content"]').focus();
			return;
		}
		var type = $("#send-message").find(".btn-group label.active").children("input").attr("type-value");
		var id = $("#send-message").attr("data-id");
		var params = {to_user_id: id, type: type, content: content, t: Math.random()};
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : params,
			url : getBasePath() +"leedane/tool/sendMessage.action",
			dataType: 'json', 
			beforeSend:function(){
			},
			success : function(data) {
				layer.close(loadi);
				layer.msg(data.message);
				if(data.isSuccess){
					$("#send-message").modal("hide");
				}
			},
			error : function() {
				layer.close(loadi);
				layer.msg("网络请求失败");
			}
		});
	}
	
	/**
	*显示上传用户头像模态框
	*/
	function showUploadHeadLink(obj, index){
		var user = users[index];
		$("#upload-user-head").attr("data-id", user.id);
		$("#upload-user-head").modal("show");
	}
	
	/**
	*执行上传头像
	*/
	function uploadHeadLink(obj){
		var uploadUserHead = $(obj).closest("#upload-user-head");
		var link = uploadUserHead.find('input[name="link"]').val();
		
		if(isEmpty(link)){
			layer.msg("请先输入头像图片链接！");
			uploadUserHead.find('input[name="link"]').focus();
			return;
		}
		var id = $("#upload-user-head").attr("data-id");
		var params = {to_user_id: id, link: link, t: Math.random()};
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : params,
			url : getBasePath() +"leedane/filepath/uploadUserHeadImageLink.action",
			dataType: 'json', 
			beforeSend:function(){
			},
			success : function(data) {
				layer.close(loadi);
				layer.msg(data.message);
				if(data.isSuccess){
					layer.msg(data.message +",1秒钟后自动刷新");
					setTimeout("window.location.reload();", 1000);
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