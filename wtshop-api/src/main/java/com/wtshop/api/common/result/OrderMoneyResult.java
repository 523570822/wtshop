package com.wtshop.api.common.result;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sq on 2017/9/7.
 */
public class OrderMoneyResult implements Serializable{

    private double price;

    private double balance;

    private double payMoney;

    private Double expire;

    private Long orderId;

    public OrderMoneyResult(double price, double balance, double payMoney, Double expire, Long orderId) {
        this.price = price;
        this.balance = balance;
        this.payMoney = payMoney;
        this.expire = expire;
        this.orderId = orderId;
    }

    public Double getExpire() {
        return expire;
    }

    public void setExpire(Double expire) {
        this.expire = expire;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
