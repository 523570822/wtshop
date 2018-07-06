package com.wtshop.api.common.result;

import com.wtshop.model.Coupon;

import java.io.Serializable;

/**
 * Created by jobfo on 2017/7/30.
 */
public class CouponCodeResult implements Serializable{

    private Coupon coupon ;

    private Long count;

    public CouponCodeResult(Coupon coupon, Long count) {
        this.coupon = coupon;
        this.count = count;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
