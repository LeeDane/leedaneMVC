var userinfo;
var last_id = 0;
var first_id = 0;
var method = 'firstloading';
var moods = [];
var isLoad = false;

//浏览器可视区域页面的高度
var winH = $(window).height(); 

var monthArray = new Array();

$(function(){
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
	    var pageH = $(document.body).height(); //页面总高度 
	    var scrollT = $(window).scrollTop(); //滚动条top 
	    var height = (pageH-winH-scrollT)/winH;
	    if(!isLoad && height < 0.20){
	    	isLoad = true;
	    	method = 'lowloading';
	    	getMoods();
	    }
	}); 
});

/**
 * 获取当前用户的基本信息
 * @param uid
 */
function loadUserInfo(){
	var params = {searchUserIdOrAccount: uid, t: Math.random()};
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : params,
		url : getBasePath() +"leedane/user/searchUserByUserIdOrAccount.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			//layer.msg(JSON.stringify(userinfo));
			if(data.isSuccess){
				
				userinfo = data.userinfo;
				if(isNotEmpty(userinfo.user_pic_path))
					$("#user_img").html('<img src="'+ userinfo.user_pic_path +'" width="120px" height="120px" class="img-circle">');
				
				var descHtml = '<div class="h3">'+ 
									userinfo.account + (userinfo.is_admin ? '<span class="badge" style="margin-left:5px;">管理员</span>': '')+
								'</div>'+
								'<div class="h4" style="max-height: 38px;overflow-y:auto;">'+ userinfo.personal_introduction+'</div>'+
								'<button type="button" class="btn btn-primary btn-xs" data-toggle="modal" data-target="#edit-user-info">'+
									  '<span class="glyphicon glyphicon-pencil" ></span> 编辑个人资料'+
									'</button>';
				if(isLoginUser){
					descHtml += '<button id="sign_button" type="button" class="btn btn-primary btn-xs" style="margin-left:5px;" disabled="disabled">'+
								  '<span class="glyphicon glyphicon-time" ></span> 签到'+
								'</button>';
				}
								
				$("#user_desc").html(descHtml);
				
				buildShowUserinfo();
				buildEditUserinfo();
			}
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

/**
 * 获取心情请求列表
 */
function getMoods(){
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : getMoodRequestParams(),
		url : getBasePath() +"leedane/mood/getPagingMood.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			if(data != null && data.isSuccess){
				if(method == 'firstloading')
					//$("#float-month").empty();
					//$("#mood-container").empty();
				
				if(data.message.length == 0){
					canLoadData = false;
					layer.msg("无更多数据");
					return;
				}
				
				if(method == 'firstloading'){
					moods = data.message;
					for(var i = 0; i < moods.length; i++){
						//判断是否有图
						$("#mood-container").append(buildMoodRow(i, moods[i]));
						if(i == 0)
							first_id = moods[i].id;
						if(i == moods.length -1)
							last_id = moods[i].id;
						
						if(!isInMonthArray(moods[i].create_time.substring(0, 7))){
							monthArray.push(moods[i].create_time.substring(0, 7));
							$("#float-month").append('<li><a href="#section2">'+ moods[i].create_time.substring(0, 7) +'</a></li>');
						}
					}
				}else{
					var currentIndex = moods.length;
					for(var i = 0; i < data.message.length; i++){
						moods.push(data.message[i]);
							$("#mood-container").append(buildMoodRow(currentIndex + i, data.message[i]));
							
						if(i == data.message.length -1)
							last_id = data.message[i].id;
						
						if(!isInMonthArray(data.message[i].create_time.substring(0, 7))){
							monthArray.push(data.message[i].create_time.substring(0, 7));
							$("#float-month").append('<li  class="active"><a href="#section2">'+ data.message[i].create_time.substring(0, 7) +'</a></li>');
						}
					}
				}
				console.log(monthArray);
				resetSideHeight();
			}else{
				layer.msg(data.message);
			}
			console.log(data);
			isLoad = false;
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

function isInMonthArray(str){
	if(monthArray.length == 0)
		return false;
	for(var i = 0; i < monthArray.length; i++){
		if(monthArray[i] == str){
			return true;
		}
	}
	
	return false;
}

/**
 * 构建每一行心情列表的数据
 * @param index
 * @param mood
 * @returns {String}
 */
function buildMoodRow(index, mood){
	/*var html = '<div id="section1" class="row_'+ index +'">'+
					'<h1>'+ mood.content+'</h1>'+
					'<p>Try to scroll this section and look at the navigation list while scrolling!</p>'+
				'</div>';*/
	var html = '<div class="list-group">'+
				    '<div class="list-group-item active">'+
						'<div class="row">'+
							'<div class="col-lg-8">'+
								'<span class="list-group-item-heading">来自：'+ changeNotNullString(mood.froms) +
						        '</span>'+
							'</div>'+
							'<div class="col-lg-4">'+
								'<span class="list-group-item-heading" style="margin-right: 5px;">'+ changeNotNullString(mood.create_time) +
						        '</span>'+
								'<span class="list-group-item-heading glyphicon glyphicon-chevron-down cursor" onclick="showItemListModal('+ index +')">'+
						        '</span> '+
							'</div>'+
						'</div>'+
					'</div>'+
					'<div class="list-group-item">'+
					    '<div class="list-group-item-text">'+ changeNotNullString(mood.content) +
					    '</div>';
				if(isNotEmpty(mood.location)){
					html += '<p class="location">位置：'+ changeNotNullString(mood.location) +'</p>';
				}
					html += '<div class="row">';
				if(isNotEmpty(mood.imgs)){
					var imgs = mood.imgs.split(";");
					for(var i = 0; i < imgs.length; i++){
						html += '<div class="col-lg-4 col-sm-4">'+
					      			'<img src="'+ imgs[i] +'" width="100%" height="180px" class="img-responsive" onClick="showImg('+ index +', '+ i +');">'+
						      	'</div>';
					}
				}
				html += '</div>';
					if(isNotEmpty(mood.zan_users)){
						var users = mood.zan_users.split(";");
						var userStr = "";
						for(var i = 0; i < users.length; i++){
							var user = users[i];
							if(isNotEmpty(user) && user.split(",").length == 2){
								if(i != users.length -1)
									userStr += '<a href="JavaScript:void(0);" onclick="linkToMy('+ user.split(",")[0] +')">'+ changeNotNullString(user.split(",")[1]) +'</a>、';
								else
									userStr += '<a href="JavaScript:void(0);" onclick="linkToMy('+ user.split(",")[0] +')">'+ changeNotNullString(user.split(",")[1]) +'</a>';
							}
						}
					html += '<div class="zan_user">'+ userStr +'等'+ users.length +'人觉得很赞</div>';
					}
					
			html +=	'</div>'+
					'<div class="list-group-item list-group-item-operate">'+
					     '<button type="button" class="btn btn-primary btn-sm" '+ (mood.can_comment? '' : 'disabled="disabled"') +'>评论('+ mood.comment_number+')</button>'+
					     '<button type="button" class="btn btn-primary btn-sm" '+ (mood.can_transmit? '' : 'disabled="disabled"') +'>转发('+ mood.transmit_number+')</button>'+
					     '<button type="button" class="btn btn-primary btn-sm" onclick="goToReadFull('+ mood.id +')">查看详细</button>'+
					'</div>'+
				'</div>';
	return html;
}

/**
 * 获取心情请求列表
 */
function getMoodRequestParams(){
	var pageSize = 15;
	if(method != 'firstloading')
		pageSize = 5;
	return {pageSize: pageSize, last_id: last_id, first_id: first_id, method: method, toUserId: uid, t: Math.random()};
}
/**
 * 页面展示的用户基本信息
 */
function buildShowUserinfo(){
	var infoHtml = '<div class="table-responsive">'+
						'<table class="table table-striped">'+
						  '<tr>'+
						    '<td>性别</td>'+
						    '<td>'+ changeNotNullString(userinfo.sex) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>出生日期</td>'+
						     '<td>'+ changeNotNullString(userinfo.birth_day) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>qq</td>'+
						     '<td>'+ changeNotNullString(userinfo.qq) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>手机号码</td>'+
						     '<td>'+ changeNotNullString(userinfo.mobile_phone) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>邮箱</td>'+
						     '<td>'+ changeNotNullString(userinfo.email) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>学历</td>'+
						     '<td>'+ changeNotNullString(userinfo.education_background) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>注册时间</td>'+
						     '<td>'+ changeNotNullString(userinfo.register_time) +'</td>'+
						  '</tr>'+
						  '<tr>'+
						     '<td>最后请求时间</td>'+
						     '<td>'+ changeNotNullString(userinfo.last_request_time) +'</td>'+
						  '</tr>'+
						'</tbody>'+
						'</table>'+
					'</div>';
		$("#user_info").html(infoHtml);
}
/**
 * 添加编辑用户信息的弹出模块
 */
function buildEditUserinfo(){
	//添加编辑用户信息的弹出模块html
	var editHtml = '<form role="form" class="myForm"><div class="form-group">'+
					  '<label for="sex">性别</label>'+
					  '<select class="form-control" name="sex">'+
						'<option value="男"'+ (userinfo.sex == '男'? ' selected="selected" ': '')+'>男</option>'+
						'<option value="女"'+ (userinfo.sex == '女'? ' selected="selected" ': '')+'>女</option>'+
						'<option value="未知"'+ (userinfo.sex == '未知'? ' selected="selected" ': '')+'>未知</option>'+
						'</select>'+
					'</div>'+
					'<div class="form-group">'+
					  '<label for="birth_day">出生日期</label>'+
					  '<input type="date" class="form-control" name="birth_day" value="'+changeNotNullString(userinfo.birth_day)+'">'+
					'</div>'+
					'<div class="form-group">'+
					  '<label for="qq">QQ</label>'+
					  '<input type="number" class="form-control" name="qq" placeholder="请输入QQ号码" value="'+changeNotNullString(userinfo.qq)+'">'+
					'</div>'+
					'<div class="form-group">'+
					  '<label for="mobile_phone">手机号码</label>'+
					  '<input type="number" class="form-control" name="mobile_phone" placeholder="请输入11位的手机号码" value="'+changeNotNullString(userinfo.mobile_phone)+'">'+
					'</div>'+
					'<div class="form-group">'+
					  '<label for="email">邮箱</label>'+
					  '<input type="email" class="form-control" name="email" placeholder="请输入电子邮箱地址" value="'+changeNotNullString(userinfo.email)+'">'+
					'</div>'+
					'<div class="form-group">'+
					  '<label for="education_background">学历</label>'+
					  '<select class="form-control" name="education_background">'+
					  	'<option value="博士后"'+ (userinfo.education_background == '博士后'? ' selected="selected" ': '')+'>博士后</option>'+
					  	'<option value="博士"'+ (userinfo.education_background == '博士'? ' selected="selected" ': '')+'>博士</option>'+
						'<option value="硕士"'+ (userinfo.education_background == '硕士'? ' selected="selected" ': '')+'>硕士</option>'+
						'<option value="本科"'+ (userinfo.education_background == '本科'? ' selected="selected" ': '')+'>本科</option>'+
						'<option value="大专"'+ (userinfo.education_background == '大专'? ' selected="selected" ': '')+'>大专</option>'+
						'<option value="中专"'+ (userinfo.education_background == '中专'? ' selected="selected" ': '')+'>中专</option>'+
						'<option value="高中"'+ (userinfo.education_background == '高中'? ' selected="selected" ': '')+'>高中</option>'+
						'<option value="初中"'+ (userinfo.education_background == '初中'? ' selected="selected" ': '')+'>初中</option>'+
						'<option value="小学"'+ (userinfo.education_background == '小学'? ' selected="selected" ': '')+'>小学</option>'+
						'<option value="文盲"'+ (userinfo.education_background == '文盲'? ' selected="selected" ': '')+'>文盲</option>'+
						'</select>'+
					'</div>'+
					'<div class="form-group">'+
					  '<label for="personal_introduction">个人简介</label>'+
					  '<textarea class="form-control" name="personal_introduction" placeholder="请输入个人简介">'+changeNotNullString(userinfo.personal_introduction)+'</textarea>'+
					'</div>'+
					'</form';
		$(".modal-body-edit-userinfo").html(editHtml);
}

/**
 * 执行编辑用户的基本信息
 */
function editUserinfo(params){
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : params,
		url : getBasePath() +"leedane/user/updateUserBase.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			
			if(data.isSuccess){
				layer.msg("基本信息更新成功！");
				window.location.reload();
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
 * 展示图片的链接
 * @param index  当前心情的索引
 * @param imgIndex 当前心情图片的索引
 */
function showImg(index, imgIndex){
	var mood = moods[index];
	var json = {
			  "title": "相册标题", //相册标题
			  "id": 0, //相册id
			  "start": imgIndex //初始显示的图片序号，默认0
			};
	var datas = new Array();
	var photos = mood.imgs.split(";");
	for(var i = 0; i < photos.length; i++){
		var each = {};
		var path = photos[i];
		each.src = path;//原图地址
		each.alt = path;//缩略图地址
		datas.push(each);
	}
	
	json.data = datas;
	
	layer.photos({
	    photos: json
	    ,shift: 1 //0-6的选择，指定弹出图片动画类型，默认随机
	  });
}

/**
 * 跳转到我的个人中心
 * @param id
 */
function linkToMy(id){
	if(isEmpty(id)){
		layer.msg("该用户不存在，请联系管理员核实");
		return;
	}
	window.open(getBasePath() +"page/my.jsp?uid="+id, "_self");
}

/**
 * 跳转到心情详细阅读
 * @param id
 */
function goToReadFull(id){
	if(isEmpty(id)){
		layer.msg("该心情不存在，请联系管理员核实");
		return;
	}
	window.open(getBasePath() +"page/detail.jsp?bid="+id, "_blank");
}

/**
 * 展示选项列表modal
 * @param index
 */
function showItemListModal(index){
	$("#operate-item-list").modal("show");
	var mood = moods[index];
	var html = '<li class="list-group-item cursor" onclick="goToReadFull('+ mood.id +');">查看</li>'+
			    '<li class="list-group-item cursor" onclick="addZan('+ mood.id +')">赞</li>'+
			    '<li class="list-group-item cursor">翻译</li>'+
			    '<li class="list-group-item cursor" onclick="copyToClipBoard(\''+ mood.content +'\');">复制文字</li>';
	if(isLoginUser){
		html += '<li class="list-group-item cursor" onclick="deleteMood('+ mood.id +')">删除</li>'+
			    '<li class="list-group-item cursor" onclick="updateCommentStatus('+ mood.can_comment +','+ mood.id +');">'+
			        '<span class="badge">'+ (mood.can_comment? '已启用': '已禁用') +'</span>'+
			        	'设置是否能评论'+
			    '</li>'+
			    '<li class="list-group-item cursor" onclick="updateTransmitStatus('+ mood.can_transmit+','+ mood.id+')">'+
			        '<span class="badge">'+ (mood.can_transmit? '已启用': '已禁用') +'</span>'+
			        	'设置是否能转发'+
			    '</li>';
	}
			    
	
	$("#operate-list").html(html);
}

/**
 * 添加赞
 * @param id
 */
function addZan(id){
	var loadi = layer.load('努力加载中…');
	$.ajax({
		type : "post",
		data: {table_name: 't_mood', content: '', forms: '网页端', table_id: id},
		url : getBasePath() +"leedane/zan/add.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
				
				layer.close(loadi);
				if(data.isSuccess){
					layer.msg("点赞成功");
					window.location.reload();
				}else
					layer.msg(data.message);
				
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

/**
 * 删除心情
 * @param id
 */
function deleteMood(id){
	var loadi = layer.load('努力加载中…');
	$.ajax({
		type : "post",
		data: {mid: id},
		url : getBasePath() +"leedane/mood/delete.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
				layer.close(loadi);
				layer.msg(data.message);
				if(data.isSuccess){
					window.location.reload();
				}
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}
/**
 * 更新评论状态
 * @param can
 * @param table_id
 */
function updateCommentStatus(can, table_id){
	var loadi = layer.load('努力加载中…');
	$.ajax({
		type : "post",
		data: {table_name: 't_mood', can_comment: !can, table_id: table_id},
		url : getBasePath() +"leedane/comment/updateCommentStatus.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
				layer.close(loadi);
				layer.msg(data.message);
				if(data.isSuccess){
					window.location.reload();
				}
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

/**
 * 更新转发状态
 * @param can
 * @param table_id
 */
function updateTransmitStatus(can, table_id){
	var loadi = layer.load('努力加载中…');
	$.ajax({
		type : "post",
		data: {table_name: 't_mood', can_transmit: !can, table_id: table_id},
		url : getBasePath() +"leedane/transmit/updateTransmitStatus.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
				layer.close(loadi);
				layer.msg(data.message);
				if(data.isSuccess){
					window.location.reload();
				}
				
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

/**
 * 复制内容到粘贴板
 * @param text
 */
function copyToClipBoard(text){
	window.clipboardData.setData("Text",text); 
	layer.msg(text +"已经成功复制到粘贴板");
} 

function testClick(obj){
	//$(this).animate({scrollTop: "10"});
	var t = $(obj);
	$("body").animate({scrollTop: t.offset().top - 50}, 800);
	//t.animate({"scrollTop": 10}, 800);​
}