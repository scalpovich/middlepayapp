$(function(){
	$('#lastInfo').click(function(){
		var loginID = localStorage.getItem("loginID");
//		window.history.go(-1);
//		location.href=document.referrer;
		if (typeof document.referrer === '') {
		    // 没有来源页面信息的时候，改成首页URL地址
		    window.location.href="reportInfo1.jsp?loginID="+loginID;
		}else{
			 window.location.href="reportInfo1.jsp?loginID="+loginID;
		}
	})
})


$(function(){
	var loginID = localStorage.getItem("loginID");
	$.ajax({
		type:"post",
		url:"/appserver/getuserbankcard",
		data:{
			loginID:loginID
		},
		success:function(repdata){
			$("#accountName").html(repdata.AccountName);
			$("#accountNo").html(repdata.AccountNo);
			$("#bankType").html(repdata.SettleBankType);
			$("#bankName").html(repdata.BankName);
			$("#bankBranch").html(repdata.BankBranch);
			$("#bankCode").html(repdata.BankCode);
			$("#creditCardNo").html(repdata.SettleCreditCard);
			$("#loc_province").html(repdata.BankProv);
			$("#loc_city").html(repdata.BankCity);
		}
	});
})

