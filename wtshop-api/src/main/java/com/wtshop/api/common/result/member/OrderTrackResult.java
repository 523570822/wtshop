package com.wtshop.api.common.result.member;

import com.wtshop.api.common.result.TrackResult;
import com.wtshop.model.Order;
import com.wtshop.model.Shipping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "订单物流跟踪", description = "OrderTrackResult")
public class OrderTrackResult implements Serializable{

    @ApiModelProperty(value = "订单", dataType = "Order")
    private Shipping shipping;

    private List<TrackResult> trackResults;

    public OrderTrackResult(Shipping shipping, List<TrackResult> trackResults) {
        this.shipping = shipping;
        this.trackResults = trackResults;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<TrackResult> getTrackResults() {
        return trackResults;
    }

    public void setTrackResults(List<TrackResult> trackResults) {
        this.trackResults = trackResults;
    }
}
