<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>注册</title>


	<script src="js/index.js"></script>
	<script src="js/jquery.min.js"></script>
	<script src="js/register.js"></script>
	<link rel="stylesheet" href="css/login.css">
	<link rel="stylesheet" href="css/reset.min.css">
	
	<%
		String tgr = request.getParameter("tgr");
		request.setAttribute("tgr",tgr );
	
	 %>
</head>
<body>



<div id="warp">
		<div class="logo">
			<img src="images/logo.png" alt="">
		</div>
		<form class="input" >
			<label>
				<div>
					<img src="images/iP.png" alt="">
				</div>
				<input type="text" placeholder="请输入手机号" onkeydown="onlyNum();" maxlength="11" id="phone">
			</label>
			<label>
				<div>
					<img src="images/2.png" alt="">
				</div>
				<input type="text"  placeholder="请输入验证码" onkeydown="onlyNum();" id="msg" maxlength="4">
				<span id="yzm" onclick="getyzm(this)">获取验证码</span>
			</label>
			<label>
				<div>
					<img src="images/suo.png" alt="">
				</div>
				<input type="password" placeholder="请输入密码" maxlength="20" id="pwd">
			</label>
		</form>
		<p class="tuiJian">推荐人：<span id="xingming"></span> &nbsp; <span id="shoujihao"></span></p>
		<div class="footer">
			<span id="gou"><img src="" alt=""></span><p>我已阅读并同意<a href="http://app.ronghuijinfubj.com/appserver/register/agree.html">《融汇金服签订协议》</a></p>
		</div>
		<div class="btn">
		
		<button type="submit"  id="submit">注册</button>
		<input type="hidden" value="" id="sendTime">
		<input type="hidden" value="A003" id="txndir">
		<input type="hidden" value="" id="sendSeqId">
		<input type="hidden" name="" value="${tgr}" id="tgr" />
		
			<!-- <button id="btn" onclick="window.location='#';">注册</button> -->
			<button onclick="window.location='http://app.ronghuijinfubj.com/appserver/register/downLoad.html';">爱码付下载</button>
		</div>
	</div>
	<script src='js/login.js'></script>
	
	<script type="text/javascript">
		$(function(){
			$.ajax({
				url : "/appserver/getmerchantinfo",
				data:{
					tjr:"${tgr}"
				},
				success:function(data){
					var objResp = eval(data);
					$("#xingming").html(objResp.xingming);
					$("#shoujihao").html(objResp.shoujihao);
				}
			});
		})
	</script>


</body>
</html>