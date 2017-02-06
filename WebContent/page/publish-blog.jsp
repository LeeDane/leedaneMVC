<%@page import="com.cn.leedane.utils.CommonUtil"%>
<%@page import="com.cn.leedane.utils.EnumUtil.BlogCategory"%>
<%@page import="com.cn.leedane.utils.EnumUtil"%>
<%@page import="com.cn.leedane.utils.StringUtil"%>
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
	boolean isLoginUser = false;
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
	}else{
		String bp = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/";
		response.sendRedirect(bp +"page/login.jsp?ref="+CommonUtil.getFullPath(request)+"&t="+UUID.randomUUID().toString());
	}
	String uid = request.getParameter("uid");
	
	if(StringUtil.isNull(uid) && userBean != null)
		uid = String.valueOf(userBean.getId());
	
	if(userBean != null && StringUtil.isNotNull(uid))
		isLoginUser = (Integer.parseInt(uid) == userBean.getId());
	
	int blogId = 0;
	String bidStr = request.getParameter("bid");
	if(StringUtil.isNotNull(bidStr))
		blogId = StringUtil.changeObjectToInt(bidStr);
	
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
	<title>记一博</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	<style type="text/css">
		.clearFloat{
			clear: both;
		}
		<% if(!noHeader1){ %>
		.container{
			margin-top: 50px;
		}
		<% } %>
		.blog-info{
			margin-top: 15px;
		}
		.must-not-null{
			margin-left: 2px;
			color: red;
		}
		.tag-list{
		}
		.tag-item{
			 margin-right: 10px;
		}
		.badge {
	    	background-color: #3071a9 !important;
	    	margin-right: 5px;
	    }
	</style>
</head>
<body>
<%@ include file="common.jsp" %>
<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=basePath %>page/other/ueditor1_4_3_3-utf8-jsp/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=basePath %>page/other/ueditor1_4_3_3-utf8-jsp/ueditor.all.min.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="<%=basePath %>page/other/ueditor1_4_3_3-utf8-jsp/lang/zh-cn/zh-cn.js"></script>
<script src="<%=basePath %>page/js/publish-blog.js"></script>
<div class="main clearFloat">	
	<div class="container">
		<% if(userBean != null && !userBean.isAdmin()){ %>
		<div class="row">
			<div class="col-lg-12">
				<div class="alert alert-warning alert-dismissible" role="alert">
				  <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				  <strong>警告!您是非管理员账号，发布的文章需要等待管理员审核!</strong>
				</div>
			</div>
		</div>
		<%} %>
		<div class="row blog-info">
	   		<div class="col-lg-9">
	   			<div class="form-group">
	   				<label for="name">标题<font class="must-not-null">*</font></label>
				    <input class="form-control" type="text" name="title" placeholder="请输入标题">
				 </div>
				 <div class="form-group">
				    <label for="name">摘要(<font class="must-not-null">为空将从内容中摘取</font>)</label>
				    <textarea class="form-control" rows="3" name="digest"></textarea>
				  </div>
	   		</div>
	   		<!-- 分类 -->
	   		<div class="col-lg-3">
	   			<div class="form-group" style="width: 100%; margin-top: 24px;">
					<select class="form-control" name="category">
						<%
							for(BlogCategory ts: EnumUtil.BlogCategory.values()){
						%>
								<option value="<%=ts.name() %>" text="<%=ts.name() %>"><%=ts.name() %></option>
						<%} %>
					</select>
				</div>
				<button type="button" class="btn btn-primary btn-sm" style="width: 100%; margin-bottom: 8px;" onclick="draftlist();">查看草稿列表</button>
	   		</div>
	   </div>
	   <div class="row baidu-editor-container">
	   		<div class="col-lg-12">
	   			<script id="editor" type="text/plain" style="width:100%;"></script>
	   		</div>
	   </div>
	   <div class="row" style="margin-top: 5px;">
	   		<div class="col-lg-12">
	   			<label for="name">标签</label>
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-4">
	   			<div class="form-group">
				    <input type="text" class="form-control tag-input" placeholder="请输入标签，最多3个，每个限制10位，回车添加" onkeypress="if (event.keyCode == 13) addTag(this);">
				 </div>
	   		</div>
	   		<div class="col-lg-8">
	   			<div class="tag-list">
	   			
	   			</div>
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12">
	   			<div class="checkbox">
					<label>
						<input type="checkbox" name="has_img">是否有图
					</label>
				</div>
	   		</div>
	   </div>
	   <div class="row hidden img-url-row">
	   		<div class="col-lg-4">
	   			<input type="text" class="form-control" name="img_url" placeholder="请输入能直接访问的网络图片链接">
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12">
	   			<div class="checkbox">
					<label>
						<input type="checkbox" checked="checked" name="is_original">是否原创
					</label>
				</div>
	   		</div>
	   </div>
	   <div class="row hidden is-original-row">
	   		<div class="col-lg-4">
	   			<input type="text" class="form-control" name="origin_link" placeholder="请输入能直接访问的原文的路径">
	   		</div>
	   		<div class="col-lg-4">
	   			<input type="text" class="form-control" name="source" placeholder="请输入网站的名称(别名)">
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12">
	   			<div class="checkbox">
					<label>
						<input type="checkbox" checked="checked" name="can_comment" title="该功能暂时不能使用" disabled>是否能评论
					</label>
				</div>
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12">
	   			<div class="checkbox">
					<label>
						<input type="checkbox" checked="checked" name="can_transmit" title="该功能暂时不能使用" disabled>是否能转发
					</label>
				</div>
	   		</div>
	   </div>
	   <div class="row">
	   		<div class="col-lg-12">
	   			<div class="checkbox">
					<label>
						<input type="checkbox" checked="checked" name="public" title="该功能暂时不能使用" disabled>是否公开
					</label>
				</div>
	   		</div>
	   </div>
	   
	   <div class="row" style="display: none;">
	   		<input type="hidden" name="create_user_id">
	   </div>
	   <div class="row" style="margin-bottom: 20px;">
	   		<div class="col-lg-12" style="text-align: center;">
	   			<button type="button" class="btn btn-primary btn-sm" style="margin-right: 10px;" onclick="release();">发布文章</button>
	   			<button type="button" class="btn btn-primary btn-sm" onclick="draft();">存为草稿</button>
	   		</div>
	   </div>
	</div>
