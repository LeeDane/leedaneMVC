
var last_id = 0;
var first_id = 0;
var method = 'firstloading';
var comments;
//浏览器可视区域页面的高度
var winH = $(window).height(); 
var isLoad = false;
var canLoadData = true;
var bid;
$(function(){
	bid = $('[name="blogId"]').val();
	if(isEmpty(bid)){
		layer.msg("文章不存在");
		return;
	}
	getInfo(bid);
	getComments(bid);
	
	$(".container").on("click", ".reply-other-btn", function(){
		$(this).closest(".list-group").find(".reply-container").toggle("fast");
	});
	
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
	    if(height < 0.20 && !isLoad && canLoadData){
	    	isLoad = true;
	    	method = 'lowloading';
	    	getComments(bid);
	    }
	}); 
});

/**
 * 获取博客的基本信息
 * @param bid
 */
function getInfo(bid){
	$.ajax({
		type : "post",
		data : {blog_id: bid, t: Math.random()},
		url : getBasePath() +"leedane/blog/getOneBlog.action",
		dataType: 'json',
		beforeSend:function(){
		},
		success : function(data) {
			if(data.isSuccess && data.message.length > 0){
				var blog = data.message[0];
				document.title = data.message[0].title;
				$(".row-content").html(data.message[0].content);
				$("#b-title").html(changeNotNullString(blog.title));
				$("#b-account").html(changeNotNullString(blog.account));
				$("#b-account").attr("onclick", 'linkToMy('+ blog.create_user_id +')');
				$("#b-create-time").html("发表于:"+changeNotNullString(blog.create_time));
				$("#b-read-time").html("阅读量:"+blog.read_number);
				$("#b-comment-number").html("评论量:"+blog.comment_number);
				$("#b-transmit-number").html("转发量:"+blog.transmit_number);
				$("#b-zan-number").html("点赞量:"+blog.zan_number);
				$("#b-share-number").html("分享量:"+blog.share_number);
				var keywords = blog.keywords;
				if(typeof(keywords) != 'undefined' && keywords.length > 0){
					$("#keywords").find("a").remove();
					for(var i = 0; i < keywords.length; i++){
						$("#keywords").append('<a href="'+ getBasePath() +'page/search.jsp?q='+ keywords[i] +'&t='+ Math.random() +'" target="_self" class="marginRight">'+ keywords[i] +'</a>');
					}
				}
				var tag = blog.tag;
				$('#tags').empty();
				if(isNotEmpty(tag)){
					var tags = tag.split(",");
					for(var i = 0; i < tags.length; i++){
						if(i == 0){
							$('#tags').append('<span class="label label-default tag" style="font-size: 13px;">'+ tags[i] +'</span>');
						}else if(i == 1){
							$('#tags').append('<span class="label label-primary tag" style="font-size: 13px;">'+ tags[i] +'</span>');
						}else{
							$('#tags').append('<span class="label label-success tag" style="font-size: 13px;">'+ tags[i] +'</span>');
						}
					}
				}
			}else{
				layer.msg(data.message);
			}
		},
		error : function() {
			layer.msg("网络请求失败");
		}
	});
}

/**
 * 获取评论请求列表参数
 */
function getCommentsRequestParams(){
	var pageSize = 15;
	if(method != 'firstloading')
		pageSize = 5;
	return {table_name: "t_blog", table_id: bid, showUserInfo: true, pageSize: pageSize, last_id: last_id, first_id: first_id, method: method, t: Math.random()};
}

/**
 * 获取博客的评论内容
 * @param bid
 */
