package com.wtshop.api.common.result;

import com.wtshop.model.Order;
import com.wtshop.model.OrderItem;

import java.util.List;

/**
 * Created by sq on 2017/7/24.
 */
public class OrderItemList {

    private Order order;

    private List<OrderItem> orderItemList;

    public OrderItemList(Order order, List<OrderItem> orderItemList) {
        this.order = order;
        this.orderItemList = orderItemList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
