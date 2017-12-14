		$('#gou img').attr("src","images/gou.png");
		$('#gou').click(function(){
			if($("#gou img").attr("src")==""){
				$('#submit').css('background', '#eb2835');
				$('#gou img').attr("src","images/gou.png");
				$("#submit").removeAttr("disabled");
			}else if($('#gou img').attr("src","images/gou.png")){
				$('#submit').css('background', '#999999');
				$('#gou img').attr('src',"");
				$("#submit").attr({"disabled":"disabled"});
			}
		})



		// 正则判断手机号
		function checkPhone(){ 
		    var phone = document.getElementById('phone').value;
		    if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){
		        alert("手机号码有误，请重填");
		        return false; 
		    } 
		}
		//input框只能输入数字
		function onlyNum(){
		    if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39))
		    if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)))
		    event.returnValue=false;  //执行至该语句时，阻止输入；可类比阻止冒泡原理或者禁止右键功能；
		}
		//失去焦点
		$('#phone').blur(function(){
		  checkPhone();
		});
		$('#btn').click(function() {
			// ajax();
			alert('注册成功');
		});
		//判定密码框只能输入6-18位数
		
	// function ajax(){
	// 	$.ajax({
 //            type: "get",//数据发送的方式（post 或者 get）
 //            url: "/admin/index",//要发送的后台地址
 //            data: {sendTime:new Date(,txndir:"2",sendSeqId:"1",terminalInfo:"2",loginID:"1",loginPwd:"2"},//要发送的数据（参数）格式为{'val1':"1","val2":"2"}
 //            dataType: "json",//后台处理后返回的数据格式
 //            success: function (data) {//ajax请求成功后触发的方法
 //               alert('请求成功');
 //            },
 //            error: function (msg) {//ajax请求失败后触发的方法
 //                alert(msg);//弹出错误信息
 //            }
 //        });

	// }
	