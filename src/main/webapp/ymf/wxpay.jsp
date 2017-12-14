<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0 minimal-ui" />
<meta name="format-detection" content="telephone=no" />
<title>支付</title>
<script src="/appserver/ymf/script/jquery.min.js" type="text/javascript"></script>
<script src="/appserver/ymf/script/fastclick.js" type="text/javascript"></script>
<script src="/appserver/ymf/script/index.js" type="text/javascript"></script>
<link href="/appserver/ymf/css/checkstand.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/appserver/ymf/css/pay.css">

<script type="application/javascript">
	
	var num = 0;
	var dec = 0;
	var pnt = false;
	var stp = 0;
	var isLock = false;
	
	function RequestModel(userId,merName,amount){
		this.userId = userId;
		this.merName = merName;
		this.amount = amount;
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
	
	function getPayLink(){
		if(isLock){
			return;
		}
		
		var tmp = $(".amount").text().split(' ');
		var amt = tmp[1];
		
		if(Number(amt) == 0){
			$(".message").text('金额不能为零');
			num = 0;
			dec = 0;
			stp = 0;
			pnt = false;
			return;
		}
		
		var tmp = amt.split('.');
		
		amt = tmp[0] + tmp[1];
		
		amt = parseInt(amt);
		
		isLock = true;
		
		var merName = '<%=request.getAttribute("merName")%>';
		var userId = '<%=request.getAttribute("userID")%>';
		var ymfCode = '<%=request.getAttribute("ymfCode")%>';
		var paychannel = '<%=request.getAttribute("payChannel")%>';
		merName = encodeURI(encodeURI(merName));
		
		//var model = new RequestModel(userId,merName,amt);
		//var data = JSON.stringify(model);
		
		
		var url = '/appserver/YMFRequest?userID='+userId + '&merName='+merName + "&amount="+amt + "&ymfCode="+ymfCode + "&paychannel="+paychannel;
		window.location.assign(url);
	}
	
	function clearActive(){
		$(".active").removeClass('active');
		$(".amount").removeClass('deactive');
	}
	
	$(function(){
		new FastClick(document.body);
		
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
					getPayLink();
					break;
				case '清除':
					num = 0;
					dec = 0;
					stp = 0;
					pnt = false;
					break;
				case '.':
					pnt=true;
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
						if(num.toString().length < 6){
							num = num * 10 + Number(key);
						}else{
							$(".message").text('');
						}
					}
					break;
				}
				formatAmount();
        	
			
		})
	})
	
	

</script>
</head>
<body>

	<%
		String userId = (String)request.getAttribute("userId");
		String merName = (String)request.getAttribute("merName");
	%>

<!-- 	<div class="header"><%=merName %></div>
	<div class="amount"><span>￥</span> 0.00</div>
	<div class="tip">请向商家询问价格后输入,勿向陌生人汇款</div>
	<button class="enter" value="确认" onclick="getPayLink()">确认</button> -->
	<!-- 
	<div id="dialog" style="display: none">
		<table style=" width:100% ;height:100% ;border:0 ; align:center; valign:middle">
			<tr height=50%>
				<td align=center>&nbsp;</td>
			</tr>
			<tr>
				<td align=center>
				<img
					src="loading.gif" 
					width="40"
					height="40"
					/></td>
			</tr>
			<tr>
				<td align=center>加载中......</td>
			</tr>
			<tr height=50%>
				<td align=center>&nbsp;</td>
			</tr>
		</table>
	</div> -->
	

		<div class="warp">
			<div class="centent">
				<div class="cententTop">
					<div>
						<img src="/appserver/ymf/images/1.png" alt="">
						<p><%=merName %></p>
					</div>
				</div>
				<div class="cententMain">
					<span class="span">金额</span>
					<div class="amount"><span>￥</span> 0.00</div>
				</div>
				<div class="cententBottom">
					<button class="enter" value="确认" onclick="getPayLink()">确认</button>
					<p>请向商家询问价格后支付</p>
				</div>
			</div>
		</div>
	<div class="keypadWarp"></div>
	<div class="keypad">
		<div class="message"></div>
		<div class="col">
			<div class="key digit">1</div>
			<div class="key digit">4</div>
			<div class="key digit">7</div>
			<div class="key point">.</div>
		</div>
		<div class="col">
			<div class="key digit">2</div>
			<div class="key digit">5</div>
			<div class="key digit">8</div>
			<div class="key digit">0</div>
		</div>
		<div class="col">
			<div class="key digit">3</div>
			<div class="key digit">6</div>
			<div class="key digit">9</div>
			<div class="key empty" id="remove">清除</div>
		</div>
	</div>
</body>
</html>
