<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.rhjf.appserver.util.KeyBean" %>  
<%
	String loginID = request.getParameter("loginID");
	String sign = request.getParameter("sign");
	KeyBean keyBean = new KeyBean();
	String key = keyBean.getkeyBeanofStr(loginID);
	
	 /* if(!key.equals(sign)){
		String url =  request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/register/register.jsp";
		response.sendRedirect(url);
	}  */
	request.setAttribute("loginID",loginID );
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
  <link rel="stylesheet" type="text/css" href="css/add_bussiness.css"/>
  <title>添加商户</title>
</head>
<body class="add_member">
	<form class="form" onsubmit="return false">
	  <ul id="inputUl" class="first">
	    <li class='merchantInfos'>
	      <p class='merchantImg four_size'><img src="imgs/info.jpg" alt=""/>商户信息</p>
	      <div class='merchantInfo'>
	         <label class="four_size">商户名称</label>
	          <input type="text" name="merchantName" id="merchantName" value=""  class="required four_size_input bussInput"  placeholder="请输入商户名称 (5-12位)" required/>
	      </div>
	
	      <!--详细门牌号需要定位  百度地图-->
	       <div class='merchantInfo address'>
	          <span class="four_size">经营地址</span>
	          <!--<input readonly="readonly" readonlyunselectable="on"  type="text" name="operateAddress" id="operateAddress" value="" class="four_size_input  bussInput" placeholder="请点击选择" /><img id="showAdd"
	              src="imgs/address.png" alt=""/>-->
	         <p  id="operateAddress" class="four_size_input  bussInput">点击获取经营地址</p>
	       </div>
				<input type="hidden" name="city" id="city" value="123"/>
				<input type="hidden" name="province" id="province" />
				<input type="hidden" name="Region" id="Region" />
			<input type="hidden" id="loginID" value="${loginID}" name="loginID"/>
	      <div class='merchantInfo'>
	          <label class="four_size">门牌号 <i>(选填)</i></label>
	          <input name="houseNum" id="houseNum" class="required" placeholder="详细门牌号"/>
	      </div>
	    </li>
	  </ul>
	  <button class="next" id="next" type="button">下一步</button>
	</form>
	<script src="plugins/jquery-1.11.0.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/rem.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/new_reg_test.js" type="text/javascript" charset="utf-8"></script>
	<script src="http://api.map.baidu.com/api?v=2.0&ak=HbUVYMUg6PwbOnXkztdgSQlQ"></script>
	<script src="js/localPosition.js"></script>
</body>
</html>