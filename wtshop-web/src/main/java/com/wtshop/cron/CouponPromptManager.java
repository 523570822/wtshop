package com.wtshop.cron;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;

import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.model.Identifier;
import com.wtshop.model.Sms;
import com.wtshop.model.SpecialCoupon;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.JPush;
import com.wtshop.util.RedisUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.util.SMSUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        List<SpecialCoupon> specialCouponList = specialCouponService.findByDay("1", day+"");
        Cache sm = Redis.use();
        for (SpecialCoupon specialCoupon:specialCouponList) {

            String mobile=specialCoupon.member.getPhone();
            String price=specialCoupon.getMoney().toString() ;
            String sound = "default";
            Object o = actCache.get("SOUND:" + specialCoupon.getMemberId());
            if (o != null) {
                sound = o.toString();
            }
            String key = "MEMBER:" + specialCoupon.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "代金卡活动消息", "代金卡活动提醒", "您的"+price+"元代金卡还有"+day+"天就要过期了，请及时使用哦~", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }
            if(day<=3){
                Map<String, Object> params = new HashMap<String, Object>();

                params.put("price",price);
                params.put("date",day );

                //检查手机号码有效性
                if (!SMSUtils.isMobileNo(mobile)) {
                    logger.error("请检查用户"+specialCoupon.getMemberId()+"手机号是否正确!——————————————————————");
                    //    renderJson(ApiResult.fail("请检查手机号是否正确!"));
                    continue;
                }
                ApiResult result = SMSUtils.send(mobile,"SMS_170840857", params);
                //ApiResult result = SMSUtils.send("", "", params);
                if(result.resultSuccess()) {
                   // sm.setex("PONHE:"+mobile,120,"1");
                    Sms sms = new Sms();
                    sms.setMobile(mobile);
                    sms.setSmsCode("您的"+price+"元代金卡还有"+day+"天就要过期了，请及时使用哦~");
                    sms.setSmsType(Setting.SmsType.other.ordinal());
                    smsService.saveOrUpdate(sms);
                    logger.info("您的"+price+"元代金卡还有"+day+"天就要过期了，请及时使用哦~");
                }else {
                    logger.info("您发送的过于频繁,请稍后再试!");
                }

            }
        }
    };

    public void juHuiKaTiXing(int day,Cache actCache){
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        List<Identifier> specialCouponList = identifierService.findByDay("1", day+"");
        Cache sm = Redis.use();
        if(specialCouponList!=null){


        for (Identifier specialCoupon:specialCouponList) {
           String mobile=specialCoupon.member.getPhone();
            BigDecimal price = specialCoupon.getTotalMoney().multiply(specialCoupon.getPrice());;
            String name=specialCoupon.member.getStore();
            String sound = "default";
            Object o = actCache.get("SOUND:" + specialCoupon.getMemberId());
            if (o != null) {
                sound = o.toString();
            }
            String key = "MEMBER:" + specialCoupon.getMemberId().toString();
            String appid = RedisUtil.getString(key);
            if (appid != null) {
                logger.info("开始调用极光推送方法——————————————————————; appid=" + appid);
                JPush.sendPushById(appid, "钜惠卡通知消息", "钜惠活动提醒", "您的"+name+"钜惠卡当前待消费金额为"+price+"元,还有"+day+"天就要过期了，请及时使用哦~", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }

            if(day<=3){
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("name", name );
                params.put("price",price);
                params.put("date",day );

                //检查手机号码有效性
                if (!SMSUtils.isMobileNo(mobile)) {
                    logger.error("请检查用户"+specialCoupon.getMemberId()+"手机号是否正确!——————————————————————");
                //    renderJson(ApiResult.fail("请检查手机号是否正确!"));
                    continue;
                }
              ApiResult result = SMSUtils.send(mobile,"SMS_170835788", params);
                //ApiResult result = SMSUtils.send("", "", params);
                if(result.resultSuccess()) {
             //       sm.setex("PONHE:"+mobile,120,"1");
                    Sms sms = new Sms();
                    sms.setMobile(mobile);
                    sms.setSmsCode("您的"+name+"钜惠卡当前待消费金额为"+price+"元,还有"+day+"天就要过期了，请及时使用哦~");
                    sms.setSmsType(Setting.SmsType.other.ordinal());
                    smsService.saveOrUpdate(sms);
                    logger.info("短信发送成功！【您的"+name+"钜惠卡当前待消费金额为"+price+"元,还有"+day+"天就要过期了，请及时使用哦~】");
                }else {
                    logger.info("您发送的过于频繁,请稍后再试!");
                }

            }
        }
        }
    };

    public void stop() {

    }
}
