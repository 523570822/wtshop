package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.api.common.result.OrderGoods;
import com.wtshop.model.Message;
import com.wtshop.model.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "订单列表", description = "OrderListResult")
public class OrderListResult implements Serializable {

    @ApiModelProperty(value = "订单", dataType = "Order")
    private List<OrderGoods> page;

    @ApiModelProperty(value = "订单状态", dataType = "Object")
    private  Object Status;

    public OrderListResult(List<OrderGoods> page, Object status) {
        this.page = page;
        Status = status;
    }

    public List<OrderGoods> getPage() {
        return page;
    }

    public void setPage(List<OrderGoods> page) {
        this.page = page;
    }

    public Object getStatus() {
        return Status;
    }

    public void setStatus(Object status) {
        Status = status;
    }
}
