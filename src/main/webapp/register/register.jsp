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
			<img src="./images/logo.png" alt="">
			<img src="./images/aimafu.png" alt="">
		</div>
		<form class="input" >
			<label>
				<div>
					<img src="images/1.png" alt="">
				</div>
				<input type="text" placeholder="请输入手机号" onkeydown="onlyNum();" maxlength="11" id="phone">
			</label>
			<label>
				<div>
					<img src="images/2.png" alt="">
				</div>
				<input type="text"  placeholder="请输入短信验证码" onkeydown="onlyNum();" id="msg" maxlength="4">
				<span id="yzm" onclick="getyzm(this)">获取验证码</span>
			</label>
			<label>
				<div>
					<img src="images/3.png" alt="">
				</div>
				<input type="password" placeholder="请设置登录密码" maxlength="18" id="pwd" class='pas'  onkeyup="this.value=this.value.replace(/\s+/g,'')">
			</label>
		</form>
		<div class="footer">
			<span id="gou"><img src="" alt=""></span><p>我已阅读并同意<a href="http://app.ronghuijinfubj.com/appserver/register/agree.html">《融汇金服签订协议》</a></p>
		</div>
		<div class="btn">
		
		<button type="submit"  id="submit" >注册</button>
		<p class="tuiJian">推荐人：<span id="xingming"></span> &nbsp; <span id="shoujihao"></span></p>
		<input type="hidden" value="" id="sendTime">
		<input type="hidden" value="A003" id="txndir">
		<input type="hidden" value="" id="sendSeqId">
		<input type="hidden" name="" value="${tgr}" id="tgr" />
		
			<!-- <button id="btn" onclick="window.location='#';">注册</button> -->
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
		
		$('.pas').blur(function(){
			if($('.pas').val().length < 6 || $('.pas').val().length > 18){
				$('.pas').val('');
				alert('请输入6-18位密码');
			}
		});

	</script>


</body>
</html>