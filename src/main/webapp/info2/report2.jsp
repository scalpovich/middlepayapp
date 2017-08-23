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
			<!--第一部分-->
			<!-- <li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户名称：</span>
				<input type="text" name="accountName" id="accountName" value="" class="required" placeholder="请输入开户名称"/>
			</li> -->
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户账号：</span>
				<input type="text" name="accountNo" id="accountNo" value="" class="required" placeholder="请输入开户账户"/>
			</li>
			<!-- <li class="bottom">
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>结算账户性质：</span>
				<select name="bankType" id="bankType">
					<option value="TOPUBLIC" name="ENTERPRISE">对公</option>
					<option value="TOPRIVATE" name="INSTITUTION">对私</option>
				</select>
			</li> -->
			<!--第二部分-->
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户银行总行：</span>
				<input type="text" name="bankName" id="bankName" value="" class="required" placeholder="请输入开户银行总行"/>
			</li>
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户银行支行：</span>
				<input type="text" name="bankBranch" id="bankBranch" value="" class="required" placeholder="请输入开户银行支行"/>
			</li>
			 <li class="bottom bank">
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>开户行省市：</span>
				<div class="bank_select">
					<select id="loc_province" readonly></select>
					<select id="loc_city" readonly></select>
				</div>
				
			</li> 
			<!--第三部分-->
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>联行号：</span>
				<input type="text" name="bankCode" id="bankCode" value="" class="required" placeholder="请输入联行号"/>
			</li>
			<li>
				<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>结算人信用卡：</span>
				<input type="text" name="creditCardNo" id="creditCardNo" value="" class="required" placeholder="请输入结算人信用卡"/>
			</li>
			<!-- <li class="bottom remarks">
				<span>备注：</span>
				<textarea name="remarks" id="remarks" value="" placeholder="请输入备注信息"></textarea>
			</li> -->
			<input type="button" name="last" id="last" value="上一页"/>
			<input type="button" name="submit" id="submit" value="提交报备" /> 
		</ul>
	</form>
	</body>
</html>