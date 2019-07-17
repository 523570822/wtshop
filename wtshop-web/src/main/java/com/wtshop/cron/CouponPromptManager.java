package com.wtshop.cron;

import com.jfinal.aop.Enhancer;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;

import com.wtshop.model.Identifier;
import com.wtshop.model.SpecialCoupon;
import com.wtshop.service.*;
import com.wtshop.util.JPush;
import com.wtshop.util.RedisUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.util.SMSUtils;

import java.util.List;

/**
 * Created by sq on 2017/8/4.
 */
public class CouponPromptManager implements ITask {
    Logger logger = Logger.getLogger(CouponPromptManager.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private DepositLogService depositLogService = Enhancer.enhance(DepositLogService.class);
    private IdentifierService identifierService = Enhancer.enhance(IdentifierService.class);
    private SpecialCouponService specialCouponService = Enhancer.enhance(SpecialCouponService.class);
    private SmsService smsService = Enhancer.enhance(SmsService.class);
    public void run() {
        Cache actCache = Redis.use();
        juHuiKaTiXing(30,actCache);
        juHuiKaTiXing(15,actCache);
        juHuiKaTiXing(7,actCache);
        juHuiKaTiXing(3,actCache);
        juHuiKaTiXing(2,actCache);
        juHuiKaTiXing(1,actCache);

        daiJinKaTiXing(30,actCache);
        daiJinKaTiXing(15,actCache);
        daiJinKaTiXing(7,actCache);
        daiJinKaTiXing(3,actCache);
        daiJinKaTiXing(2,actCache);
        daiJinKaTiXing(1,actCache);

    }
    public void daiJinKaTiXing(int day,Cache actCache){

        List<SpecialCoupon> specialCouponList = specialCouponService.findByDay("1", day+"");

        for (SpecialCoupon specialCoupon:specialCouponList) {

            String sound = "default";
            Object o = actCache.get("SOUND:" + specialCoupon.getMemberId());
            if (o != null) {
                sound = o.toString();
            }
            String key = "MEMBER:" + specialCoupon.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "代金卡活动提醒", "您的", ""+specialCoupon.getMoney()+"元代金卡还有"+day+"天就要过期了，请及时使用哦~", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }

            if(day<=3){
                //发短信
               // SMSUtils.

            }
        }
    };

    public void juHuiKaTiXing(int day,Cache actCache){

        List<Identifier> specialCouponList = identifierService.findByDay("1", day+"");

        for (Identifier specialCoupon:specialCouponList) {

            String sound = "default";
            Object o = actCache.get("SOUND:" + specialCoupon.getMemberId());
            if (o != null) {
                sound = o.toString();
            }
            String key = "MEMBER:" + specialCoupon.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "代金卡活动提醒", "您的", ""+specialCoupon.getMoney()+"元代金卡还有"+day+"天就要过期了，请及时使用哦~", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }

            if(day<=3){
                //发短信
           //     SMSUtils.send(specialCoupon.get)

            }
        }
    };

    public void stop() {

    }
}
