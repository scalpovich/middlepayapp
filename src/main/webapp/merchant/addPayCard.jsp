<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>添加结算卡</title>
    <link rel="stylesheet" href="css/addCard.css"/>
</head>
<body class="addCard">
<section class="box">
    <form class="form"  id="cardInfo" onsubmit="return false">
        <p><img src="imgs/idCard.png" alt="">信用卡信息</p>
        <ul>
            <li><label>信用卡号</label><input id="bankNum" name="bankNum" type="text" placeholder="请输入持卡人信用卡号" /></li>
        </ul>
        <div>
            <!--postInfo-->
            <button class="btn-submit2" type="button" name="next" id="addInfoBtn">补充资质</button>
        </div>
    </form>
</section>
    <script src="plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="js/rem.js"></script>
    <script src="js/new_reg_test.js"></script>
</body>
</html>