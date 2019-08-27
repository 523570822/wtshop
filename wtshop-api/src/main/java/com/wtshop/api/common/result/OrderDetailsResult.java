package com.wtshop.api.common.result;


import com.wtshop.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "订单结算", description = "OrderDetailsResult")
public class OrderDetailsResult  implements Serializable {

    private String taxUrl;

    private Double couponYunfei;

    private Long[] cartTokens;

    private Double delivery;

    @ApiModelProperty(value = "会员", dataType = "Member")
    private Member member;

    @ApiModelProperty(value = "获取令牌", dataType = "String")
    private String token;

    @ApiModelProperty(value = "收货地址", dataType = "Receiver")
    private Receiver receiver;

    private List<Coupon> codeList;

    private List<Goods> goodsList;

    private String receiveTime;

    private Boolean is_freeMoney;

    private Boolean is_useMiaobi;
    private Boolean is_useMiao;

    public Boolean getIs_useMiao() {
        return is_useMiao;
    }

    public void setIs_useMiao(Boolean is_useMiao) {
        this.is_useMiao = is_useMiao;
    }

    private String miaoBiDesc;

    private Boolean is_Integral;
    private String integralDesc;

    public Boolean getIs_Integral() {
        return is_Integral;
    }

    public void setIs_Integral(Boolean is_Integral) {
        this.is_Integral = is_Integral;
    }

    public String getIntegralDesc() {
        return integralDesc;
    }

    public void setIntegralDesc(String integralDesc) {
        this.integralDesc = integralDesc;
    }

    private Boolean isReturnInsurance;

    private Double returnMoney;

    private String returnCopy;

    private List<PriceResult> priceList;

    private String realPrice;

    private String couponPrice;

    private Double[] param;

    private Boolean is_promotion;

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    private Identifier identifier;

    public OrderDetailsResult(String taxUrl, Double couponYunfei, Long[] cartTokens, Double delivery, Member member, String token, Receiver receiver, List<Coupon> codeList, List<Goods> goodsList, String receiveTime, Boolean is_freeMoney, Boolean is_useMiaobi, Boolean is_useMiao, String miaoBiDesc, Boolean isReturnInsurance, Double returnMoney, String returnCopy, List<PriceResult> priceList, String realPrice, String couponPrice, Double[] param, Boolean is_promotion,Identifier identifier  ,Boolean is_Integral,String  integralDesc  ) {
        this.taxUrl = taxUrl;
        this.couponYunfei = couponYunfei;
        this.cartTokens = cartTokens;
        this.delivery = delivery;
        this.member = member;
        this.token = token;
        this.receiver = receiver;
        this.codeList = codeList;
        this.goodsList = goodsList;
        this.receiveTime = receiveTime;
        this.is_freeMoney = is_freeMoney;
        this.is_useMiaobi = is_useMiaobi;
        this.is_useMiao = is_useMiao;
        this.miaoBiDesc = miaoBiDesc;
        this.isReturnInsurance = isReturnInsurance;
        this.returnMoney = returnMoney;
        this.returnCopy = returnCopy;
        this.priceList = priceList;
        this.realPrice = realPrice;
        this.couponPrice = couponPrice;
        this.param = param;
        this.is_promotion = is_promotion;
        this.identifier = identifier;
        this.is_Integral = is_Integral;
        this.integralDesc = integralDesc;
    }

    public Boolean getIs_promotion() {
        return is_promotion;
    }

    public void setIs_promotion(Boolean is_promotion) {
        this.is_promotion = is_promotion;
    }

    public String getTaxUrl() {
        return taxUrl;
    }

    public void setTaxUrl(String taxUrl) {
        this.taxUrl = taxUrl;
    }

    public Double getCouponYunfei() {
        return couponYunfei;
    }

    public void setCouponYunfei(Double couponYunfei) {
        this.couponYunfei = couponYunfei;
    }

    public Double getDelivery() {
        return delivery;
    }

    public void setDelivery(Double delivery) {
        this.delivery = delivery;
    }

    public Double[] getParam() {
        return param;
    }

    public void setParam(Double[] param) {
        this.param = param;
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

    public Long[] getCartTokens() {
        return cartTokens;
    }

    public void setCartTokens(Long[] cartTokens) {
        this.cartTokens = cartTokens;
    }


    public List<PriceResult> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceResult> priceList) {
        this.priceList = priceList;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public List<Coupon> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Coupon> codeList) {
        this.codeList = codeList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
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

    public Boolean getReturnInsurance() {
        return isReturnInsurance;
    }

    public void setReturnInsurance(Boolean returnInsurance) {
        isReturnInsurance = returnInsurance;
    }

    public Double getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Double returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getReturnCopy() {
        return returnCopy;
    }

    public void setReturnCopy(String returnCopy) {
        this.returnCopy = returnCopy;
    }

}
