<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>添加结算卡</title>
    <link rel="stylesheet" href="css/pay.css"/>
    <link rel="stylesheet" href="css/mPicker.css"/>
</head>
<body class="payBody">
    <section class="box" >
        <!--<div class="payCard"> <img src="imgs/card.png" alt=""/> 结算银行卡</div>-->
        <form class="form" id="payInfo" onsubmit="return false" >
            <p><img src="imgs/paying.png" alt="">结算银行卡</p>
            <ul>
                <li class="liReg"><label for="holderName">持卡人姓名</label><input id="holderName" placeholder="输入持卡人姓名" type="text"  /></li>
               	<li class="liReg"><label for="idCard">身份证号码</label><input id="idCard"  name="idCard" type="text" placeholder="输入持卡人身份证"/></li>
                <li class="liReg"><label for="bankNum">银行卡号</label><input id="bankNum"  name="bankNum" type="text" placeholder="输入银行卡号"/></li>
                <li><label for="bankAdd">开户行地址</label><input id="bankAdd" class="chooseBank" name="bankAdd" type="text" placeholder="省/市"/><img
                        src="imgs/right.png" alt=""/></li>
                <li class="liReg"><label for="branchBank">分行/支行</label><input class="chooseBank" id="branchBank" name="branchBank" placeholder="请选择"/><img
                        src="imgs/right.png" alt=""/></li>
            </ul>
            <div>
                <button id="postInfo">补充资质</button>
            </div>
        </form>
    </section>

    <div id="remindBox" style="display: none;">
        <p>当前单日限额3万元,提供新信用卡信息可提供20万元,是否提供?</p>
        <div>
            <a id="jump" href="#">跳过</a><!--苹果端填写自己的链接-->
            <a id="provide" href="addPayCard.jsp">提供</a>
        </div>
    </div>
    <!--2017.08.31-->
    <div class="zhezhao"></div>
    <!--2017.08.31-->

    <script src="js/rem.js" type="text/javascript"></script>
    <script src="plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="plugins/mPicker.js" type="text/javascript"></script>
    <script src="plugins/city.js" type="text/javascript"></script>
    <script src="js/pay.js" type="text/javascript"></script>
</body>
</body>
</html>