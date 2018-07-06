package com.wtshop.api.common.result.member;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "收货地址添加", description = "ReceiverListResult")
public class ReceiverAddResult implements Serializable{

    @ApiModelProperty(value = "地址", dataType = "String")
    private String url;

    public ReceiverAddResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
