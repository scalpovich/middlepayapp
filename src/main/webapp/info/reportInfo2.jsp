<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html style="font-size: 40px;">
	<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		 	<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		    <title>商户报备</title>
		    		 			
			<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
			<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
			<META HTTP-EQUIV="Expires" CONTENT="0"> 
			
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
		    <link rel="stylesheet" type="text/css" href="./css/reportInfo.css"/>
	</head>
	<body>
		<form class="form">
		<ul class="second">
			<!--第一部分-->
			<li>
				<span class="four_size span1"><font style="color: #ff0000;">*</font>开户名称：</span>
				<span id="accountName" class="required four_size_input span2" ></span>
			</li>
			<li>
				<span class="four_size span1"><font style="color: #ff0000;">*</font>开户账号：</span>
				<span id="accountNo"  class="required four_size_input span2"></span>
			</li>
			<li class="bottom">
				<span class="six_size span1"><font style="color: #ff0000;">*</font>结算账户性质：</span>
				<span id="bankType" class="six_size_input span2"></span>
			</li>
			<!--第二部分-->
			<li>
				<span class="six_size span1"><font style="color: #ff0000;">*</font>开户银行总行：</span>
				<span id="bankName" class="required six_size_input span2"></span>
			</li>
			<li>
				<span class="six_size span1"><font style="color: #ff0000;">*</font>开户银行支行：</span>
				<span   id="bankBranch"  class="required six_size_input span2" ></span>
			</li>
			<li class="bottom bank">
				<span class='fifth_size span1'><font style="color: #ff0000;">*</font>开户行省市：</span>
				<div class="bank_select span2">
					<span id="loc_province" style='margin-left: 1rem;'></span>
					<span id="loc_city"></span>
				</div>
				
			</li>
			<!--第三部分-->
			<li>
				<span class="third_size span1"><font style="color: #ff0000;">*</font>联行号：</span>
				<span id="bankCode" class="required third_size_input span2" ></span>
			</li>
			<li>
				<span class="six_size span1"><font style="color: #ff0000;"></font>结算人信用卡：</span>
				<span  id="creditCardNo"  class="required six_size_input span2"></span>
			</li>
		</ul>
	</form>
	<input type="button" id="lastInfo" value="上一页" name=""/>
	</body>
</html>