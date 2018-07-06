package com.wtshop.api.common.result.member;

import com.wtshop.api.common.result.ReturnItem;
import com.wtshop.model.Receiver;
import com.wtshop.model.ReturnsItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "售后列表", description = "ServiceListResult")
public class ServiceListResult implements Serializable{


    @ApiModelProperty(value = "退货申请", dataType = "ReturnsItem")
    private List<ReturnItem> returnItem;

    public ServiceListResult(List<ReturnItem> returnItem) {
        this.returnItem = returnItem;
    }

    public List<ReturnItem> getReturnItem() {
        return returnItem;
    }

    public void setReturnItem(List<ReturnItem> returnItem) {
        this.returnItem = returnItem;
    }
}
