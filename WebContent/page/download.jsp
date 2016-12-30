<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>客户端下载</title>
    <style type="text/css">
    	body {
		  padding-top: 40px;
		  padding-bottom: 40px;
		  background-color: #f5f5f5;
		}
		.container{
			margin-top: 180px;
		}
		
    </style>
  </head>
  

  <body onload="init();">
	<%@ include file="/page/common.jsp" %>
	<script type="text/javascript" src="other/layui/layui.js"></script>
	<script type="text/javascript" src="other/layui/lay/dest/layui.all.js"></script>
    <div class="container">
	      欢迎您的使用，请下载最新版的app后使用扫一扫功能。下载地址<span class="download-link"></span>：
    </div> <!-- /container -->
 
  </body>
  <script type="text/javascript">
  /**
	 * 查询获取列表
	 * @param uid
	 */
	function init(){
		var loadi = layer.load('努力加载中…'); //需关闭加载层时，执行layer.close(loadi)即可
		$.ajax({
			type : "post",
			data : {},
			url : getBasePath() +"leedane/appVersion/getNewest.action",
			dataType: 'json', 
			beforeSend:function(){
			},
			success : function(data) {
				layer.close(loadi);
				if(data.isSuccess){
					$(".download-link").html('<a href="'+ data.message[0].path+'">点击下载</a>');
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