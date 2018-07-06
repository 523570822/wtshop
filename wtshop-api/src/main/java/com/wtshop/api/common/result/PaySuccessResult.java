package com.wtshop.api.common.result;

import com.wtshop.model.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "支付成功", description = "PaySuccessResult")
public class PaySuccessResult implements Serializable{

    @ApiModelProperty(value = "订单项", dataType = "Order")
    private Order order;

    public PaySuccessResult(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
