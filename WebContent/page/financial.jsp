<%@page import="com.cn.leedane.utils.CommonUtil"%>
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
	if(isLogin){
		userBean = (UserBean)obj;
		account = userBean.getAccount();
	}else{
		String bp = request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath()+"/";
		response.sendRedirect(bp +"page/login.jsp?ref="+CommonUtil.getFullPath(request)+"&t="+UUID.randomUUID().toString());
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><%=account %>的记账</title>
	<link rel="stylesheet" href="other/layui/css/layui.css">
	
	<style type="text/css">
		body{
			background-color: #f5f5f5 !important;
		}
		.container{
			margin-top: 60px;
		}
		.senior-condition{
			margin-top: 8px;
			display: none;
		}
		.financial-list-row{
			margin-top: 16px;
			border: 1px solid #ddd;
			background-color: #fff !important;
			border-radius: 4px;
			padding-bottom: 8px;
			padding-top: 8px;
		}
		
	</style>
</head>
<body data-spy="scroll" data-target="#myScrollspy" data-offset="90">
<%@ include file="/page/common.jsp" %>
<script src="<%=basePath %>page/js/financial.js"></script>
<script src="<%=basePath %>page/other/echarts.min.js"></script>
<div class="container main-container">
	   <div class="row">
		   <div class="col-lg-12">
		   		<div class="row">
		   			<div class="col-lg-4">
		   				 <input type="text" name="searchKey" class="form-control" placeholder="请输入搜索关键字" onkeypress="if (event.keyCode == 13) search(this);">
		   			</div>
		   			<div class="col-lg-4">
		   				<button type="button" class="btn btn-primary" onclick="search(this);">搜索</button>
		   				<button type="button" class="btn btn-primary senior-condition-btn">高级条件</button>
		   			</div>
		   		</div>
		   		
		   		<div class="row senior-condition">
		   			<div class="col-lg-4">
		   				<div class="row">
		   					<div class="col-lg-12">
		   						<label for="" class="col-sm-3 control-label">开始时间</label>
		   					</div>
		   				</div>
		   				<div class="row">
			   				 <div class="form-group">
							    <div class="col-sm-12">
							      <input type="date" name="startTime" class="form-control" placeholder="开始时间">
							   </div>
							 </div>
						 </div>
		   			</div>
		   		</div>
		   		<div class="row senior-condition">
		   			<div class="col-lg-4">
		   				<div class="row">
		   					<div class="col-lg-12">
		   						<label for="" class="col-sm-3 control-label">结束时间</label>
		   					</div>
		   				</div>
		   				<div class="row">
			   				 <div class="form-group">
							    <div class="col-sm-12">
							      <input type="date" name="endTime" class="form-control" placeholder="开始时间">
							   </div>
							 </div>
						 </div>
		   			</div>
		   		</div>
		   		<div class="row senior-condition">
		   			<div class="col-lg-4">
		   				 <div class="form-group">
						     <select class="form-control" name="levels">
						     <!-- 分类 -->
						    </select>
						 </div>
		   			</div>
		   		</div>
		   </div>
	   </div> 
	   <div class="row" style="text-align: right;">
	   		<button class="btn btn-success" onclick="addInfo();">记一笔</button>
   			<button type="button" class="btn btn-primary chart-or-list">切换图表</button>
	   </div>
	    <div class="row financial-chart hide">
	   		<div class="col-lg-6" id="chart-payments" style="height: 300px; ">
	   			
	   		</div>
   			<div class="col-lg-6" id="chart-categorys" style="height: 300px; ">
   			
   			</div>
	   </div>
	    <div class="row financial-chart hide">
	   		<div class="col-lg-12" id="chart-times" style="height: 300px; ">
	   			
	   		</div>
	   </div>
</div>
<!-- 模态框编辑 -->
<div class="modal fade" id="edit-financial-info" tabindex="-1" role="dialog" aria-labelledby="editFinancialInfoModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="editFinancialInfoModalLabel">
					编辑记账基本信息
				</h4>
			</div>
			<div class="modal-body modal-body-edit-financialinfo">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary edit-financial-info-btn" onclick="editInfo(this);">
					编辑
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
</body>
<script type="text/javascript">
	
</script>
</html>