package com.wtshop.api.common.result;

import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.plugin.PaymentPlugin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "订单支付", description = "OrderPayResult")
public class OrderPayResult implements Serializable {

    @ApiModelProperty(value = "手续费", dataType = "BigDecimal")
    private BigDecimal fee;

    @ApiModelProperty(value = "计算支付手续费", dataType = "BigDecimal")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付信息", dataType = "PaymentPlugin")
    private PaymentPlugin defaultPaymentPlugin;

    @ApiModelProperty(value = "支付信息集合", dataType = "PaymentPlugin")
    private List<PaymentPlugin> receiver;

    @ApiModelProperty(value = "会员", dataType = "Member")
    private Member mmeber;

    @ApiModelProperty(value = "订单", dataType = "Order")
    private Order order;


    public OrderPayResult(BigDecimal fee, BigDecimal amount, PaymentPlugin defaultPaymentPlugin, List<PaymentPlugin> receiver, Member mmeber, Order order) {
        this.fee = fee;
        this.amount = amount;
        this.defaultPaymentPlugin = defaultPaymentPlugin;
        this.receiver = receiver;
        this.mmeber = mmeber;
        this.order = order;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentPlugin getDefaultPaymentPlugin() {
        return defaultPaymentPlugin;
    }

    public void setDefaultPaymentPlugin(PaymentPlugin defaultPaymentPlugin) {
        this.defaultPaymentPlugin = defaultPaymentPlugin;
    }

    public List<PaymentPlugin> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<PaymentPlugin> receiver) {
        this.receiver = receiver;
    }

    public Member getMmeber() {
        return mmeber;
    }

    public void setMmeber(Member mmeber) {
        this.mmeber = mmeber;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


}
