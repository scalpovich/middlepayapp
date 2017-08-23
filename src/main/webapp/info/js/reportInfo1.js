$(function(){
	$('#next').click(function(){
		var loginID = $('#loginID').val();
		localStorage.setItem("loginID",loginID);
		
	
		window.location.href="reportInfo2.jsp";
	})
})

$(function(){
	var loginID = $("#loginID").val();
	$.ajax({
		type:"post",
		url:"/appserver/getmerchantinfo",
		data:{
			loginID:loginID
		},
		success:function(repdata){
			$("#merchantName").html(repdata.merchantName);
			$("#merchantBillName").html(repdata.merchantBillName);
			$("#merchantType").html(repdata.merchantTypeValue);
			$("#merchantPersonName").html(repdata.merchantPersonName);
			$("#merchantPersonPhone").html(repdata.loginID);
			$("#merchantPersonEmail").html(repdata.email);
			$("#businessLicense").html(repdata.businessLicense);
			$("#legalPersonName").html(repdata.name);
			$("#legalPersonID").html(repdata.IDCardNo);
			$("#operateAddress").html(repdata.address);
			$("#loc_province").html(repdata.state);
			$("#loc_city").html(repdata.city);
			$("#loc_town").html(repdata.region);
		}
	});
});


