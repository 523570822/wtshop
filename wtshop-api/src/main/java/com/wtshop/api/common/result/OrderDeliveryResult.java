package com.wtshop.api.common.result;

import com.wtshop.model.PaymentMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "订单物流", description = "OrderDeliveryResult")
public class OrderDeliveryResult implements Serializable {


    @ApiModelProperty(value = "url", dataType = "String")
    private String url_forward;

    @ApiModelProperty(value = "收支方式", dataType = "paymentMethodId")
    private Long paymentMethodId;

    @ApiModelProperty(value = "名称", dataType = "String")
    private String title;

    @ApiModelProperty(value = "收支方式", dataType = "PaymentMethod")
    private List<PaymentMethod> paymentMethod;

    public OrderDeliveryResult(String url_forward, Long paymentMethodId, String title, List<PaymentMethod> paymentMethod) {
        this.url_forward = url_forward;
        this.paymentMethodId = paymentMethodId;
        this.title = title;
        this.paymentMethod = paymentMethod;
    }

    public String getUrl_forward() {
        return url_forward;
    }

    public void setUrl_forward(String url_forward) {
        this.url_forward = url_forward;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PaymentMethod> getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(List<PaymentMethod> paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}



