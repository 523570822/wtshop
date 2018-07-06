package com.wtshop.api.common.result.member;

import com.wtshop.model.DeliveryCorp;
import com.wtshop.model.Returns;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "退货信息", description = "ServiceReturnResult")
public class ServiceReturnResult implements Serializable{

    @ApiModelProperty(value = "退货项", dataType = "Returns")
    private Returns returns;

    @ApiModelProperty(value = "物流公司", dataType = "DeliveryCorp")
    private List<DeliveryCorp> deliveryCorps;

    public ServiceReturnResult(Returns returns, List<DeliveryCorp> deliveryCorps) {
        this.returns = returns;
        this.deliveryCorps = deliveryCorps;
    }

    public Returns getReturns() {
        return returns;
    }

    public void setReturns(Returns returns) {
        this.returns = returns;
    }

    public List<DeliveryCorp> getDeliveryCorps() {
        return deliveryCorps;
    }

    public void setDeliveryCorps(List<DeliveryCorp> deliveryCorps) {
        this.deliveryCorps = deliveryCorps;
    }
}
