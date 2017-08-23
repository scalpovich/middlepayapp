<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
	<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		 	<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		    <title>商户报备</title>
		    <script src="js/jquery-1.11.0.min.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/common.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/tripledes.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/cipher-core.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/core.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/mode-ecb.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/md5.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/jquery.cityselect.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/location.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/reportInfo2.js" type="text/javascript" charset="utf-8"></script>
		   	<link rel="stylesheet" type="text/css" href="css/select2.css"/>
		    <link rel="stylesheet" type="text/css" href="css/report.css"/>
	</head>
	<body>
		<form class="form">
		<ul class="second">
			<!--第一部分-->
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户名称：</span>
				<span id="accountName" class="required" ></span>
			</li>
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户账号：</span>
				<span id="accountNo"  class="required"></span>
			</li>
			<li class="bottom">
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>结算账户性质：</span>
				<span id="bankType"></span>
			</li>
			<!--第二部分-->
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户银行总行：</span>
				<span id="bankName" class="required"></span>
			</li>
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户银行支行：</span>
				<span   id="bankBranch"  class="required" ></span>
			</li>
			<li class="bottom bank">
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户行省市：</span>
				<div class="bank_select">
					<span id="loc_province"></span>
					<span id="loc_city"></span>
				</div>
				
			</li>
			<!--第三部分-->
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>联行号：</span>
				<span id="bankCode" class="required" ></span>
			</li>
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>结算人信用卡：</span>
				<span  id="creditCardNo"  class="required"></span>
			</li>
			<li class="bottom remarks">
				<span>备注：</span>
				<textarea name="remarks" id="remarks" value="" placeholder="请输入备注信息"></textarea>
			</li>
			<input type="button" name="last" id="last" value="上一页"/>
		</ul>
	</form>
	</body>
</html>