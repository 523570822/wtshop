package com.wtshop.api.common.result;

import com.wtshop.entity.Organ;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.Receiver;

import java.util.List;

/**
 * Created by sq on 2017/9/1.
 */
public class VipOrganResult {

    private Member member;

    private Receiver receiver;

    private Goods goods;

    private List<Organ> organList;

    private Integer quantity;

    public VipOrganResult(Member member, Receiver receiver, Goods goods, List<Organ> organList, Integer quantity) {
        this.member = member;
        this.receiver = receiver;
        this.goods = goods;
        this.organList = organList;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public List<Organ> getOrganList() {
        return organList;
    }

    public void setOrganList(List<Organ> organList) {
        this.organList = organList;
    }
}
