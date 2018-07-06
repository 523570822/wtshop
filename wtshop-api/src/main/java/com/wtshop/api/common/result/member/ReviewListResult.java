package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "评论列表", description = "ReviewListResult")
public class ReviewListResult implements Serializable {

    @ApiModelProperty(value = "是否评论", dataType = "Boolean")
    private Boolean isReview;

    @ApiModelProperty(value = "待评论的订单行", dataType = "OrderItem")
    private Page<OrderItem>  pendingOrderItems;

    public ReviewListResult(Boolean isReview, Page<OrderItem> pendingOrderItems) {
        this.isReview = isReview;
        this.pendingOrderItems = pendingOrderItems;
    }

    public Boolean getReview() {
        return isReview;
    }

    public void setReview(Boolean review) {
        isReview = review;
    }

    public Page<OrderItem> getPendingOrderItems() {
        return pendingOrderItems;
    }

    public void setPendingOrderItems(Page<OrderItem> pendingOrderItems) {
        this.pendingOrderItems = pendingOrderItems;
    }
}
