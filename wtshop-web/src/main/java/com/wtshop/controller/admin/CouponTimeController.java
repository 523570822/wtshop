package com.wtshop.controller.admin;

import com.wtshop.model.Coupon;
import com.wtshop.service.CouponService;

import java.util.List;

/**
 * Created by sq on 2017/8/7.
 */
public class CouponTimeController extends BaseController implements Runnable{

    private CouponService couponService = enhance(CouponService.class);

    public void run() {
        List<Coupon> all = couponService.findCoupon();
        for(Coupon coupon : all){
            if(!coupon.hasExpired() && coupon.hasBegun()){
                coupon.setStatus(Coupon.status.using.ordinal());
                couponService.update(coupon);
            }
            if(coupon.hasExpired()){
                coupon.setStatus(Coupon.status.expire.ordinal());
                couponService.update(coupon);
            }
        }
    }
}
