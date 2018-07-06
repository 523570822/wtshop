package com.wtshop.api.common.result;

import com.wtshop.model.Receiver;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/7/12.
 */
public class FuDaiResult implements Serializable {

    private Double balance;

    private Receiver receiver;

    private Double price ;

    private List<Delivery> deliveryList;


    public FuDaiResult(Double balance, Receiver receiver, Double price, List<Delivery> deliveryList) {
        this.balance = balance;
        this.receiver = receiver;
        this.price = price;
        this.deliveryList = deliveryList;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Delivery> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }
}
