

function getyzm(obj){
		var user=document.getElementById("phone");
		var reg=/^1[3|4|5|7|8]\d{9}$/;
		if(user.value==""||(reg.test(phone.value)==false)){
			alert("请输入格式正确的手机号！");
			user.focus();
			return false;
		}
		var yzm = document.getElementById('yzm');
		var userId = $('#phone').val();
		var jsonData = "{\"loginID\":\""+userId+"\",\"txndir\":\"A007\",\"version\":\"shuabei\",\"sendSeqId\":\""+$("#sendTime1").val() 
				+ "\",\"terminalInfo\":\"123\" , \"sendTime\" : \"" +$("#sendTime1").val() +"\"}";
			settime(obj);
			$.ajax({
				type: "post",
				url:"/appserver/requestentry",
				data:{
					data:jsonData
				},
				success: function(data, textStatus){
					var objResp = eval('(' + data + ')');
					if(objResp.respCode=="00"){
						wx.closeWindow;
					}else{
						alert(objResp.respDesc);
					}
				},
				complete: function(XMLHttpRequest, textStatus){
				//HideLoading();
				},
				error: function(){
					alert("验证码发送失败");
				}
			});
		
	}


	var countdown=60; 
	function settime(val) { 
		if (countdown == 0) { 
			val.disabled=false;
			val.innerHTML="免费获取验证码"; 
			countdown = 60; 
		} else { 
			document.getElementById('yzm').removeAttribute('onclick');
			val.disabled=true;
			val.innerHTML="重新发送(" + countdown + ")";
			countdown--; 
			setTimeout(function() { settime(val) },1000);
			setTimeout(function(){
				document.getElementById('yzm').setAttribute('onclick','getyzm(this)');
			},60000)
		} 
	}
	
$(function(){
	sendTime();
	MathRand();
	$('.pic').toggle(function(){
		$('.pic').css('background-image','url(img/no_checked.png)');
		$('#submit').attr('class','notclick');
	},function(){
		$('.pic').css('background-image','url(img/checked.png)');
		$('#submit').removeAttr('class','notclick');
	})
	$('#submit').click(function(){
		var sendTime=$('#sendTime').val();
		var sendSeqId=$('#sendSeqId').val();
		var reg=/^1[3|4|5|7|8]\d{9}$/;
		var phone=$('#phone');
//		if(reg.test(phone.val())==false){
//			alert("请输入格式正确的手机号！");
//			phone.focus();
//			return;
//		}
		
		var reg=/^[A-Za-z0-9]{6,20}$/;
		var pwd=$('#pwd');
		if(reg.test(pwd.val())==false){
			alert("请输入6-20位字母与数字结合的密码！");
			return;
			pwd.focus();
		}
		
		if($("#submit").hasClass('notclick')){
			alert('请阅读并同意本公司的《融汇金服签订协议》！');
			return;
		}
		var loginID=$('#phone').val();
		var msg=$('#msg').val();
		var loginPwd=$("#pwd").val();
		var tgr=$("#tgr").val();
		var txndir=$('#txndir').val();
		var msg = $('#msg').val();
		
		/**
		 * url = http://192.168.17.190:9090/appserver/requestentry
		 * 参数 ： sendTime   当前时间
		 *        txndir   交易类型 A003
		 *        sendSeqId  终端流水号 6位随机数
		 *        loginID   登录手机号
		 *        loginPwd  登录密码
		 *        tgr       推广人
		 * 
		 * 
		 * 
		 */
		
		var jsonData = "{\"sendTime\":\""+sendTime+"\",\"txndir\":\""+txndir+"\",\"sendSeqId\":\""+sendSeqId+"\",\"loginID\":\""
		+loginID+"\",\"loginPwd\":\""+loginPwd+"\",\"tgr\":\""+tgr+"\",\"terminalInfo\":\""+ sendSeqId +"\",\"smsCode\":\""+msg+"\"}";

		$.ajax({
			type: "post",
			url:"/appserver/requestentry" , // + jsonData,
			data:{
				data:jsonData
			},
			success: function(data, textStatus){
				var objResp = eval(data);
				if(objResp.respCode=="00"){
					alert("注册成功")
					window.location.href="downLoad.html";
				}else{
					alert(objResp.respDesc);
				}
			},
			error: function(){
				alert("提交失败，请稍后重试");
			}
		});
	})
})								
function sendTime(){
			var now=new Date();
			var month=now.getMonth()+1;
			if (month<10) {
				month='0'+month;
			}
			var date=now.getDate();
			if (date<10) {
				date='0'+date;
			}
			var minute=now.getMinutes();
			if (minute<10) {
				minute='0'+minute;
			}
			var hour=now.getHours();
			if (hour<10) {
				hour='0'+hour;
			}
			var second=now.getSeconds();
			if (second<10) {
				second='0'+second;
			}
			var timer1 = now.getFullYear()+"" + month+"" + date+"" + hour+"" + minute+"" + second+"";
			$('#sendTime').val(timer1);
}

function MathRand(){ 
	var Num=""; 
	for(var i=0;i<6;i++){ 
		Num+=Math.floor(Math.random()*10); 
	}
	$('#sendSeqId').val(Num);
	
}