$(function(){
	var w=document.documentElement.clientWidth;
	var a=$("html").css("font-size");
	var ws=(w*4/75).toFixed(5)+"px";
	$("html").css({"font-size":ws});
})
//function is_weixin(){
//  var ua = navigator.userAgent.toLowerCase();
//  if(ua.match(/MicroMessenger/i)=="micromessenger") {
//      return true;
//   } else {
//      return false;
//  }
//}
//if (!is_weixin()) {
//  window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxdf3f22ebfe96b912&redirect_uri=xxx&response_type=code&scope=snsapi_base&state=hyxt#wechat_redirect';
//}