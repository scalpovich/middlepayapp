<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>注册</title>

<link rel="stylesheet" type="text/css" href="css/index.css" />
	<script src="js/jquery-1.11.0.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/register.js" type="text/javascript" charset="utf-8"></script>
	<%
		String tgr = request.getParameter("tgr");
		request.setAttribute("tgr",tgr );
	
	 %>
</head>
<body>
	<div id="index">
		<div class="top">
			<div class="logo"></div>
			<ul>
				<li class="phone">
					<input type="number" name="" value="" placeholder="请输入手机号码" id="phone" maxlength="15" >
					
				</li>
				<li class="yzm">
					<input type="number" name="" value="" placeholder="请输入验证码" id="msg" maxlength="8" >
					<button id="yzm" onclick="getyzm(this)">获取验证码</button>
				</li>
				<li class="pwd">
					<input type="password" name="" value="" placeholder="请输入6-20位密码" id="pwd" maxlength="20" >
				</li>
				<li class="repwd">
					<input type="password" name="" value="" placeholder="请再次输入密码" id="repwd" >
				</li>
			</ul>
		</div>
		<p class="agreement">
			<span class="pic"></span>
			<span class="read">我已阅读并同意<a href="javascript:;">《融汇金服签订协议》</a></span>
		</p>
		<input type="submit" value="注册" id="submit"/>
		<input type="hidden" value="" id="sendTime">
		<input type="hidden" value="A003" id="txndir">
		<input type="hidden" value="" id="sendSeqId">
		<input type="hidden" name="" value="${tgr}" id="tgr" />
		<img src="img/btn_bg1.png" alt="" style="display: none;"/>
	</div>
</body>
</html>