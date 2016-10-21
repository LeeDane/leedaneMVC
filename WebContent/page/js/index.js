var last_id = 0;
var first_id = 0;
var method = 'firstloading';
var imgs = [];

var blogs;
$(function(){
	//layer.msg($('.main_bg').offset().top)
	//getLogin();
	//getScore();
	getWebBackgroud();
	getMainContentData();
});

function getLogin(){
	var params = {account: 'leedane', password: $.md5("456"), t: Math.random()};
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : params,
		url : getBasePath() +"leedane/user/login.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			layer.msg(data);
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

function getScore(){
	var params = {account: 'leedane', password: $.md5("456"), t: Math.random()};
	$.ajax({
		type : "post",
		data : params,
		url : getBasePath() +"leedane/score/paging.action",
		dataType: 'json', 
		beforeSend:function(){
		},
		success : function(data) {
				layer.msg(data);
		},
		error : function() {
			layer.msg("网络请求失败");
		}
	});
}

//获取背景图
function getWebBackgroud(){
	 $.ajax({
			type : "post",
			data : "t="+Math.random(),
			url : getBasePath() +"leedane/webConfig/background.action",
			beforeSend:function(){
			},
			success : function(data) {
				if(data != null)
					$(".main_bg").css('background', 'url("'+data+'")');
				else
					layer.msg("获取背景图片失败");
			},
			error : function() {
				layer.msg("网络请求失败");
			}
		});
}

//获取内容数据
function getMainContentData(){
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : getMainContentRequestParams(),
		dataType: 'json',  
		url : getBasePath() +"leedane/blog/paging.action",
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			if(data != null && data.isSuccess){
				if(method == 'firstloading')
					$(".container").empty();
				
				blogs = data.message;
				for(var i = 0; i < blogs.length; i++){
					//判断是否有图
					if(data.message[i].has_img)
						$(".container").append(buildHasImgRow(i, blogs[i]));
					else
						$(".container").append(buildNotHasImgRow(i, blogs[i]));
				}
				resetSideHeight();
			}else{
				layer.msg(data.message);
			}
			console.log(data);
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

//构建有图的情况下的html
function buildHasImgRow(index, blog){
	var html ='<div class="row">'+
			      '<div class="col-lg-4">'+
			      	  	'<img width="100%" height="424" class="img-rounded" alt="" src="'+ blog.img_url +'" onClick="showImg('+ index +');">'+
			      '</div>'+
			      '<div class="col-lg-8" style="height:424px;">'+
						'<div class="panel panel-info">'+
							'<div class="panel-heading">'+
								'<div class="page-header">'+
								    '<h1>'+ blog.title +
								        '<small>'+
								        	
										'</small>'+
								    '</h1>'+
								    '<ol class="breadcrumb">'+
								    	'<li>文章</li>'+
										'<li class="active">'+ blog.create_time +'</li>'+
									'</ol>';
									if(isNotEmpty(blog.tag)){
										var t = blog.tag;
										var tags = t.split(',');
										for(var i = 0; i < tags.length; i++){
											if(i == 0)
												html += '<span class="label label-default tag">'+ tags[i]+'</span>';
												
											if(i == 1)
												html += '<span class="label label-primary tag">'+ tags[i]+'</span>';
												
											if(i == 2)
												html += '<span class="label label-success tag">'+ tags[i]+'</span>';
										}
									}
									
						html += '</div>'+
							'</div>'+
							'<div class="panel-body">'+ blog.digest +
							'...</div>'+
							'<div class="panel-footer">';
								if(isLogin){
									html += '<div class="btn-group dropup">'+
												'<button type="button" class="btn btn-primary dropdown-toggle btn-default" '+
														'data-toggle="dropdown">操作 <span class="caret"></span>'+
												'</button>'+
												'<ul class="dropdown-menu" role="menu">'+
													'<li><a href="#">关注</a></li>'+
													'<li><a href="#">Edit</a></li>'+
													'<li><a href="#">Delete</a></li>'+
													'<li class="divider"></li>'+
													'<li><a href="#">Delete</a></li>'+
												'</ul>'+
											'</div>';
								}
								
						html +='<button type="button" class="btn btn-primary btn-default ">'+
									  			'<span class="glyphicon glyphicon-phone"></span> '+ blog.froms +
												'</button>'+
								'<button type="button" class="btn btn-primary btn-default ">'+
									  			'<span class="glyphicon glyphicon-user"></span> '+ blog.account +
												'</button>'+
								'<button type="button" class="btn btn-primary" onclick="goToReadFull('+ blog.id+')">阅读全文</button>'+
							'</div>'+
						'</div>'+
			      '</div>'+
			   '</div>';
		return html;
}

//构建无图的情况下的html
function buildNotHasImgRow(index, blog){
	var html = '<div class="row">'+
			      '<div class="col-lg-12">'+
		      	  	'<div class="panel panel-info">'+
						'<div class="panel-heading">'+
							'<div class="page-header">'+
							    '<h1>'+ blog.title +
							        '<small>'+
							        	
									'</small>'+
							    '</h1>'+
							    '<ol class="breadcrumb">'+
							    	'<li>文章</li>'+
									'<li class="active">'+ blog.create_time +'</li>'+
								'</ol>';
								if(isNotEmpty(blog.tag)){
									var t = blog.tag;
									var tags = t.split(',');
									for(var i = 0; i < tags.length; i++){
										if(i == 0)
											html += '<span class="label label-default tag">'+ tags[i]+'</span>';
											
										if(i == 1)
											html += '<span class="label label-primary tag">'+ tags[i]+'</span>';
											
										if(i == 2)
											html += '<span class="label label-success tag">'+ tags[i]+'</span>';
									}
								}
					html += '</div>'+
						'</div>'+
						'<div class="panel-body">'+ blog.digest +
						'...</div>'+
						'<div class="panel-footer">';
							if(isLogin){
								html += '<div class="btn-group dropup">'+
											'<button type="button" class="btn btn-primary dropdown-toggle btn-default" '+
													'data-toggle="dropdown">操作 <span class="caret"></span>'+
											'</button>'+
											'<ul class="dropdown-menu" role="menu">'+
												'<li><a href="#">关注</a></li>'+
												'<li><a href="#">Edit</a></li>'+
												'<li><a href="#">Delete</a></li>'+
												'<li class="divider"></li>'+
												'<li><a href="#">Delete</a></li>'+
											'</ul>'+
										'</div>';
							}
							
					html +='<button type="button" class="btn btn-primary btn-default ">'+
								  			'<span class="glyphicon glyphicon-phone"></span> '+ blog.froms +
											'</button>'+
							'<button type="button" class="btn btn-primary btn-default ">'+
								  			'<span class="glyphicon glyphicon-user"></span> '+ blog.account +
											'</button>'+
							'<button type="button" class="btn btn-primary" onclick="goToReadFull('+ blog.id+')">阅读全文</button>'+
						'</div>'+
					'</div>'+
		      '</div>'+
		   '</div>';
	return html;
}

//获取请求参数
function getMainContentRequestParams(){
	var pageSize = 5;
	if(method != 'firstloading')
		pageSize = 5;
	return {pageSize: pageSize, last_id: last_id, first_id: first_id, method: method, t: Math.random()};
}

//展示图片的链接
function showImg(index){
	var blog = blogs[index];
	var json = {
			  "title": "相册标题", //相册标题
			  "id": 0, //相册id
			  "start": 0, //初始显示的图片序号，默认0
			  "data": [   //相册包含的图片，数组格式
			    {
			      "alt": blog.title,
			      "pid": 0, //图片id
			      "src": blog.img_url, //原图地址
			      "thumb": blog.img_url //缩略图地址
			    }
			  ]
			};
	layer.photos({
	    photos: json
	    ,shift: 1 //0-6的选择，指定弹出图片动画类型，默认随机
	  });
}

//跳转到全文阅读
function goToReadFull(id){
	layer.msg("文章ID为："+id);
}