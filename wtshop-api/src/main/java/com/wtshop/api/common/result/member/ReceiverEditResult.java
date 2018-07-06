package com.wtshop.api.common.result.member;

import com.wtshop.model.Receiver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "收货地址编辑", description = "ReceiverEditResult")
public class ReceiverEditResult implements Serializable{


    @ApiModelProperty(value = "收货地址", dataType = "Receiver")
    private Receiver receiver;

    public ReceiverEditResult( Receiver receiver) {
        this.receiver = receiver;
    }


    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
