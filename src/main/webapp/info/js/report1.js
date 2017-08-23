$(function(){
	var installProvince=sessionStorage.getItem('installProvince');
	var installCity=sessionStorage.getItem('installCity');
	var installCounty=sessionStorage.getItem('installCounty');
	var provid = sessionStorage.getItem("provid");
	$('#loc_province').select2('data').text=installProvince;
	$('#loc_city').select2('data').text=installCity;
	$('#loc_town').select2('data').text=installCounty;
	$("#loc_province  option:eq("+provid+")").attr('selected','selected');
	if(installProvince!=null){
		$('#select2-chosen-1').text(""+installProvince+"");
	}
	if(installCity!=null){
		$('#select2-chosen-2').text(""+installCity+"");
	}
	if(installCounty!=null){
		$('#select2-chosen-3').text(""+installCounty+"");
	}
	
	
	
	
	$("#submit").click(function(){
		var arr=new Array('开户名称','开户账号','开户银行总行','开户银行支行','联行号','结算人信用卡');
		for (var i=0;i<arr.length;i++) {
			if($('.required').eq(i).val()==''){
				alert(arr[i]+'为空，请返回填写');
				return false;
			}
		}
		if($('#loc_province').val()==""){
			alert('请选择开户行省份');
			return false;
		}
		if($('#loc_city').val()==""){
			alert('请选择开户行城市');
			return false;
		}
		var merchantName=$('#merchantName').val();
		var merchantType=$('#merchantType').val();
		var merchantBillName=$('#merchantBillName').val();
		var merchantPersonName=$('#merchantPersonName').val();
		var merchantPersonPhone=$('#merchantPersonPhone').val();
		var merchantPersonEmail=$('#merchantPersonEmail').val();
		var businessLicense=$('#businessLicense').val();
		var legalPersonName=$('#legalPersonName').val();
		var legalPersonID=$('#legalPersonID').val();
		var installProvince=$('#loc_province').select2('data').text;
		var installCity=$('#loc_city').select2('data').text;
		var installCounty=$('#loc_town').select2('data').text;
		var provid=$('#loc_province').val();
		var operateAddress=$('#operateAddress').val();
		var loginID = $('#loginID').val();
		var accountName=$('#accountName').val();
		var accountNo=$('#accountNo').val();
		var bankName=$('#bankName').val();
		var bankBranch=$('#bankBranch').val();
		var bankCode=$('#bankCode').val();
		var creditCardNo=$('#creditCardNo').val();
		var bankProv=$('#loc_province').select2('data').text;
		var bankCity=$('#loc_city').select2('data').text;
		var remarks=$('#remarks').val();
		var bankType=$('#bankType').val();
		

		
		var jsondata="{'accountName':'"+accountName+"','accountNo':'"+accountNo+"','bankBranch'" + ":'"+bankBranch+"','bankCode':'"+bankCode+"','bankName':'"+bankName+"','bankType':'"
				+ bankType+"','creditCardNo':'"+creditCardNo+"','installCity':'"+installCity+"','installCounty':'"+installCounty+"','installProvince':'"+installProvince+"','legalPersonID':'"
				+ legalPersonID+"','merchantName':'"+merchantName+"','merchantPersonEmail':'"+merchantPersonEmail+"','merchantPersonName':'"
				+ merchantPersonName+"','merchantType':'"+merchantType+"','operateAddress':'"+operateAddress+"','remarks':'"+remarks+"' , 'loginID': '"+ loginID +"'}";
		$.ajax({
			type:"post",
			url:"/appserver/in",
			dataType:"json",
			cache: false,
			data:{
				data:jsondata
			},
			success:function(repdata){
				if(repdata.respCode=="00"){
					alert('提交成功');
					location.href="http://www.baidu.com";
					JSInterface.changeActivity();
				}else{
					alert(repdata.respMsg);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("服务异常!");
			}
		});
	})
	
	
	$('#next').click(function(){
		var arr=new Array('商户名称','签购单显示名称','商户联系人姓名','商户联系人电话','商户联系人邮箱','营业执照号码','法人代表姓名','法人身份证号','经营地址');
		for (var i=0;i<arr.length;i++) {
			if($('.required').eq(i).val()==''){
				alert(arr[i]+'为空，请返回填写');
				return;
			}
		}
		/*var reg=/^1[3|4|5|7|8]\d{9}$/;
		if(reg.test($('#merchantPersonPhone').val())==false){
			alert("请输入格式正确的手机号");
			return;
		}*/
		var reg=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ ;
		if(reg.test($('#merchantPersonEmail').val())==false){
			alert("请输入格式正确的邮箱号");
			return;
		}
		if($('#loc_province').val()==''){
			alert('安装归属省不能为空，请选择');
			return;
		}
		if($('#loc_city').val()==''){
			alert('安装归属市不能为空，请选择');
			return;
		}
		if($('#loc_town').val()==''){
			alert('安装归属县（区）不能为空，请选择');
			return;
		}
		var merchantName=$('#merchantName').val();
//		var merchantType=$('#merchantType').val();
//		var merchantBillName=$('#merchantBillName').val();
		var merchantPersonName=$('#merchantPersonName').val();
//		var merchantPersonPhone=$('#merchantPersonPhone').val();
		var merchantPersonEmail=$('#merchantPersonEmail').val();
//		var businessLicense=$('#businessLicense').val();
//		var legalPersonName=$('#legalPersonName').val();
		var legalPersonID=$('#legalPersonID').val();
		var installProvince=$('#loc_province').select2('data').text;
		var installCity=$('#loc_city').select2('data').text;
		var installCounty=$('#loc_town').select2('data').text;
		var provid=$('#loc_province').val();
		var operateAddress=$('#operateAddress').val();
		var loginID = $('#loginID').val();
		localStorage.setItem('merchantName',merchantName);
//		localStorage.setItem('merchantType',merchantType);
//		localStorage.setItem('merchantBillName',merchantBillName);
		localStorage.setItem('merchantPersonName',merchantPersonName);
//		localStorage.setItem('merchantPersonPhone',merchantPersonPhone);
		localStorage.setItem('merchantPersonEmail',merchantPersonEmail);
//		localStorage.setItem('businessLicense',businessLicense);
//		localStorage.setItem('legalPersonName',legalPersonName);
		localStorage.setItem('legalPersonID',legalPersonID);
		localStorage.setItem('installProvince',installProvince);
		localStorage.setItem('installCity',installCity);
		localStorage.setItem('installCounty',installCounty);
		localStorage.setItem('operateAddress',operateAddress);
		localStorage.setItem("provid",provid);
		localStorage.setItem("loginID",loginID);
		window.location.href="report2.jsp";
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
			$("#merchantName").val(repdata.merchantName);
			$("#merchantBillName").val(repdata.merchantBillName);
			$("#merchantType").val(repdata.merchantTypeValue);
			$("#merchantPersonName").val(repdata.merchantPersonName);
			$("#merchantPersonPhone").val(repdata.loginID);
			$("#merchantPersonEmail").val(repdata.email);
			$("#businessLicense").val(repdata.businessLicense);
			$("#legalPersonName").val(repdata.name);
			$("#legalPersonID").val(repdata.IDCardNo);
			$("#operateAddress").val(repdata.address);
		}
	});
});


