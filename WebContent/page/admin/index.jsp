<%@page import="java.util.UUID"%>
<%@page import="com.cn.leedane.utils.CommonUtil"%>
<%@page import="com.cn.leedane.model.UserBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cn.leedane.controller.UserController"%>
<%
	Object obj = session.getAttribute(UserController.USER_INFO_KEY);
	UserBean userBean = null;
	String account = "";
	boolean isLogin = false;
	boolean isLoginUser = false;
	String basePath = request.getScheme()+"://"+request.getServerName()
			+":"+request.getServerPort()+request.getContextPath()+"/";
	if(obj != null){
		System.out.print("obj不为空");
		isLogin = !isLogin;
		userBean = (UserBean)obj;
		//后台只有管理员权限才能操作
		if(userBean.isAdmin()){
			isLogin = !isLogin;
			account = userBean.getAccount();
		}else{
			response.sendRedirect(basePath +"page/login.jsp?ref="+CommonUtil.getFullPath(request)+"&t="+UUID.randomUUID().toString());
		}
	}else{
		System.out.print("obj为空");
		response.sendRedirect(basePath +"page/login.jsp?ref="+CommonUtil.getFullPath(request)+"&t="+UUID.randomUUID().toString());
	}
	
%>
<!DOCTYPE HTML>
<html>
 <head>
  <title>leedane后台管理系统</title>
   <meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
   <link href="assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
  <link href="assets/css/bui-min.css" rel="stylesheet" type="text/css" />
   <link href="assets/css/main-min.css" rel="stylesheet" type="text/css" />
   <link rel="stylesheet" href="<%=basePath %>page/other/layui/css/layui.css">
   <script type="text/javascript" src="<%=basePath %>page/other/layui/layui.js"></script>
   <script type="text/javascript" src="<%=basePath %>page/other/layui/lay/dest/layui.all.js"></script>
 </head>
 <body>

  <div class="header">
    
      <div class="dl-title">
        <a href="http://www.builive.com" title="文档库地址" target="_blank"><!-- 仅仅为了提供文档的快速入口，项目中请删除链接 -->
          <span class="dl-title-text">后台管理</span>
        </a>
      </div>

    <div class="dl-log">欢迎您，<span class="dl-log-user"><%=account %></span><a href="javascript:void(0);" title="退出系统" class="dl-log-quit" onclick="logout()">[退出]</a>
    </div>
  </div>
   <div class="content">
    <div class="dl-main-nav">
      <div class="dl-inform"><div class="dl-inform-title">贴心小秘书<s class="dl-inform-icon dl-up"></s></div></div>
      <ul id="J_Nav"  class="nav-list ks-clear">
        <li class="nav-item dl-selected"><div class="nav-item-inner nav-home">首页</div></li>
        <li class="nav-item"><div class="nav-item-inner nav-user">用户管理</div></li>
        <li class="nav-item"><div class="nav-item-inner nav-blog">博客管理</div></li>
        <li class="nav-item"><div class="nav-item-inner nav-order">表单页</div></li>
        <li class="nav-item"><div class="nav-item-inner nav-inventory">搜索页</div></li>
        <li class="nav-item"><div class="nav-item-inner nav-supplier">详情页</div></li>
        <li class="nav-item"><div class="nav-item-inner nav-marketing">图表</div></li>
      </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
   </div>
  <script type="text/javascript" src="assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="./assets/js/bui.js"></script>
  <script type="text/javascript" src="./assets/js/config.js"></script>

  <script>
	//退出登录
	function logout(){
		var params = {t: Math.random()};
		$.ajax({
			type : "post",
			data : params,
			dataType: 'json',
			url : "<%=basePath %>leedane/user/logout.action",
			beforeSend:function(){
			},
			success : function(data) {
				layer.msg(data.message);
				if(data.isSuccess)
					//刷新当前页面
					window.location.reload();					
			},
			error : function() {
				layer.msg("网络请求失败");
			}
		});
	}
	
    BUI.use('common/main',function(){
      var config = [{
          id:'welcome', 
          homePage : 'welcome',
          menu:[{
              text:'基本操作',
              items:[
                {id:'welcome',text:'欢迎页面',href:'welcome/welcome.jsp'},
                {id:'history',text:'登录历史',href:'welcome/loginHistory.jsp'},
                {id:'about',text:'关于我们',href:'welcome/about.jsp'},
                {id:'contact',text:'联系我们',href:'welcome/contact.jsp'},
                {id:'appdownload',text:'APP下载',href:'welcome/appdownload.jsp'}
              ]
            }]
          },{
          id:'user', 
          homePage : 'search',
          menu:[{
              text:'用户管理',
              items:[
                {id:'search',text:'查询用户',href:'user/search.jsp',closeable : false},
                {id:'new',text:'新增用户',href:'user/new.jsp'},
                {id:'black',text:'黑名单用户',href:'user/black.jsp'}
              ]
            },{
              text:'用户统计',
              items:[
                {id:'chart',text:'图表展示',href:'user/chart.jsp'},
              ]
            },{
              text:'角色管理',
              items:[
                {id:'resource',text:'添加权限',href:'user/addRole.jsp'},
                {id:'loader',text:'管理权限',href:'user/managerRole.jsp'},
                {id:'resource',text:'用户授权',href:'user/authorizationRole.jsp'}
              ]
            }]
          },{
              id:'blog', 
              homePage : 'publish',
              menu:[{
                  text:'博客管理',
                  items:[
                    {id:'publish',text:'写一博',href:'blog/publish.jsp',closeable : false},
                    {id:'search',text:'查询博客',href:'blog/search.jsp'},
                    {id:'draft',text:'博客草稿',href:'blog/draft.jsp'}
                  ]
                },{
                  text:'博客统计',
                  items:[
                    {id:'chart',text:'图表展示',href:'blog/chart.jsp'}
                  ]
                },{
                  text:'博客审核',
                  items:[
                    {id:'check',text:'博客审核',href:'blog/check.jsp'}
                  ]
                }]
             },{
            id:'form',
            menu:[{
                text:'表单页面',
                items:[
                  {id:'code',text:'表单代码',href:'form/code.html'},
                  {id:'example',text:'表单示例',href:'form/example.html'},
                  {id:'introduce',text:'表单简介',href:'form/introduce.html'},
                  {id:'valid',text:'表单基本验证',href:'form/basicValid.html'},
                  {id:'advalid',text:'表单复杂验证',href:'form/advalid.html'},
                  {id:'remote',text:'远程调用',href:'form/remote.html'},
                  {id:'group',text:'表单分组',href:'form/group.html'},
                  {id:'depends',text:'表单联动',href:'form/depends.html'}
                ]
              },{
                text:'成功失败页面',
                items:[
                  {id:'success',text:'成功页面',href:'form/success.html'},
                  {id:'fail',text:'失败页面',href:'form/fail.html'}
                
                ]
              },{
                text:'可编辑表格',
                items:[
                  {id:'grid',text:'可编辑表格',href:'form/grid.html'},
                  {id:'form-grid',text:'表单中的可编辑表格',href:'form/form-grid.html'},
                  {id:'dialog-grid',text:'使用弹出框',href:'form/dialog-grid.html'},
                  {id:'form-dialog-grid',text:'表单中使用弹出框',href:'form/form-dialog-grid.html'}
                ]
              }]
          },{
            id:'search',
            menu:[{
                text:'搜索页面',
                items:[
                  {id:'code',text:'搜索页面代码',href:'search/code.html'},
                  {id:'example',text:'搜索页面示例',href:'search/example.html'},
                  {id:'example-dialog',text:'搜索页面编辑示例',href:'search/example-dialog.html'},
                  {id:'introduce',text:'搜索页面简介',href:'search/introduce.html'}, 
                  {id:'config',text:'搜索配置',href:'search/config.html'}
                ]
              },{
                text : '更多示例',
                items : [
                  {id : 'tab',text : '使用tab过滤',href : 'search/tab.html'}
                ]
              }]
          },{
            id:'detail',
            menu:[{
                text:'详情页面',
                items:[
                  {id:'code',text:'详情页面代码',href:'detail/code.html'},
                  {id:'example',text:'详情页面示例',href:'detail/example.html'},
                  {id:'introduce',text:'详情页面简介',href:'detail/introduce.html'}
                ]
              }]
          },{
            id : 'chart',
            menu : [{
              text : '图表',
              items:[
                  {id:'code',text:'引入代码',href:'chart/code.html'},
                  {id:'line',text:'折线图',href:'chart/line.html'},
                  {id:'area',text:'区域图',href:'chart/area.html'},
                  {id:'column',text:'柱状图',href:'chart/column.html'},
                  {id:'pie',text:'饼图',href:'chart/pie.html'}, 
                  {id:'radar',text:'雷达图',href:'chart/radar.html'}
              ]
            }]
          }];
      new PageUtil.MainPage({
        modulesConfig : config
      });
    });
  </script>
 </body>
</html>
