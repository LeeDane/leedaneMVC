<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

	String basePath = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/"; 
	String content = String.valueOf(request.getAttribute("content"));
	String imgs = String.valueOf(request.getAttribute("imgs"));
	String deviceWidth = String.valueOf(request.getAttribute("device_width"));
%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1">
<title>全文阅读</title>

<!-- 百度JQUERYCDN -->
<script type="text/javascript" src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style type="text/css">
	*{
		padding: 0;
		margin: 0;
	}
	
</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-lg-12 col-sm-12" id="content" style="width: 100%;overflow: hidden;">
				<%=content %>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript">
	$(function(){
	});
	
	function clickImg(obj, index){
		var screenW = !(typeof winW === 'undefined');
		//大屏幕下 
		if(screenW && winW > 800){
			showImg(index);
		}else{
			
			var width = parseInt($(obj).width());
			var height = parseInt($(obj).height());
			webview.clickImg("<%=imgs %>", index, width, height);
		}
	}
	
	//展示图片的链接
	function showImg(index){
		var imgs = '<%=imgs %>';
		
		//切割获取图像数组
		var imgArr = imgs.split(";");
		var json = {
				  "title": "相册标题", //相册标题
				  "id": 0, //相册id
				  "start": index //初始显示的图片序号，默认0
				};
		var datas = new Array();
		for(var i = 0; i < imgArr.length; i++){
			var each = {};
			var path = imgArr[i];
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
	
</script>
</html>