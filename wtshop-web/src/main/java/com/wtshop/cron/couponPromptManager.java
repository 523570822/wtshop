package com.wtshop.cron;

import com.jfinal.aop.Enhancer;
import com.jfinal.log.Logger;
import com.jfinal.plugin.cron4j.ITask;

import com.wtshop.model.SpecialCoupon;
import com.wtshop.service.DepositLogService;
import com.wtshop.service.IdentifierService;
import com.wtshop.service.MemberService;
import com.wtshop.service.SpecialCouponService;
import com.wtshop.util.JPush;
import com.wtshop.util.RedisUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import java.util.List;

/**
 * Created by sq on 2017/8/4.
 */
public class couponPromptManager implements ITask {
    Logger logger = Logger.getLogger(couponPromptManager.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private DepositLogService depositLogService = Enhancer.enhance(DepositLogService.class);
    private IdentifierService identifierService = Enhancer.enhance(IdentifierService.class);
    private SpecialCouponService specialCouponService = Enhancer.enhance(SpecialCouponService.class);
    public void run() {


        List<SpecialCoupon> specialCouponList = specialCouponService.findByDay("1", "30");
        Cache actCache = Redis.use();
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
                JPush.sendPushById(appid, "团购消息", "您有", ""+0+"个团购提醒,活动即将开始，快去看看吧！", sound, null);
                logger.info("成功调用极光推送方法——————————————————————");
            }
        }

    }

    public void stop() {

    }
}
