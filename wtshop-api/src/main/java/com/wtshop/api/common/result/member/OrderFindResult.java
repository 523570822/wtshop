package com.wtshop.api.common.result.member;


import com.wtshop.api.common.result.PriceResult;
import com.wtshop.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/5/17.
 */

@ApiModel(value = "订单查询", description = "OrderListResult")
public class OrderFindResult implements Serializable{

    private String taxUrl;

    private String returnUrl;

    private Long time;

    private Long expire;

    @ApiModelProperty(value = "订单", dataType = "Order")
    private  Order order;

    @ApiModelProperty(value = "订单", dataType = "Order")
    private Shipping shippinging;

    @ApiModelProperty(value = "商品", dataType = "Goods")
    private List<OrderItem> orderItemList;

    private Member member;

    private Receiver receiver;

    private String receiveTime;

    private List<PriceResult> priceList;

    private String realMoney;

    private String couponMoney;

    private Boolean returns;


    public OrderFindResult(String taxUrl, String returnUrl, Long time, Long expire, Order order, Shipping shippinging, List<OrderItem> orderItemList, Member member, Receiver receiver, String receiveTime, List<PriceResult> priceList, String realMoney, String couponMoney, Boolean returns) {
        this.taxUrl = taxUrl;
        this.returnUrl = returnUrl;
        this.time = time;
        this.expire = expire;
        this.order = order;
        this.shippinging = shippinging;
        this.orderItemList = orderItemList;
        this.member = member;
        this.receiver = receiver;
        this.receiveTime = receiveTime;
        this.priceList = priceList;
        this.realMoney = realMoney;
        this.couponMoney = couponMoney;
        this.returns = returns;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getTaxUrl() {
        return taxUrl;
    }

    public void setTaxUrl(String taxUrl) {
        this.taxUrl = taxUrl;
    }

    public Boolean getReturns() {
        return returns;
    }

    public void setReturns(Boolean returns) {
        this.returns = returns;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public List<PriceResult> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceResult> priceList) {
        this.priceList = priceList;
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Shipping getShippinging() {
        return shippinging;
    }

    public void setShippinging(Shipping shippinging) {
        this.shippinging = shippinging;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
}
