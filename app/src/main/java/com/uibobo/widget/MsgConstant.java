package com.uibobo.widget;

/**
 * @Author: bo
 * @Date: 2021/5/8 21:48
 */
public class MsgConstant {
    public final static String url = "马赛克";
    /**
     * 发送内容
     */
    public final static String send1 = "马赛克";
    public final static String send2 = "ping";
    public final static String send3 = "马赛克";

    //简单判断一下内容
    public static int order(String msg){
        if(msg.contains("hbInterval")){
            return 1;
        }else if(msg.equals("pong")){
            return 2;
        }else if(msg.contains("deviceid")&&msg.contains("error")){
            return 3;
        }else if(msg.contains("deviceid")&&msg.contains("update")){
            return 4;
        }
        return 0;
    }

}
