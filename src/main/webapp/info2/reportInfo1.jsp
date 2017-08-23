<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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
		<script src="js/reportInfo1.js" type="text/javascript" charset="utf-8"></script>
		
		
		
		
		<link rel="stylesheet" type="text/css" href="css/select2.css"/>
		<link rel="stylesheet" type="text/css" href="css/report.css"/>
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
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户名称：</span>
					<span id="merchantName" class="required" ></span>
				</li>
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户类型：</span>
					<span  id="merchantType"  class="required"></span>
				</li>
				<li class="bottom">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>签购单显示名称：</span>
					<span id="merchantBillName" class="required" ></span>
				</li>
				<!--第二部分-->
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户联系人姓名：</span>
					<span id="merchantPersonName"  class="required" ></span>
				</li>
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户联系人电话：</span>
					<span id="merchantPersonPhone"  class="required"></span>
				</li>
				<li class="bottom">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户联系人邮箱：</span>
					<span id="merchantPersonEmail" class="required"></span>
				</li>
				<!--第三部分-->
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>营业执照号码：</span>
					<span id="businessLicense" class="required"></span>
				</li>
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>法人代表姓名：</span>
					<span id="legalPersonName" class="required"></span>
				</li>
				<li class="bottom">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>法人身份证号：</span>
					<span id="legalPersonID" class="required" ></span>
				</li>
				<!--第四部分-->
				<li class="install">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>安装归属：</span>
					<div class="install_select">
						<span id="loc_province"></span>
						<span id="loc_city"></span>
		    			<span id="loc_town"></span>
					</div>
				</li>
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>经营地址：</span>
					<span  id="operateAddress" class="required" ></span>
				</li>
			</ul>
			<input type="button" id="next" value="下一页" name="next"/>
			<input type="hidden" id="loginID" value="${loginID}" name="loginID"/>
		</form>
	</body>
</html>