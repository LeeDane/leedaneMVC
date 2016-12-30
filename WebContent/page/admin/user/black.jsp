<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
 <head>
 	
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <style type="text/css">
		.padding-five{
			padding: 5px;
		}
		.cut-text{
			display: block;
			white-space: nowrap; 
			overflow: hidden; 
			text-overflow: ellipsis;
		}
		.text-align-center{
			text-align: center;
		}
		
   </style>
 </head>
<body>
	<%@ include file="../adminCommon.jsp" %>
  	<div class="container clearFloat">
  		<div class="row" style="margin-top: 10px;">
	   		<div class="col-lg-12">
			    <table class="table table-bordered main-table">
					<caption>以下是黑名单用户列表</caption>
					<thead>
						<tr height="50">
							<th width="10" ><!-- <input type="checkbox" value="选择"> --></th>
							<th width="200" class="text-align-center">标题</th>
							<th class="text-align-center">简要</th>
							<th width="100" class="text-align-center">发布人</th>
							<th width="150" class="text-align-center">发布时间</th>
							<th width="80" class="text-align-center">操作</th>
						</tr>
					</thead>
					<tbody>
						<!-- <tr class="each-row" height="50">
							<td><input type="checkbox" value="选择"></td>
							<td>Rabbitmq和Redis的队列的比较</td>
							<td><a href="http://127.0.0.1:8080/leedaneMVC/page/detail.jsp?bid=6551" target="_blank">一生只谈三次恋爱最好，一次懵懂，一次刻骨，一次一生。谈的太多会比较，无法确定；经历太多会麻木，不再相信爱情，行尸走肉，最后与不爱的人结婚，无法发自内心的爱对方，日常表现的应付，对方则抱怨你不够关心和不顾家，最后这失败的爱情，让你在遗憾和凑合中走完一生。</a></td>
							<td>leedane</td>
							<td>2016-10-10 10:10</td>
							<th width="150" class="text-align-center"><button type="button" class="btn btn-primary">立即审核</button></th>
						</tr> -->
					</tbody>
				</table>
			</div>
	   	</div>	
	</div>
	