</div>

<!-- 模态框发布心情列表 -->
<div class="modal fade" id="load-draft" tabindex="-1" role="dialog" aria-labelledby="loadDraftListModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="loadDraftListModalLabel">
					草稿列表
				</h4>
			</div>
			<div class="modal-body">
				<div class="list-group" id="draft-group-list">
					<!-- <div class="list-group-item">
						<div class="row">
							<div class="col-lg-1 col-sm-1">
								<span class="badge">1</span>
							</div>
							<div class="col-lg-7 col-sm-7">
								你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗你啊，怎么这么逗
							</div>
							<div class="col-lg-4 col-sm-4">
								2016-10-10 10:00:00
							</div>
						</div>
					</div> -->
				</div> 
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
</body>
	<script type="text/javascript">
	var isLogin = <%=isLogin %>; //是否已经登录
	//实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    //var ue = UE.getEditor('editor');
    var ue = UE.getEditor('editor', {
        "initialFrameHeight": "200",
        
        toolbars: [[
	              'fullscreen', 'source', '|', 'undo', 'redo', '|',
	              'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
	              'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
	              'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
	              'directionalityltr', 'directionalityrtl', 'indent', '|',
	              'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
	              'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
	              'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertvideo'/* , 'music' MP3*/, 'attachment', 'map', /* 'gmap', 谷歌地图 */ 'insertframe', 'insertcode'/* , 'webapp' 百度应用 */, 'pagebreak', 'template', 'background', '|',
	              'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
	              'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
	              'print', 'preview', 'searchreplace', 'drafts'/* , 'help' 帮助 */
	          ]],
        enterTag: "&nbsp;"
    });
    var bid = <%=blogId %>;
    $(function(){
    	if(bid > 0){
    		ue.addListener("ready", function () {
    			// editor准备好之后才可以使用,不然要是ue还没有初始化完成就调用就会报错
    			getEditBlog(bid);
    		});
    		
    	}
    	
    	//是否有主图
    	$('[name="has_img"]').click(function(){ 
    		if(this.checked){
    			$(".img-url-row").removeClass("hidden");
    		}else{
    			$(".img-url-row").addClass("hidden");
    		}
    	}); 
		
    	//是否原创
    	$('[name="is_original"]').click(function(){ 
    		if(!this.checked){
    			$(".is-original-row").removeClass("hidden");
    		}else{
    			$(".is-original-row").addClass("hidden");
    		}
    	}); 
	});
    
    
	</script>
</html>