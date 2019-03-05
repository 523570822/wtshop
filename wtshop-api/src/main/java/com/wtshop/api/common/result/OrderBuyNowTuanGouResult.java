package com.wtshop.api.common.result;

import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.Receiver;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author Shi Qiang
 * @date 2018/6/14 13:49
 */
public class OrderBuyNowTuanGouResult implements Serializable{


    private String taxUrl;

    private Double delivery;

    private Member member;

    private Receiver receiver;

    private Goods goods;

    private Integer quantity;

    private String receiveTime;

    private Boolean is_freeMoney;

    private Boolean is_useMiaobi;

    private String miaoBiDesc;

    private List<PriceResult> priceList;

    private String realPrice;

    private String couponPrice;

    private Double[] param;

    private Boolean is_promotion;

    public Long getTuanGouId() {
        return tuanGouId;
    }

    public void setTuanGouId(Long tuanGouId) {
        this.tuanGouId = tuanGouId;
    }

    private Long tuanGouId;

    public Boolean getSinglepurchase() {
        return isSinglepurchase;
    }

    public void setSinglepurchase(Boolean singlepurchase) {
        isSinglepurchase = singlepurchase;
    }

    public Long getFightGroupId() {
        return fightGroupId;
    }

    public void setFightGroupId(Long fightGroupId) {
        this.fightGroupId = fightGroupId;
    }

    private Boolean isSinglepurchase;
 private Long fightGroupId;


    private Double payPrice;

    public OrderBuyNowTuanGouResult(String taxUrl, Double delivery, Member member, Receiver receiver, Goods goods, Integer quantity, String receiveTime, Boolean is_freeMoney, Boolean is_useMiaobi, String miaoBiDesc, List<PriceResult> priceList, String realPrice, String couponPrice, Double[] param, Boolean is_promotion, Double payPrice,Boolean isSinglepurchase,Long fightGroupId,Long tuanGouId) {
        this.taxUrl = taxUrl;
        this.delivery = delivery;
        this.member = member;
        this.receiver = receiver;
        this.goods = goods;
        this.quantity = quantity;
        this.receiveTime = receiveTime;
        this.is_freeMoney = is_freeMoney;
        this.is_useMiaobi = is_useMiaobi;
        this.miaoBiDesc = miaoBiDesc;
        this.priceList = priceList;
        this.realPrice = realPrice;
        this.couponPrice = couponPrice;
        this.param = param;
        this.is_promotion = is_promotion;
        this.payPrice = payPrice;
        this.isSinglepurchase = isSinglepurchase;
        this.fightGroupId = fightGroupId;
        this.tuanGouId = tuanGouId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getTaxUrl() {
        return taxUrl;
    }

    public void setTaxUrl(String taxUrl) {
        this.taxUrl = taxUrl;
    }


    public Double getDelivery() {
        return delivery;
    }

    public void setDelivery(Double delivery) {
        this.delivery = delivery;
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

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Boolean getIs_freeMoney() {
        return is_freeMoney;
    }

    public void setIs_freeMoney(Boolean is_freeMoney) {
        this.is_freeMoney = is_freeMoney;
    }

    public Boolean getIs_useMiaobi() {
        return is_useMiaobi;
    }

    public void setIs_useMiaobi(Boolean is_useMiaobi) {
        this.is_useMiaobi = is_useMiaobi;
    }

    public String getMiaoBiDesc() {
        return miaoBiDesc;
    }

    public void setMiaoBiDesc(String miaoBiDesc) {
        this.miaoBiDesc = miaoBiDesc;
    }

    public List<PriceResult> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceResult> priceList) {
        this.priceList = priceList;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Double[] getParam() {
        return param;
    }

    public void setParam(Double[] param) {
        this.param = param;
    }

    public Boolean getIs_promotion() {
        return is_promotion;
    }

    public void setIs_promotion(Boolean is_promotion) {
        this.is_promotion = is_promotion;
    }
}
