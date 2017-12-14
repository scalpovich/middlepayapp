$(document).ready(function(){
var u = navigator.userAgent;
var ua = navigator.userAgent.toLowerCase();
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios
var isweChat = ua.match(/MicroMessenger/i)=="micromessenger";//微信内置浏览器
//1.判断微信内置浏览器 及其遮罩
 if(isweChat) {
	$("#download").on("click",function () { 
		if (isiOS) { // 是ios 就跳转
			window.location.href = 'https://itunes.apple.com/cn/app/id1217679017?mt=8';
		} else if(isAndroid){ // 是安卓 就显示安卓弹窗
			$("#weChatWrap").css({"display":"block"});
		}
	});
}else {
		
		$("#download").on("click",function (){
			if (isiOS) { // 是ios 就跳转
				window.location.href = 'https://itunes.apple.com/cn/app/id1217679017?mt=8';
			} else if(isAndroid){ // 是安卓 就显示安卓弹窗
				$("#androidWrap").show("100");
			}
		});
}
})
