    
 var infoStr = "";// 2017.08.31 
 
 
 var arr = [] ;
$(function(){
	var holderName = window.sessionStorage.getItem('holderName' );// 2017.08.31
    var idCard = window.sessionStorage.getItem('idCard');// 2017.08.31
    var bankNum = window.sessionStorage.getItem('bankNum');// 2017.08.31
    var bankAdd = window.sessionStorage.getItem('bankAdd');// 2017.08.31
    
    console.log(holderName);
    console.log(idCard);
    console.log(bankNum);
    console.log(bankAdd);
    
    
	
    $.ajax({
		type:"post",
		url:"/appserver/bankbranch",
		cache: false,
		data:{
			bankNum:bankNum ,
			bankAdd:bankAdd
		},
		success:function(repdata){
			if(repdata.code=="00"){
				 console.log(repdata.list);
				 arr = repdata.list;
				 for(var i=0 ; i < repdata.list.length ; i++){
					 console.log(repdata.list[i].BankBranch);
					 infoStr += "<li>" + repdata.list[i].BankBranch + "</li>";
				 }
				 $("#ul").html(infoStr); 
			}else{
				alert(repdata.msg);
			}
		},
	});
    
    
    
    
});

//for (var i = 0; i < arr.length; i++) {// 2017.08.31
//    infoStr += "<li>" + arr[i] + "</li>";
//}
// 2017.08.31

function search(keyWord) {
    var reg = new RegExp(keyWord);
    for (var i = 0, resultArr = []; i < arr.length; i++) {
        var index = arr[i].BankBranch.match(reg);
        if (index != null) {
            resultArr.push(arr[i].BankBranch)
        }
    }
    return resultArr;
}
//    console.log(search("招商"))


/*电脑端keyup 可用手机端兼容有问题 选择的propertychange*/
$("#searchInput").on('input propertychange', function () {
    var keyWord = $(this).val();
    var searchArr = search(keyWord);
    var str;
    for (var i = 0, str = ""; i < searchArr.length; i++) {
        str += "<li>" + searchArr[i] + "</li>";
    }
    $("ul").html(str);
});


/*
 * 搜索结果点击回显事件touchstart
 * */
/*$("#ul").on("click", "li", function () {
 $("#searchInput").val($(this).text());
 var ulKey="";
 var ulVal=$("#searchInput").val($(this).text());
 window.sessionStorage.setItem('ulKey',ulVal);
 // 跳回提交页面
 //var ulVal=$("#searchInput").val($(this).text());
 //            console.log(ulVal);
 window.history.back(-1);
 });*/

$("#ul").on("touchstart", "li", function () {
    $("#searchInput").val($(this).text());
    // 跳回提交页面
    var ulVal = $("#searchInput").val();
    var ulKey = "";
    window.sessionStorage.setItem('ulKey', ulVal);
    //window.history.back(-1);
    window.location.href = "pay.jsp?search=true";// 2017.08.31
});



/*搜索栏取消按钮的样式*/
/*input获得焦点后样式改变*/
$(".inputWrapper").on("click",function(){
    inputChange();
});
function inputChange(){
    $("#cancel").css({"display":"block"});
    $(".inputWrapper").css({"width":"88%"});
    $(".icon-shousuo").css("margin-left","1rem");

}
$("#cancel").on("click",function(){
    $("#label2").css({"transform":"translate(0rem)"});
    $(this).css("display","none");
    $(".inputWrapper").css({"width":"100%"});
    $(".icon-shousuo").css("margin-left","5rem");
    $('#searchInput').val('');
});





/*向后台发送搜索条件 待定 后台要求都在前端实现 发送数据待定*/
function senData(data) {

}