<!-- 模态框审核信息 -->
<div class="modal fade" id="check-blog" data-id=0 tabindex="-1" role="dialog" aria-labelledby="checkModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="checkModalLabel">
					文章审核
				</h4>
			</div>
			<div class="modal-body modal-body-check">
				<div class="row">
					<div class="col-lg-12">
						<div class="btn-group" data-toggle="buttons">
							<label class="btn btn-primary check-btn">
								<input type="radio" name="options" id="agree" type-value="1"> 通过
							</label>
							<label class="btn btn-primary check-btn">
								<input type="radio" name="options" id="no-agree" type-value="2"> 不通过
							</label>
						</div>
					</div>
				</div>
				<div class="row" style="margin-top: 8px;">
					<div class="col-lg-12">
						<div class="form-group reason-group">
						  <label for="reason">不通过原因<font color="red">*</font></label>
						  <textarea class="form-control" name="reason" placeholder="请输入详细的原因"></textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary" onclick="check(this);">
					确定
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<body>
<script type="text/javascript">
	var blogs;
	var last_id = 0;
	var first_id = 0;
	var method = 'firstloading';
	//浏览器可视区域页面的高度
	var winH = $(window).height(); 
	var canLoadData = true;
	var isLoad = false;
	$(function(){
		
		//默认查询操作
		queryNoChecks();
		
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
		    if(!isLoad && height < 0.20 && canLoadData && !isLoad){
		    	isLoad = true;
		    	method = 'lowloading';
		    	queryNoChecks();
		    }
		}); 
	});
	
	/**
	 * 获取请求列表
	 */
	function getQueryPagingParams(){
		var pageSize = 15;
		if(method != 'firstloading')
			pageSize = 10;
		return {pageSize: pageSize, last_id: last_id, first_id: first_id, method: method, t: Math.random()};
	}
	
	/**
	* 查询
	*/
	function queryNoChecks(params){
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : getQueryPagingParams(),
			url : getBasePath() +"leedane/blog/noCheckPaging.action",
			dataType: 'json', 
			beforeSend:function(){
			
			},
			success : function(data) {
				layer.close(loadi);
				if(data.isSuccess){
					if(data.message.length == 0){
						canLoadData = false;
						layer.msg("无更多数据");
						return;
					}
						
					if(method == 'firstloading'){
						//清空原来的数据
						$(".each-row").remove();
						blogs = data.message;
						for(var i = 0; i < blogs.length; i++){
							buildEachRow(blogs[i], i);
							if(i == 0)
								first_id = blogs[i].id;
							if(i == blogs.length -1)
								last_id = blogs[i].id;
						}
					}else{
						var currentIndex = blogs.length;
						for(var i = 0; i < data.message.length; i++){
							blogs.push(data.message[i]);
							buildEachRow(data.message[i], currentIndex);
							if(i == data.message.length -1)
								last_id = data.message[i].id;
						}
					}
				}else{
					layer.msg(data.message);
				}
				isLoad = false;
			},
			error : function() {
				layer.close(loadi);
				layer.msg("网络请求失败");
				isLoad = false;
			}
		});
	}
	
	function buildEachRow(blog, index){
		var html = '<tr class="each-row" height="50">'+
						'<td>'+ (index + 1) +'</td>'+
						'<td>'+ changeNotNullString(blog.title) +'</td>'+
						'<td><a href="'+ getBasePath()+'page/detail.jsp?bid='+ blog.id +'" target="_blank">'+ changeNotNullString(blog.digest) +'</a></td>'+
						'<td><a href="'+ getBasePath()+'page/my.jsp?uid='+blog.create_user_id+'" target="_blank">'+ changeNotNullString(blog.account) +'<a/></td>'+
						'<td>'+ changeNotNullString(blog.create_time) +'</td>'+
						'<th width="150" class="text-align-center"><button type="button" class="btn btn-primary" onclick="showCheckDialog(this, '+ index +');">立即审核</button></th>'+
					'</tr>';
		$(".main-table tbody").append(html);
		
	}
	/**
	* 展示审核弹出层
	*/
	function showCheckDialog(obj, index){
		var blog = blogs[index];
		$("#check-blog").find(".modal-body-check label:first-child").addClass("active");
		$("#check-blog").find(".modal-body-check label:eq(1)").removeClass("active");
		$("#check-blog").find('.reason-group').addClass("hide");
		$("#check-blog").attr("data-id", blog.id);
		$("#check-blog").modal("show");
		$("#check-blog").find(".check-btn").on("click", function(){
			if($(this).find("input").attr("id") == "no-agree"){
				$("#check-blog").find(".reason-group").removeClass("hide");
			}else{
				$("#check-blog").find(".reason-group").addClass("hide");
			}
		});
	}
	
	/**
	**	审核
	*/
	function check(obj){
		var blogId = $("#check-blog").attr("data-id");
		var agree = false;
		if(isEmpty(blogId)){
			layer.msg("页面参数有误，请重新刷新！");
			return;
		}
		
		var reason = "";
		if($("#check-blog").find("#no-agree").parent("label").hasClass("active")){
			if(isEmpty($("#check-blog").find('[name="reason"]').val())){
				layer.msg("请输入不通过原因！");
				$("#check-blog").find('[name="reason"]').focus();
				return;
			}else{
				reason = $("#check-blog").find('[name="reason"]').val();
				agree = false;
			}
		}else{
			agree = true;
		}
		
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : {blog_id: blogId, reason: reason, agree: agree, t: Math.random()},
			url : getBasePath() +"leedane/blog/check.action",
			dataType: 'json', 
			beforeSend:function(){
			
			},
			success : function(data) {
				layer.close(loadi);			
				layer.msg(data.message);
				if(data.isSuccess){
					$("#check-blog").modal("hide");
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