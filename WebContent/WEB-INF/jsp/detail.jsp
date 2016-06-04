<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
	<base href="<%=basePath%>">
	<title>秒杀详情页</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- 引入 Bootstrap -->
	<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
	<div class="container">
		<div class="panel panel-defualt text-center">
			<div class="panel-heading">
				<h1>${seckill.name }</h1>
			</div>
			
			<div class="panel-body">
				<h2 class="text-danger">
					<!-- 显示time图标 -->
					<span class="glyphicon glyphicon-time"></span>
					<!-- 显示倒计时 -->
					<span class="glyphicon" id="seckill-box"></span>
				</h2>
			</div>
		</div>
	</div>
	<!-- 登录弹出层，输入电话 -->
	<div id="killPhoneModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h3 class="modal-title text-center">
						<span class="glyphicon glyphicon-phone"></span>秒杀电话:
					</h3>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-xs-8 col-xs-offset-2">
							<input type="text" name="killPhone" id="killPhoneKey" 
									placeholder="填手机号^o^" class="form-control"/>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<!-- 验证信息 -->
					<span id="killPhoneMessage" class="plyphicon"></span>
					<button type="button" id="killPhoneBtn" class="btn btn-success">
						<span class="plyphicon plyphicon-phone"></span>
						Submit
					</button>
				</div>
			</div>
		</div>
	</div>
	<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
	<!-- 包括所有已编译的插件 -->
 	<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<!-- jQuery cookie操作插件 -->
	<script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	<!-- jQuery countDown倒计时插件 -->
	<script src="//cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
	<!-- 编写交互逻辑 -->
	<script src="resources/script/seckill.js"></script>
	<script type="text/javascript">
		$(function(){
			seckill.detail.init({
				seckillId : ${seckill.seckillId },
				startTime : ${seckill.startTime.time },
				endTime : ${seckill.endTime.time}
			});
		});
	</script>
</body>
</html>