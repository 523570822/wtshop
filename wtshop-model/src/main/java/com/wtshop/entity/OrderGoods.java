package com.wtshop.entity;

import com.wtshop.model.Goods;
import com.wtshop.model.Order;

import java.util.List;

/**
 * Created by sq on 2017/6/16.
 */
public class OrderGoods {

    private List<Goods> goodsList;

    private Order order;

    public OrderGoods(List<Goods> goodsList, Order order) {
        this.goodsList = goodsList;
        this.order = order;
    }

    public OrderGoods() {
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
