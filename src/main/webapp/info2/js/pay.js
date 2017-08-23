//$(function(){
//	var flag;
//	if((navigator.userAgent).indexOf('MicroMessenger')>'0'){
//		flag=4;  //微信	
//	}else if((navigator.userAgent).indexOf('AlipayClient')>'0'){
//		flag=5;  //支付宝
//	}
//	var jsonData="{'merchantNo':merchantNo,'encrypt':encrypt，'flag':flag}";
//	var jsondata="{\"merchantNo\":\""+merchantNo+"\",\"encrypt\":\""+encrypt+"\",\"flag\":\""+flag+"\"}"
//	
//	$.ajax({
//		type:'post',
////		url:'http://admin.ronghuijinfubj.com/middlepayboss/merchant/queryMerchant?merchantNo='+GetQueryString('merchantNo')+'&encrypt='+GetQueryString('encrypt')+flag,
//		url:'http://admin.ronghuijinfubj.com/middlepayboss/merchant/queryMerchant',
//		data:jsonData+flag,
//		dataType:'json',
//		success:function(data, textStatus){
//			var objResp = eval('(' + data + ')');
//			if(objResp.respCode=="0000"){
//				$('#merchantName').val(objResp.merchantName);
//				$('.header').text(merchantName);
//				$('#bankCode').val(objResp.bankCode);
//				$('#accountName').val(objResp.accountName);
//				$('#accountNo').val(objResp.accountNo);
//				$('#idCardNo').val(objResp.idCardNo);
//				$('#signKey').val(objResp.signKey);
//			}else{
//				alert(objResp.respMsg);
//			}
//		},
//		error:function(){
//			alert('请求失败，请稍后重试');
//		}
//	})
//	
//})
var num = 0;
var dec = 0;
var pnt = false;
var stp = 0;
var isLock = false;
//	function RequestModel(userId,merName,amount){
//		this.userId = userId;
//		this.merName = merName;
//		this.amount = amount;
//	}
function encryptByDES(message, key) {    
    var keyHex = CryptoJS.enc.Utf8.parse(key);  
    var encrypted = CryptoJS.DES.encrypt(message, keyHex, {    
   		mode: CryptoJS.mode.ECB,    
    	padding: CryptoJS.pad.Pkcs7    
    });   
    return encrypted.toString();    
}
function formatAmount(){
	var tmp = num.toString();
	if(pnt){
		tmp += '.' + (dec + 100).toString().substr(1);
	}else{
		tmp += '.00';
	}
	$(".amount").text('￥ ' + tmp);
}
function getPayLink(amt){
	if(isLock){
		return;
	}
	if(Number(amt) == 0){
		$(".message").text('金额不能为零');
		num = 0;
		dec = 0;
		stp = 0;
		pnt = false;
		return;
	}
//	if(Number(amt) > 10000.00){
//		$(".message").text('金额超过限额，当前限额是 10000.00元');
//		return;
//	}
	var tmp = amt.split('.');
	amt = tmp[0] + tmp[1];
	amt = parseInt(amt);
	isLock = true;
}
function clearActive(){
	$(".active").removeClass('active');
	$(".amount").removeClass('deactive');
}
$(function(){
	$(".key").click(function(){
		var key = $(this).text();
		$(this).addClass('active');
		$(".amount").addClass('deactive');
		var t = setTimeout(clearActive, 50);
		if(isLock){
			return;
		}
		switch(key){
			case '':
				break;
			case '付款':
				var tmp = $(".amount").text().split(' ');
				getPayLink(tmp[1]);
				break;
			case '清除':
//				num = 0;
//				dec = 0;
//				stp = 0;
//				pnt = false;
				var tmp = $(".amount").text().split(' ');
				var amount=$('.amount').text();
	  			var amounts= amount.substr(amount.indexOf("￥")+1);
	  			var tmp=parseFloat(amounts);
	  			tmp=' '+tmp;
	  			console.log(tmp);
	  			tmp=tmp.substring(0,tmp.length-1);
	  			console.log(tmp);
	  			alert(tmp);
	  			var clearAfter=tmp;
	  			$('.amount').html(clearAfter);
	  			
				
				break;
			case '.':
				pnt = true;
				break;
			default:
				$(".message").text('');
				if(pnt){
					if(stp == 0){
						dec = Number(key) * 10;
						stp++;
					}else if(stp == 1){
						dec += Number(key);
						stp++;
					}
				}else{
					if(num.toString().length < 9){
						num = num * 10 + Number(key);
					}else{
//						$(".message").text('输入金额过大');
						alert('金额输入过大');
					}
				}
				break;
		}
		formatAmount();
	})

//	function orderTime(){
//		var today=new Date;
//		var month=today.getMonth()+1;
//		if (month<10) {
//		}
//		var dates=today.getDate();
//		if (dates<10) {
//			dates='0'+dates;
//		}
//		var minute=today.getMinutes();
//		if (minute<10) {
//			minute='0'+minute;
//		}
//		var hour=today.getHours();
//		if (hour<10) {
//			hour='0'+hour;
//		}
//		var order1=GetQueryString('merchantNo')+today.getFullYear()+''+month+''+dates+''+hour+''+minute+''+today.getSeconds()+''+today.getMilliseconds();
//		$('#orderNum').val(order1);
//	}
	$('.enter').click(function(){
//		orderTime();
//		var	merchantName=$('#merchantName').val();
//		var	merchantNo=GetQueryString('merchantNo');
//		var orderNum=$('#orderNum').val();
//		var goodsName=$('#merchantName').val();
//		var amount=$('.amount').text();
//	  	var amounts= amount.substr(amount.indexOf("￥")+1);
//	  	alert(amounts);
//	  	$("#orderIp").val(returnCitySN.cip);
//	  	var orderIp=$("#orderIp").val();
//	  	var base = new Base64();
//	  	var	toibkn=$('#bankCode').val();
//	  	var cardNo=$('#accountNo').val();
//	  	var cardNo = base.encode(cardNo);
//	  	var idCardNo=$('#idCardNo').val();
//	  	var idCardNo = base.encode(idCardNo);
//	  	var payerName=$('#accountName').val();
//	  	var payerName = base.encode(payerName);
//		var	encrypt=GetQueryString('encrypt');
//		var signKey=$('#signKey').val();
//		if((navigator.userAgent).indexOf('MicroMessenger')>'0'){
//			var trxType='WX_SCANCODE_JSAPI';
//			var sign1='#'+trxType+"#"+merchantNo+'#'+orderNum+'#'+amounts+'#'+merchantName+'#'+orderIp+'#'+toibkn+'#'+cardNo+'#'+idCardNo+'#'+payerName+'#'+encrypt+'#'+signKey;
			
//			var sign=($.md5(sign1));
			
//			$.ajax({
//				type:"post",
//				url:"http://trx.ronghuijinfubj.com/middlepaytrx/wx/scanCommonCode?trxType="+trxType+'&merchantNo='+merchantNo+'&orderNum='+orderNum+'&amount='+amount+'&goodsName='+merchantName+'&orderIp='+orderIp+'&toibkn='+toibkn+'&cardNo='+cardNo+'&idCardNo='+idCardNo+'&payerName='+payerName+'&encrypt='+encrypt+'&sign='+sign,
//				success: function(data, textStatus){
//					var objResp = eval('(' + data + ')');
//					if(objResp.respCode=="0000"){
//						alert(objResp.retMsg);
//						var qrcode=objResp.qrCode;
//						window.location.href=qrcode;
//					}else{
//						alert(objResp.retMsg);
//					}
//				},
//				error: function(){
//					alert("请求失败，请稍后重试");
//				}
//			});
//		}else if((navigator.userAgent).indexOf('AlipayClient')>'0'){
//			var trxType='Alipay_SCANCODE_JSAPI';
//			var sign1='#'+trxType+"#"+merchantNo+'#'+orderNum+'#'+amounts+'#'+goodsName+'#'+orderIp+'#'+toibkn+'#'+cardNo+'#'+idCardNo+'#'+payerName+'#'+encrypt+'#'+signKey;
//			var sign=($.md5(sign1));
//			var jsonData2="{\"trxType\":\""+trxType+"\",\"merchantNo\":\""+merchantNo+"\",\"orderNum\":\""+orderNum+"\",\"amount\":\""+amount+"\",\"goodsName\":\""+goodsName+"\",\"orderIp\":\""+orderIp+"\",\"toibkn\":\""+toibkn+"\",\"cardNo\":\""+cardNo+"\",\"idCardNo\":\""+idCardNo+"\",\"payerName\":\""+payerName+"\",\"encrypt\":\""+encrypt+"\",\"sign\":\""+sign+"\"}"
//			$.ajax({
//				type:"post",
//				url:"http://trx.ronghuijinfubj.com/middlepaytrx/alipay/scanCommonCode?trxType="+trxType+'&merchantNo='+merchantNo+'&orderNum='+orderNum+'&amount='+amount+'&goodsName='+goodsName+'&orderIp='+orderIp+'&toibkn='+toibkn+'&cardNo='+cardNo+'&idCardNo='+idCardNo+'&payerName='+payerName+'&encrypt='+encrypt+'&sign='+sign,
//				success: function(data, textStatus){
//					var objResp = eval('(' + data + ')');
//					if(objResp.respCode=="0000"){
//						alert(objResp.respMsg);
//						var qrcode=objResp.qrCode;
//						window.location.href=qrcode;
//					}else{
//						alert(objResp.respMsg);
//					}
//				},
//				error: function(){
//					alert("请求失败，请稍后重试");
//				}
//			});
//		}
	})
})
//function GetQueryString(name) {
// 	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
// 	var r = window.location.search.substr(1).match(reg);
// 	if (r!=null) return unescape(r[2]); return null;
//}