package com.rhjf.appserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 *
 *   支付回调控制器
 *
 * Created by hadoop on 2018/3/20.
 *
 * @author hadoop
 */

@Controller
@RequestMapping("/tradenotify")
@ResponseBody
public class TradeNotifyController {


    @RequestMapping(value = "{channelID}")
    public Object notify(@PathVariable("channelID") String channelID,
                         HttpServletRequest request, HttpServletResponse response) {

        try {
            Class clasz = Class.forName("com.rhjf.appserver.service." + channelID.trim().toLowerCase() + ".PayNotifyService");
            Method method = clasz.getMethod("paynotify", new Class[]{HttpServletRequest.class, HttpServletResponse.class});
            Object obj = method.invoke(clasz.newInstance(), request, response);
            return obj.toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "fail";
    }
}
