package com.wtshop.api.common.result.member;

import com.wtshop.model.ReturnsItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "等待商家处理退货申请", description = "ServiceNotDetailsResult")
public class ServiceNotDetailsResult implements Serializable{

    @ApiModelProperty(value = "退货项", dataType = "ReturnsItem")
    private ReturnsItem returnsItem;

    @ApiModelProperty(value = "类型 return", dataType = "String")
    private String type;

    public ServiceNotDetailsResult(ReturnsItem returnsItem, String type) {
        this.returnsItem = returnsItem;
        this.type = type;
    }

    public ReturnsItem getReturnsItem() {
        return returnsItem;
    }

    public void setReturnsItem(ReturnsItem returnsItem) {
        this.returnsItem = returnsItem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
