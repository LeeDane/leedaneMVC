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
		response.sendRedirect(bp +"page/login.jsp?ref="+request.getRequestURL()+"&t="+UUID.randomUUID().toString());
	}
	String uid = request.getParameter("uid");
	
	if(StringUtil.isNull(uid) && userBean != null)
		uid = String.valueOf(userBean.getId());
	
	if(userBean != null && StringUtil.isNotNull(uid))
		isLoginUser = (Integer.parseInt(uid) == userBean.getId());
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>记一博</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	
	<script src="js/base.js"></script>
	<style type="text/css">
		.clearFloat{
			clear: both;
		}
		.baidu-editor-container{
			margin-top: 50px;
		}
	</style>
</head>
<body>
<%@ include file="common.jsp" %>

<script type="text/javascript" src="<%=basePath %>page/other/layui/layui.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/layui/lay/dest/layui.all.js"></script>
<script src="<%=basePath %>page/js/publish-blog.js"></script>
<script type="text/javascript" src="<%=basePath %>page/other/jquery.md5.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=basePath %>page/other/ueditor1_4_3_3-utf8-jsp/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=basePath %>page/other/ueditor1_4_3_3-utf8-jsp/ueditor.all.min.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="<%=basePath %>page/other/ueditor1_4_3_3-utf8-jsp/lang/zh-cn/zh-cn.js"></script>
    
<div class="main clearFloat">	
	<div class="container">
	   <div class="row baidu-editor-container">
	   		<div class="col-lg-12">
	   			<script id="editor" type="text/plain" style="width:1024px;height:500px;"></script>
	   		</div>
	   </div>
	</div>
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
	              'simpleupload', 'insertimage', 'emotion', 'scrawl', 'insertvideo'/* , 'music' MP3*/, 'attachment', 'map', /* 'gmap', 谷歌地图 */ 'insertframe', 'insertcode', 'webapp', 'pagebreak', 'template', 'background', '|',
	              'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
	              'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
	              'print', 'preview', 'searchreplace', 'drafts', 'help'
	          ]],
        enterTag: "&nbsp;"
    });
	</script>
</html>