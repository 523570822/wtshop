package com.wtshop.api.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "订单发票", description = "OrderInvoiceResult")
public class OrderInvoiceResult implements Serializable {

    @ApiModelProperty(value = "url", dataType = "String")
    private String url_forward;

    @ApiModelProperty(value = "名称", dataType = "String")
    private String title;

    public OrderInvoiceResult(String url_forward, String title) {
        this.url_forward = url_forward;
        this.title = title;
    }

    public String getUrl_forward() {
        return url_forward;
    }

    public void setUrl_forward(String url_forward) {
        this.url_forward = url_forward;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
