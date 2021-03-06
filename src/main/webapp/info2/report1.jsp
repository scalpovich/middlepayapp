<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.rhjf.appserver.util.KeyBean" %> 
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
		<script src="js/report1.js" type="text/javascript" charset="utf-8"></script>
		
		<link rel="stylesheet" type="text/css" href="css/select2.css"/>
		<link rel="stylesheet" type="text/css" href="css/report.css"/>
		<%
			String loginID = request.getParameter("loginID");
			String sign = request.getParameter("sign");
			KeyBean keyBean = new KeyBean();
			String key = keyBean.getkeyBeanofStr(loginID);
			
			if(!key.equals(sign)){
				String url =  request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/register/register.jsp";
				response.sendRedirect(url);
			}
			request.setAttribute("loginID",loginID );
	 	%>
	</head>
	<body>
		<form class="form" style="padding-bottom:2.5rem;">
			<ul class="first">
				<!--第一部分-->
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户名称：</span>
					<input type="text" name="merchantName" id="merchantName" value="" class="required" placeholder="请输入商户名称"/>
				</li>
				<!-- <li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户类型：</span>
					<select name="merchantType" id="merchantType">
						<option value="ENTERPRISE" name="ENTERPRISE">企业商户</option>
						<option value="INSTITUTION" name="INSTITUTION">事业单位商户</option>
						<option value="INDIVIDUALBISS" name="INDIVIDUALBISS">个体工商户</option>
						<option value="PERSON" name="PERSON">个人商户</option> 
					</select>
				</li> -->
				<!-- <li class="bottom">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>签购单显示名称：</span>
					<input type="text" name="merchantBillName" id="merchantBillName" value="" class="required" placeholder="请输入签购单显示名称"/>
				</li> -->
				<!--第二部分-->
				<li class="bottom">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户联系人姓名：</span>
					<input type="text" name="merchantPersonName" id="merchantPersonName" value="" class="required" placeholder="请输入商户联系人姓名"/>
				</li>
				<!-- <li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户联系人电话：</span>
					<input type="text" name="merchantPersonPhone" id="merchantPersonPhone" value="" class="required" placeholder="请输入格式正确的商户联系人电话"/>
				</li> -->
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>商户联系人邮箱：</span>
					<input type="text" name="merchantPersonEmail" id="merchantPersonEmail" value="" class="required" placeholder="请输入格式正确的商户联系人邮箱"/>
				</li>
				<!--第三部分-->
				<!-- <li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>营业执照号码：</span>
					<input type="text" name="businessLicense" id="businessLicense" value="" class="required" placeholder="营业执照号码"/>
				</li> -->
				<!-- <li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>法人代表姓名：</span>
					<input type="text" name="legalPersonName" id="legalPersonName" value="" class="required" placeholder="请输入法人代表姓名"/>
				</li> -->
				<li class="bottom">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>法人身份证号：</span>
					<input type="text" name="legalPersonID" id="legalPersonID" value="" class="required" placeholder="请输入法人身份证号"/>
				</li>
				<!--第四部分-->
				<li class="install">
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>安装归属：</span>
					<div class="install_select">
						<select id="loc_province" readonly></select>
						<select id="loc_city" readonly></select>
		    			<select id="loc_town" readonly></select>
					</div>
					
				</li>
				<li>
					<span><font style="color: #ff0000;font-size: 0.6rem;padding-right: 0.2rem;">*</font>经营地址：</span>
					<input type="text" name="operateAddress" id="operateAddress" value="" class="required" placeholder="请输入经营地址"/>
				</li>
			</ul>
			<input type="button" name="next" id="next" value="下一页" /> 
			<input type="hidden" id="loginID" value="${loginID}" name="loginID"/>
		</form>
	</body>
</html>