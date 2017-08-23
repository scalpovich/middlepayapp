
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0 minimal-ui" />
<meta name="format-detection" content="telephone=no" />
<title>支付成功</title>
<link href="/appserver/ymf/css/information.css" rel="stylesheet" type="text/css" />
<script type="application/javascript">
	function closeWindow(){
		WeixinJSBridge.invoke('closeWindow',{},function(res){
			
		});
	}
</script>
</head>
<body>
	<%
		String merName = (String)request.getAttribute("merName");
		String amount = (String)request.getAttribute("amount");
		String date = (String)request.getAttribute("date");
		String sendSeqId = (String)request.getAttribute("sendSeqId");
	%>

	<div class="header"><%=merName %></div>
	<ul class="state">
		<li>
			<span>支付金额：</span>
			<span><font>￥ <%=amount %></font></span>
		</li>
		<li>
			<span>商户名称：</span>
			<span><%=merName %></span>
		</li>
		<li>
			<span>支付状态：</span>
			<span>支付成功</span>
		</li>
		<li>
			<span>交易时间：</span>
			<span><%=date %></span>
		</li>
		<li>
			<span>交易流水：</span>
			<span><%=sendSeqId %></span>
		</li>
	</ul>
	<button class="confirmbtn" onclick="closeWindow()">完成</button>

</body>
</html>
