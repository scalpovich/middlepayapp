$(function(){
	$('.next').click(function(){
		var loginID = $('#loginID').val();
		localStorage.setItem("loginID",loginID);
		window.location.href="reportInfo2.jsp";
	})
})

$(function(){
	var loginID = $("#loginID").val();
	$.ajax({
		type:"post",
		url:"/appserver/getmerchantinfo",/*��̨���ĵ�ַ*/
		data:{
			/*form name*/
			loginID:loginID
		},
		success:function(repdata){
		/*��̨�÷��صĹ���*/
		}
	});
});