function getComments(bid){
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : getCommentsRequestParams(),
		url : getBasePath() +"leedane/comment/paging.action",
		dataType: 'json',
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			if(data.isSuccess){
				if(method == 'firstloading')
					$(".comment-list").remove();
				
				if(data.message.length == 0 && method != 'firstloading'){
					canLoadData = false;
					layer.msg("已无更多评论数据");
					return;
				}
				
				if(method == 'firstloading'){
					comments = data.message;
					for(var i = 0; i < comments.length; i++){
						buildEachCommentRow(i, comments[i]);
						if(i == 0)
							first_id = comments[i].id;
						if(i == comments.length -1)
							last_id = comments[i].id;
					}
				}else{
					var currentIndex = comments.length;
					for(var i = 0; i < data.message.length; i++){
						comments.push(data.message[i]);
						buildEachCommentRow(currentIndex + i, data.message[i]);
						
						if(i == data.message.length -1)
							last_id = data.message[i].id;
					}
				}
			}else{
				layer.msg("获取评论列表失败，"+data.message);
			}
			isLoad = false;
		},
		error : function() {
			isLoad = false;
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

/**
 * 构建每一行评论html
 * @param comment
 * @param index
 */
function buildEachCommentRow(index, comment){
	var html = '<div class="row comment-list comment-list-padding">'+
			   		'<div class="col-lg-1">'+
						'<img src="'+ changeNotNullString(comment.user_pic_path) +'" width="100%" height="70px" class="img-rounded">'+
					'</div>'+
					'<div class="col-lg-11">'+
				       '<div class="list-group">'+
				       		'<div class="list-group-item comment-list-item active">'+
				       			'<a href="JavaScript:void(0);" onclick="linkToMy('+ comment.create_user_id +')" target="_blank" class="marginRight">'+ changeNotNullString(comment.account)+'</a>'+
				       			'<span class="marginRight publish-time">发表于:'+ changeNotNullString(comment.create_time) +'</span>'+
				       			'<span class="marginRight publish-time">来自:'+ changeNotNullString(comment.froms) +'</span>'+
				       		'</div>';
							if(isLogin){
								html += '<div class="list-group-item comment-list-item">'+
							       			'<div class="row">'+
						       				'<div class="col-lg-12">'+ changeNotNullString(comment.content) +'</div>'+
						       			'</div>'+
						       			'<div class="row">'+
						       				'<div class="col-lg-offset-11 col-lg-1 text-align-right">'+
						       					 '<button class="btn btn-sm btn-primary btn-block reply-other-btn" style="width: 60px;" type="button">回复TA</button>'+
						       				'</div>'+
						       			'</div>'+
						       		'</div>'+
						       		'<div class="col-lg-12 reply-container" table-id="'+ comment.table_id+'" table-name="'+ comment.table_name +'" style="display: none;">'+
							    		'<div class="row">'+
							    			'<div class="col-lg-12">'+
							    				'<form class="form-signin" role="form">'+
							    			     '<fieldset>'+
							    				     '<textarea class="form-control"> </textarea>'+
							    			     '</fieldset>'+
							    			 '</form>'+
							    			'</div>'+
							    			'<div class="col-lg-offset-11 col-lg-1 text-align-right" style="margin-top: 10px;">'+
							    				'<button class="btn btn-sm btn-info btn-block" style="width: 60px;" type="button">评论</button>'+
							    			'</div>'+
							    		'</div>'+
							    	'</div>';
							}
				       		
					html += '</div>'+
				'</div>'+
			'</div>';
	
	$(".container").append(html);
}

/**
 * 添加评论
 * @param obj
 */
function addComment(obj){
	var addCommentObj = $(obj).parent().find('[name="add-comment"]');
	if(isEmpty(addCommentObj.val())){
		addCommentObj.focus();
		layer.msg("评论内容不能为空");
		return;
	}
	var params = {table_name: "t_blog", table_id: bid, content: addCommentObj.val(), froms: "web端", t: Math.random()};
	doAddComment(params);
}

/**
 * 获取博客的评论内容
 * @param bid
 */
function doAddComment(params){
	var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
	$.ajax({
		type : "post",
		data : params,
		url : getBasePath() +"leedane/comment/add.action",
		dataType: 'json',
		beforeSend:function(){
		},
		success : function(data) {
			layer.close(loadi);
			if(data.isSuccess){
				layer.msg("评论成功,1秒钟后自动刷新");
				setTimeout("window.location.reload();", 1000);
			}else{
				layer.msg("添加评论失败，"+data.message);
			}
		},
		error : function() {
			layer.close(loadi);
			layer.msg("网络请求失败");
		}
	});
}

function dd(){
	
}