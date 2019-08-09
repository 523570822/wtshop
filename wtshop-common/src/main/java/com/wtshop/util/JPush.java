package com.wtshop.util;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import com.jfinal.log.Logger;
import com.wtshop.CommonAttributes;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/1.
 */
public class JPush {





    protected static final Logger logger =  Logger.getLogger(JPush.class);
    /**
     * 指定注册Id发送
     *
     * @param pushPKId 保存的推送表记录主键id
     * @param jpushRegId 注册ID
     * @param type 类型
     * @param jsonObject 需要app里处理的数据
     *
     */
    protected static  String appKey ="";
    protected static  String masterSecret = "";
    protected static  Boolean devMode = false;

    static{
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        appKey=prop.get("APP_KEY");
        masterSecret=prop.get("MASTER_SECRET");
        devMode = prop.getBoolean("devMode");
    }

    public static Logger getLogger() {
        return logger;
    }




    /**单用户推送
     *
     *
     * @param jpushRegId app用户注册id
     * @param type 类型(业务相关)
     * @param title 标题
     * @param content 内容
     * @param map 更详细的数据(业务相关)
     * @return
     */
     public static boolean sendPushById(final String jpushRegId, final String type, String title, String content,String sound, Map<String,Object> map) {
         logger.debug("jpushRegId:"+jpushRegId + ", type:"+type+", data:"+(map != null ? JSON.toJSON(map) : "{}"));
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
         String t=new Date().getTime()+"";
         List<String> uid=new ArrayList<>();
         uid.add(jpushRegId);
        PushPayload payload = buildPush(t ,uid, type, title, content, sound, map);
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Jpush Got result - " + result);
            return true;

        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            return false;

        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            return false;
        }
    }

    private void setStyleBasic(boolean opened){

    }


//多用户推送
    public static boolean sendPushByIds(final List<String> jpushRegId, final String type, String title,String content, Map<String,Object> map) {
        logger.debug("jpushRegId:"+jpushRegId + ", type:"+type+", data:"+(map != null ? JSON.toJSONString(map) : "{}"));
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        String t=new Date().getTime()+"";
        PushPayload payload = buildPush(t ,jpushRegId, type, title,content,null, map);
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Jpush Got result - " + result);
            return true;

        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            return false;

        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            return false;
        }
    }

    //所有用户推送


    //所有用户推送
    public static boolean sendPushAll( final String type, String title,String content,Map<String,Object> map) {
        logger.debug("jpushRegId:所有用户" + ", type:"+type+", data:"+(map != null ? JSON.toJSONString(map) : "{}"));
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        String t=new Date().getTime()+"";
        PushPayload payload = buildPushAll(t, type, title,content, map);
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Jpush Got result - " + result);
            return true;

        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            return false;

        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            return false;
        }
    }

    private static PushPayload buildPush(String pushPkID,final List<String> jpushRegIdList, final String type,final  String title, final String msgContent,final String sound, Map <String,Object> map) {
        logger.info("推送环境设置:::true-推送生产环境 false-推送开发环境,当前设置为" + !devMode);
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(jpushRegIdList))
                .setNotification(Notification.newBuilder()
                        .setAlert(msgContent)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtra("type", "type").addExtra("data", map != null ? JSON.toJSONString(map) : "{}").build()
                        )
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .setSound(sound)
                                .addExtra("type", "type").addExtra("data", map != null ? JSON.toJSONString(map)  : "{}").build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(!devMode)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .build())
                .build();


//

                //发送消息

//                .setMessage(Message.newBuilder()
//                        .setTitle(title)
//                        .setMsgContent(msgContent)
//                        .addExtra("msgType", type)
//                        .addExtra("msgContent", jsonObject != null ? jsonObject.toString() : "{}" )
//                        .addExtra("pushID", pushPkID)
//                        .build())
//                .setOptions(Options.newBuilder()
//                        .setApnsProduction(true)//是否用于生产环境
//                        .build())
//                .build();
    }


    private static PushPayload buildPushAll(String pushPkID, final String type,final  String title, final String msgContent, Map<String,Object> map) {
        return PushPayload.newBuilder()


                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(msgContent)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtra("type", "type").addExtra("data", map != null ?JSON.toJSONString(map)  : "{}").build()
                        )
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtra("type", "type").addExtra("data", map != null ?JSON.toJSONString(map)  : "{}").build())
                        .build())
                .build();
    }


    public static void main(String[] args) {
    }

}
