package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Coupon;
import com.wtshop.model.CouponCode;
import com.wtshop.model.Goods;
import com.wtshop.model.Ticketreceive;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * Created by sq on 2017/5/16.
 */

@ApiModel(value = "优惠码列表", description = "CouponCodeListResult")
public class CouponCodeListResult implements Serializable{

    @ApiModelProperty(value = "商品", dataType = "CouponCode")
    private Page<Coupon> goods;

    @ApiModelProperty(value = "是否使用", dataType = "Object")
    private Object isUsed;

    private Page<Ticketreceive> pageList ;

    public CouponCodeListResult(Page<Coupon> goods, Object isUsed, Page<Ticketreceive> pageList) {
        this.goods = goods;
        this.isUsed = isUsed;
        this.pageList = pageList;
    }

    public Page<Coupon> getGoods() {
        return goods;
    }

    public void setGoods(Page<Coupon> goods) {
        this.goods = goods;
    }

    public Object getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Object isUsed) {
        this.isUsed = isUsed;
    }

    public Page<Ticketreceive> getPageList() {
        return pageList;
    }

    public void setPageList(Page<Ticketreceive> pageList) {
        this.pageList = pageList;
    }
}
