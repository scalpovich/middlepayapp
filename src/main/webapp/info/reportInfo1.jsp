<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html style="font-size: 40px;">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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
		<script src="js/location.js" type="text/javascript" charset="utf-8"></script>
		<script src="js/reportInfo1.js" type="text/javascript" charset="utf-8"></script>
		
		
		
		
		<link rel="stylesheet" type="text/css" href="css/select2.css"/>
		<link rel="stylesheet" type="text/css" href="css/reportInfo.css"/>
		<%
			String loginID = request.getParameter("loginID");
			request.setAttribute("loginID",loginID );
	 	%>
	</head>
	<body>
		<form class="form" style="padding-bottom:2.5rem;">
			<ul class="first">
				<!--第一部分-->
				<li>
					<span class="four_size span1"><font style="color: #ff0000;">*</font>商户名称：</span>
					<span id="merchantName" class="required four_size_input span2" ></span>
				</li>
				<li>
					<span class="four_size span1"><font style="color: #ff0000;">*</font>商户类型：</span>
					<span  id="merchantType"  class="required four_size_input span2"></span>
				</li>
				<li class="bottom">
					<span class="seven_size span1"><font style="color: #ff0000;">*</font>签购单显示名称：</span>
					<span id="merchantBillName" class="required seven_size_input span2" ></span>
				</li>
				<!--第二部分-->
				<li>
					<span class="seven_size span1"><font style="color: #ff0000;">*</font>商户联系人姓名：</span>
					<span id="merchantPersonName"  class="required seven_size_input span2" ></span>
				</li>
				<li>
					<span class="seven_size span1"><font style="color: #ff0000;">*</font>商户联系人电话：</span>
					<span id="merchantPersonPhone"  class="required seven_size_input span2"></span>
				</li>
				<li class="bottom">
					<span class="seven_size span1"><font style="color: #ff0000;">*</font>商户联系人邮箱：</span>
					<span id="merchantPersonEmail" class="required seven_size_input span2"></span>
				</li>
				<!--第三部分-->
				<li>
					<span class="six_size span1"><font style="color: #ff0000;">*</font>营业执照号码：</span>
					<span id="businessLicense" class="required six_size_input span2"></span>
				</li>
				<li>
					<span class="six_size span1"><font style="color: #ff0000;">*</font>法人代表姓名：</span>
					<span id="legalPersonName" class="required six_size_input span2"></span>
				</li>
				<li class="bottom">
					<span class="six_size span1"><font style="color: #ff0000;">*</font>法人身份证号：</span>
					<span id="legalPersonID" class="required six_size_input span2" ></span>
				</li>
				<!--第四部分-->
				<li class="install">
					<span class="four_size span1"><font style="color: #ff0000;">*</font>安装归属：</span>
					<div class="install_select span2">
						<span id="loc_province" style="margin-left: 1rem;"></span>
						<span id="loc_city"></span>
		    			<span id="loc_town"></span>
					</div>
				</li>
				<li>
					<span class="six_size span1"><font style="color: #ff0000;">*</font>经营地址：</span>
					<span  id="operateAddress" class="required six_size_input span2" ></span>
				</li>
			</ul>
			<input type="button" id="next" value="下一页" name="next"/>
			<input type="hidden" id="loginID" value="${loginID}" name="loginID"/>
		</form>
	</body>
</html>