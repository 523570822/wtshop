package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.CouponCode;
import com.wtshop.plugin.PaymentPlugin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "预存款充值", description = "DepositRechargeResult")
public class DepositRechargeResult implements Serializable {

    @ApiModelProperty(value = "收支信息集合", dataType = "PaymentPlugin")
    private List<PaymentPlugin> paymentPlugins;

    @ApiModelProperty(value = "收支信息", dataType = "PaymentPlugin")
    private PaymentPlugin paymentPlugin;

    public DepositRechargeResult(List<PaymentPlugin> paymentPlugins, PaymentPlugin paymentPlugin) {
        this.paymentPlugins = paymentPlugins;
        this.paymentPlugin = paymentPlugin;
    }

    public List<PaymentPlugin> getPaymentPlugins() {
        return paymentPlugins;
    }

    public void setPaymentPlugins(List<PaymentPlugin> paymentPlugins) {
        this.paymentPlugins = paymentPlugins;
    }

    public PaymentPlugin getPaymentPlugin() {
        return paymentPlugin;
    }

    public void setPaymentPlugin(PaymentPlugin paymentPlugin) {
        this.paymentPlugin = paymentPlugin;
    }
}
