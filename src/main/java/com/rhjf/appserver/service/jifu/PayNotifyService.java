package com.rhjf.appserver.service.jifu;

import com.rhjf.appserver.constant.Constant;
import com.rhjf.appserver.constant.RespCode;
import com.rhjf.appserver.db.LoginUserDAO;
import com.rhjf.appserver.db.TradeDAO;
import com.rhjf.appserver.model.Fee;
import com.rhjf.appserver.model.PayOrder;
import com.rhjf.appserver.model.LoginUser;
import com.rhjf.appserver.service.FeeComputeService;
import com.rhjf.appserver.util.*;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hadoop on 2018/3/20.
 *
 * @author hadoop
 */
public class PayNotifyService {

    private LoggerTool log = new LoggerTool(this.getClass());

    public Object paynotify(HttpServletRequest request , HttpServletResponse response){


        log.info("收到即富支付回调通知");

        String signature = request.getParameter("signature");
        String encryptData = request.getParameter("encryptData");

        if(UtilsConstant.strIsEmpty(encryptData)){
            log.info("接收到的即富通知报文为空");
            return RespCode.notifyfail;
        }

        log.info("收到的报文：signature : " + signature + "   =====  encryptData :" + encryptData);
        String plainText = AESCBCUtil.decrypt(encryptData, JifuConstant.aesKey.getBytes(), JifuConstant.aesKey.getBytes());

        log.info("接收回调的明文 : " + plainText);

        String sign = MD5.getSha1(encryptData + JifuConstant.signKey);

        log.info(sign + "====" + signature);

        if(!sign.toLowerCase().equals(signature.toLowerCase())){
            log.info("报文签名和计算签名不一致");
            return RespCode.notifyfail;
        }

        JSONObject plainTextJSON = JSONObject.fromObject(plainText);

        JSONObject head = JSONObject.fromObject(plainTextJSON.getString("head"));


        String orderNumber = head.getString("orderId");

        PayOrder order = TradeDAO.getPayOrderInfo(orderNumber);


        if(order==null){
            log.info("订单号：" + orderNumber + "未查到订单信息");
            return RespCode.notifyfail;
        }

        if(Constant.payRetCode.equals(order.getPayRetCode())){
            log.info("订单号：" + orderNumber + "已经成功支付");
            return RespCode.notifySuccess;
        }


        String respCode = head.getString("respCode");
        if ("000000".equals(respCode) && "01".equals(plainTextJSON.getString("orderStatus"))) {

            log.info("支付成功 , workId(上游平台流水号) :" + head.getString("workId"));


            log.info("订单 ：" + orderNumber  + "支付成功");
            String retMsg = "支付成功";
            respCode = Constant.payRetCode;

            LoginUser loginUser;
            try {
                loginUser = LoginUserDAO.getLoginuserInfo(order.getUserID());
            } catch (Exception e) {
                log.info(e.getMessage());
                return RespCode.notifyfail;
            }


            FeeComputeService notifyService = new FeeComputeService();
            Fee fee = notifyService.calProfit(orderNumber ,order, loginUser);

            if(fee==null){
                log.info("订单：" +  orderNumber + "计算手续费失败");
                return RespCode.notifyfail;
            }

            int updateRet = TradeDAO.updatePayOrder(new Object[]{respCode ,retMsg ,fee.getMerchantFee() , 0  , order.getID()});
            if(updateRet < 1){
                log.info("订单号：" + orderNumber + "更新数据库失败");
                return RespCode.notifyfail;
            }
            int x = TradeDAO.saveProfit(new Object[]{
                    UtilsConstant.getUUID(),loginUser.getID(), order.getID() ,fee.getMerchantFee(),null,0,
                    null,0,
                    0,fee.getPlatformProfit(),fee.getPlatCostFee()
            });
            if(x < 1){
                log.info("订单号:"  + orderNumber + "保存收益记录失败");
                return RespCode.notifyfail;
            }

            EhcacheUtil ehcache = EhcacheUtil.getInstance();
            ehcache.clear(Constant.cacheName);
        }

        return "000000";
    }
}
