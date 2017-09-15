/**
 * Created by a on 2017/8/29.
 */

$(function () { // 2017.08.31
  //var status = window.location.href.split("l?search=")[1];
  //var status="";
  //if (status == "true") {
    var _holderName = window.sessionStorage.getItem('holderName');// 2017.08.31
    var _idCard = window.sessionStorage.getItem('idCard');// 2017.08.31
    var _bankNum = window.sessionStorage.getItem('bankNum');// 2017.08.31
    var _bankAdd = window.sessionStorage.getItem('bankAdd');// 2017.08.31
    var _ulKey = window.sessionStorage.getItem('ulKey');// 2017.08.31
    //console.log(_holderName,_idCard,_bankNum,_bankAdd,_ulKey)
    $("#holderName").val(_holderName);// 2017.08.31
    $("#idCard").val(_idCard);// 2017.08.31
    $("#bankNum").val(_bankNum);// 2017.08.31
    $("#bankAdd").val(_bankAdd);// 2017.08.31
    $("#branchBank").val(_ulKey);// 2017.08.31
  //}
});// 2017.08.31

$(document).ready(function () {
  var regs = {
    holderName: {
      reg: /^[\u4E00-\u9FA5]{1,8}$/,
      empty: "姓名是必填项",
      error: "姓名不符合格式"
    },
    idCard: {
      reg: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
      empty: "身份证是必填项",
      error: "身份证不符合格式"
    },
    bankNum: {
      reg: /^\d{16,19}$/,
      empty: "银行卡是必填项",
      error: "银行卡不符合格式"
    }
  };

  var promptBox = ""; //创建提示框
  $("#postInfo").on("click", function (e) {
   var holderName = $("#holderName").val().trim("");
    var idCard = $("#idCard").val().trim("");
    var bankNum = $("#bankNum").val().trim("");
    var bankAdd = $("#bankAdd").val();
    var chooseBank=$("#chooseBank").val();
    var holderNameStatus = test("holderName", holderName, regs);
    var idCardStatus = test("idCard", idCard, regs);
    var bankNumStatus = test("bankNum", bankNum, regs);
    console.log(holderNameStatus);
    console.log(idCardStatus);
    console.log(idCardStatus);
    
    
    var holderName = $("#holderName").val();
    var idCard = $("#idCard").val();
    var bankNum = $("#bankNum").val();
    var bankAdd = $("#bankAdd").val();
    var branchBank = $("#branchBank").val();
    
    var merchantName = localStorage.getItem('merchantName');
    var operateAddress = localStorage.getItem('operateAddress');
    var houseNum = localStorage.getItem('houseNum');
    
    var loginID = localStorage.getItem('loginID');
  
    
    var province = localStorage.getItem('province');
    var ctiy = localStorage.getItem('ctiy');
    var district = localStorage.getItem('district');
    
    console.log(province);
    console.log(ctiy);
    console.log(district);
    
    
    //正则验证成功后弹出框
    if (holderNameStatus && idCardStatus && bankNumStatus && bankAdd) {
        console.log("success");
        $("#remindBox").show();
        $(".zhezhao").show(); //2017.08.31
        $(".mPicker-mask").css({
          "display": "block"
        });

        /*冒泡兼容苹果*/
        $("body").children().click(function () {});
        e.stopPropagation();//阻止冒泡
        $(document).click(function(){
          $('#remindBox').hide();
            $(".zhezhao").hide();

        });
        
        var bankName = "";
        var creditCardNo = "";
        
        
        var jsondata="{'accountNo':'"+bankNum+"','bankBranch'" + ":'"+branchBank+"','bankCity':'"+bankAdd.split("-")[0]+"','bankName':'"+bankName+"','bankProv':'"+bankAdd.split("-")[1]+"',"
		+"'creditCardNo':'"+creditCardNo+"','installCity':'"+ctiy+"','installCounty':'"+district+"','installProvince':'"+province+"','legalPersonID':'"
		+ idCard+"','merchantName':'"+merchantName+"','merchantPersonName':'"
		+ holderName+"','operateAddress':'"+operateAddress + houseNum+ "','loginID': '"+ loginID +"'}";
		
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
		});
        
        
        //提交表单
      //  var param = $("form#payInfo").serialize();
        /* $.ajax({
         async: true,
         type: "POST",
         url: "dddddddd.form",
         data: param,
         dataType: "JSON",
         success: function (data) {
         console.log(JSON.stringify(data, null, 2));
         }
         });*/

    } 
  });

  $("#jump","#provide").on("click", function () { // 跳过
    $("#remindBox").hide();
    $(".zhezhao").hide(); //2017.08.31
    promptBox = true;
  });

  /*提交表单*/
  // $("#payInfo").click(function () {
  //   $("#payInfo").submit();
  // })
});
// 正则验证
function test(name, val, regs) {
  var reg = regs[name].reg;
  var emptyStr = regs[name].empty;
  var errorStr = regs[name].error;
  if (!val) {
    //$("input[name=" + name + "]").attr("placeholder", emptyStr);
     alert(emptyStr);
  } else if (val && !reg.test(val)) {
    //$("input[name=" + name + "]").val();
      alert(errorStr);
  } else if (reg.test(val)) {
    return true;
  }
}


$(function () {
  var kaihuhang = window.sessionStorage.getItem('ulKey');
  $("#branchBank").val(kaihuhang || '请选择');
  $("#branchBank").on("click", function () {
   var holderName = $("#holderName").val();
    var idCard = $("#idCard").val();
    var bankNum = $("#bankNum").val();
    var bankAdd = $("#bankAdd").val();
    window.sessionStorage.setItem('holderName', holderName);// 2017.08.31
    window.sessionStorage.setItem('idCard', idCard);// 2017.08.31
    window.sessionStorage.setItem('bankNum', bankNum);// 2017.08.31
    window.sessionStorage.setItem('bankAdd', bankAdd);// 2017.08.31
      /*需要判断银行卡和开户行的填写才能选择加载支行*/
      if (bankNum&& bankAdd) {
          console.log(" input有内容匹配");
          //$("#postInfo").css("background-color", "#E9C259");
          window.location.href = 'searchbank.jsp';
      }else{
          alert("请先填写正确的银行卡和开户行");
      }
  });
});




/*需求  页面加载按钮颜色问题*/
$(function () {
    function btnColor() {
        var holderName = $("#holderName").val().trim("");
        var idCard = $("#idCard").val().trim("");
        var bankNum = $("#bankNum").val().trim("");
        var bankAdd = $("#bankAdd").val();
        var chooseBank=$("#chooseBank").val();
        //正则验证成功后弹出框
        var ulKey = window.sessionStorage.getItem('ulKey');
        if (!(ulKey&&holderName&&idCard)) {
            $("#postInfo").css("background-color","#dcdcdc");
        }else{
            $("#postInfo").css("background-color","#E9C259");

        }
    }
    btnColor();
});

