package com.wtshop.api.common.result;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Receiver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "收货地址", description = "OrderAddressResult")
public class OrderAddressResult  implements Serializable {

    @ApiModelProperty(value = "url方法", dataType = "String")
    private String curRedirectUrl;

    @ApiModelProperty(value = "url", dataType = "String")
    private String redirectUrl;

    @ApiModelProperty(value = "收货人ID", dataType = "Long")
    private Long receiverId;

    @ApiModelProperty(value = "名称", dataType = "String")
    private String title;

    @ApiModelProperty(value = "收货地址分页", dataType = "Receiver")
    private Page<Receiver> receiver;

    public OrderAddressResult(String curRedirectUrl, String redirectUrl, Long receiverId, String title, Page<Receiver> receiver) {
        this.curRedirectUrl = curRedirectUrl;
        this.redirectUrl = redirectUrl;
        this.receiverId = receiverId;
        this.title = title;
        this.receiver = receiver;
    }

    public String getCurRedirectUrl() {
        return curRedirectUrl;
    }

    public void setCurRedirectUrl(String curRedirectUrl) {
        this.curRedirectUrl = curRedirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Page<Receiver> getReceiver() {
        return receiver;
    }

    public void setReceiver(Page<Receiver> receiver) {
        this.receiver = receiver;
    }
}
