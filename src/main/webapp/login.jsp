<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script>

		//页面加载完毕后，将文本框中的内容清空
		$("#loginAct").val("");
		$("#loginPwd").val("");

		$(function (){
			//页面加载完毕后，让用户文本框自动获得焦点
			$("#loginAct").focus();

			//为登录按钮绑定事件，执行登录操作
			$("#regist").click(function (){
				regist();
			})

			//为当前登录页窗口绑定敲键盘事件
			//event：这个参数可以取得我们敲的是哪个键
			$(window).keydown(function (event) {
				//13表示enter键
				if (event.keyCode == 13) {
					regist();
				}
			});
		})

		function regist(){

			//验证账号密码不能为空
			let loginAct = $.trim($("#loginAct").val());
			let loginPwd = $.trim($("#loginPwd").val());
			if(loginAct == "" || loginPwd == ""){
				$("#msg").html("账号密码不能为空");
				//账号密码为空，需要强制终止该方法
				return false;
			}

			//验证账号密码
			$.ajax({
				url:"settings/user/login.do",
				data:{"loginAct":loginAct,"loginPwd":loginPwd},
				type:"POST",
				dataType:"json",
				success:function (data){
					//如果登录成功
					if(data.success){
						//跳转到工作台的初始页
						window.location.href = "workbench/index.html";
					}else {
						//登录失败，展示消息
						$("#msg").html(data.msg);
					}
				}
			})

		}

	</script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
	<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
	<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
	<div style="position: absolute; top: 0px; right: 60px;">
		<div class="page-header">
			<h1>登录</h1>
		</div>
		<form action="workbench/index.html" id="form" class="form-horizontal" role="form">
			<div class="form-group form-group-lg">
				<div style="width: 350px;">
					<input class="form-control" type="text" placeholder="用户名" id="loginAct" name="loginAct">
				</div>
				<div style="width: 350px; position: relative;top: 20px;">
					<input class="form-control" type="password" placeholder="密码" id="loginPwd" name="loginPwd">
				</div>
				<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

					<span id="msg" style="color: red"></span>

				</div>
				<button type="button" id="regist" class="btn btn-primary btn-lg btn-block" style="width: 350px; position: relative;top: 45px;">登录</button>
			</div>
		</form>
	</div>
</div>
</body>
</html>