<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html style="font-size: 40px;">
	<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		 	<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		 			
			<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
			<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
			<META HTTP-EQUIV="Expires" CONTENT="0"> 
		
		    <title>商户报备</title>
		    <script src="js/jquery-1.11.0.min.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/common.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/tripledes.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/cipher-core.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/core.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/mode-ecb.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/md5.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/jquery.cityselect.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/area.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/location.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/select2.js" type="text/javascript" charset="utf-8"></script>
		    <script src="js/report2.js" type="text/javascript" charset="utf-8"></script>
		   	<link rel="stylesheet" type="text/css" href="css/select2.css"/>
		    <link rel="stylesheet" type="text/css" href="css/report.css"/>
	</head>
	<body>
		<form class="form">
		<ul class="second">
			
			<li>
				<span class="four_size"><font style="color: #ff0000;">*</font>结算卡号：</span>
				<input type="text" name="accountNo" id="accountNo" class="required four_size_input" placeholder="请填写借记卡(储蓄卡)信息"/>
			</li>
			
			<!--第二部分-->
			<!-- <li>
				<span class="six_size"><font style="color: #ff0000;">*</font>开户银行总行：</span>
				<input type="text" name="bankName" id="bankName" value="" class="required six_size_input" placeholder="请输入开户银行总行"/>
			</li> -->
			<li>
				<span class="" style="width: 8rem;"><font style="color: #ff0000;">*</font>开户银行支行/分行：</span>
				<input type="text" name="bankBranch" id="bankBranch" class="required" placeholder="请输入开户银行支行/分行"/>
			</li>
			 <li class="bottom bank">
				<span><font style="color: #ff0000;">*</font>开户行省市：</span>
				<div class="bank_select">
					<select id="loc_province" readonly></select>
					<select id="loc_city" readonly></select>
				</div>
				
			</li> 
			<!--第三部分-->
			<!-- <li>
				<span class="third_size"><font style="color: #ff0000;"></font>联行号：</span>
				<input type="text" name="bankCode" id="bankCode" value="" class="third_size_input" placeholder="请输入联行号"/>
			</li> -->
			<li>
				<span class="six_size"><font style="color: #ff0000;"></font>结算人信用卡：</span>
				<input type="text" name="creditCardNo" id="creditCardNo"  class="six_size_input" placeholder="请输入结算人信用卡"/>
			</li>
			
			<input type="button" name="last" id="last" value="上一页"/>
			<input type="button" name="submit" id="submit" value="提交报备" /> 
		</ul>
	</form>
	</body>
</html>