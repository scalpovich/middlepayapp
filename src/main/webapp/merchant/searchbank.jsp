<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>添加结算卡</title>
  <link rel="stylesheet" href="css/searchBank.css"/>
  <link rel="stylesheet" href="http://at.alicdn.com/t/font_400610_kcjlc26dck1n61or.css">
</head>
<body>
<form action="post" onsubmit="return false" class="form">
  <div class="searchBank-wrapper">
    <div class="search-wrap">
      <div class="inputWrapper" style="width:100%;">
        <label for="searchInput" style="margin: 0 auto;" id="label2">
          <i class="iconfont icon-shousuo"></i>
          <input id="searchInput" autocomplete="off" type="search" placeholder="请选择开户行"/>
        </label>
      </div>
      <!-- 搜索框后面的取消文字-->
      <span id="cancel" style="display:none">取消</span>
    </div>
    <ul id="ul"></ul>
  </div>
</form>

<script src="plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
<script src="js/oldFontSet.js"></script>
<script src="js/searchBank.js"></script>
</body>
</html>