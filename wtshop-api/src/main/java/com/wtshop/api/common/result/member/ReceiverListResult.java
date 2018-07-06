package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Order;
import com.wtshop.model.Receiver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "收货地址列表", description = "ReceiverListResult")
public class ReceiverListResult implements Serializable{

    @ApiModelProperty(value = "收货地址", dataType = "Receiver")
    private Page<Receiver> page;


    public ReceiverListResult(Page<Receiver> page) {
        this.page = page;
    }

    public Page<Receiver> getPage() {
        return page;
    }

    public void setPage(Page<Receiver> page) {
        this.page = page;
    }
}
