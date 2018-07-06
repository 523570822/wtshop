package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Goods;
import com.wtshop.model.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */
@ApiModel(value = "评论添加", description = "ReviewAddResult")
public class ReviewAddResult implements Serializable {

    @ApiModelProperty(value = "评论", dataType = "OrderItem")
    private OrderItem orderItem;

    @ApiModelProperty(value = "商品", dataType = "Goods")
    private Goods goods;

    public ReviewAddResult(OrderItem orderItem, Goods goods) {
        this.orderItem = orderItem;
        this.goods = goods;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
