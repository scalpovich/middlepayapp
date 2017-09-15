/************增加商户页面正则验证 以及验证后操作****************/
$(function () {
    $("#next").on("click", function () {
        var merchantName = $("#merchantName").val(); //商户名称
        var operateAddress = $("#operateAddress").text();//经营地址
        var houseNum = $("#houseNum").val();
        
        localStorage.setItem('merchantName',merchantName);
        localStorage.setItem('operateAddress',operateAddress);
        localStorage.setItem('houseNum',houseNum);
        
        var loginID = $("#loginID").val();
        localStorage.setItem('loginID',loginID);
        
        
        var addCompCity = $("#city").val();
        var province = $("#province").val();
        var district = $("#Region").val();
        
        localStorage.setItem('province' , province);
        localStorage.setItem('ctiy' , addCompCity);
        localStorage.setItem('district' ,district );
        
        /*商户正则*/
        var merchantNameReg = /^[\u4E00-\u9FA5]{5,12}$/;
        /*用户名地址栏正则匹配后提交*/
        if(merchantNameReg.test(merchantName)&&operateAddress){
            $("#next").addClass("active");
                window.location.href="pay.jsp";
                /*$.ajax({
                 async:true,
                 type: "POST",
                 url: url,
                 data:{"merchantName":merchantName},
                 dataType:"JSON",
                 success:function (data) {
                 window.location.href="pay.html";
                 console.log(JSON.stringify(data));
                 }
                 });*/
        }else {
            if (!merchantName) {
                alert("商户名称是必填");
            }else if (merchantName && !merchantNameReg.test(merchantName)) {
                $("#merchantName").val();
                alert("您输入的商户名称不符合格式");
            }
            if(!operateAddress){
                alert("经营地址为空,请选择");
            }
            $("#next").removeClass("active");
            $("#next").off("click.submit");
        }
    });

});










/*增加信用卡页面正则  finish*/
$(function () {
    $("#bankNum").on("blur",function(){
        var cardNum = $("#bankNum").val();
        if(cardNum){
            $("#addInfoBtn").css("background-color","#EBC35B");
        }

    });


    $("#addInfoBtn").on("click",function () {
        var cardNum = $("#bankNum").val();
        
        var loginID = localStorage.getItem('loginID');
        
        var reg = /^(\d{16}|\d{19})$/;
        console.log(reg.test(cardNum));
        if(cardNum && reg.test(cardNum)){
        	/*ajax 这里演示用 成功发送后再提交  苹端的链接*/
            $.ajax({
                async:true,
                type: "POST",
                url: "/appserver/bankbranch/addCreditCard",
                data:{
                	creditCard:cardNum,
                	loginID:loginID
                },
                dataType:"JSON",
                success:function (data) {
                    alert(data.msg);
                    console.log(JSON.stringify(data));
                }
            });
            console.log("通过")
        }else if(cardNum && !reg.test(cardNum)){
            //$("#bankNum").val("");
            //$("#bankNum").attr("placeholder","格式不正确");
            alert("格式不正确")
        }else if(!cardNum){
            //$("#bankNum").attr("placeholder","请输入信用卡号");
            alert("请输入信用卡号");
        }
    });
});

