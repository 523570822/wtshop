package com.wtshop.api.common.result.member;

import com.wtshop.model.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "售后添加", description = "ServiceAddResult")
public class ServiceAddResult {

    @ApiModelProperty(value = "订单项", dataType = "OrderItem")
    private OrderItem orderItem;

    public ServiceAddResult(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
