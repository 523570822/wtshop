package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Record;
import com.wtshop.entity.InterestResult;
import com.wtshop.model.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/7/4.
 */
public class MemberMessageResult {

    @ApiModelProperty(value = "等待付款订单数量", dataType = "Long")
    private Long memberPendingPaymentOrderCount;

    @ApiModelProperty(value = "等待发货订单数量", dataType = "Long")
    private Long memberPendingShipmentOrderCount;

    @ApiModelProperty(value = "等待收货订单数量", dataType = "Long")
    private Long memberReceivedOrderCount;

    @ApiModelProperty(value = "评论数量", dataType = "Long")
    private Long pendingReviewCount;

    @ApiModelProperty(value = "会员", dataType = "Member")
    private Member member;

    private Certificates certificates;

    private  String sign;

    private String birth;

    private String phone;

    private Integer genter;

    private Receiver aDefault;

    private List<Member> interest;

    private List<InterestCategory> interestCategories;

    private List<Member> skin;

    private List<SkinType> skinTypes;

    private Map memberType;

    private Double commission;

    private Double prestore;

    private String price;

    public MemberMessageResult(Long memberPendingPaymentOrderCount, Long memberPendingShipmentOrderCount, Long memberReceivedOrderCount, Long pendingReviewCount, Member member, Certificates certificates, String sign, String birth, String phone, Integer genter, Receiver aDefault, List<Member> interest, List<InterestCategory> interestCategories, List<Member> skin, List<SkinType> skinTypes, Map memberType, Double commission, Double prestore, String price) {
        this.memberPendingPaymentOrderCount = memberPendingPaymentOrderCount;
        this.memberPendingShipmentOrderCount = memberPendingShipmentOrderCount;
        this.memberReceivedOrderCount = memberReceivedOrderCount;
        this.pendingReviewCount = pendingReviewCount;
        this.member = member;
        this.certificates = certificates;
        this.sign = sign;
        this.birth = birth;
        this.phone = phone;
        this.genter = genter;
        this.aDefault = aDefault;
        this.interest = interest;
        this.interestCategories = interestCategories;
        this.skin = skin;
        this.skinTypes = skinTypes;
        this.memberType = memberType;
        this.commission = commission;
        this.prestore = prestore;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getPrestore() {
        return prestore;
    }

    public void setPrestore(Double prestore) {
        this.prestore = prestore;
    }

    public Map getMemberType() {
        return memberType;
    }

    public void setMemberType(Map memberType) {
        this.memberType = memberType;
    }

    public List<Member> getInterest() {
        return interest;
    }

    public void setInterest(List<Member> interest) {
        this.interest = interest;
    }

    public List<Member> getSkin() {
        return skin;
    }

    public void setSkin(List<Member> skin) {
        this.skin = skin;
    }

    public Long getMemberPendingPaymentOrderCount() {
        return memberPendingPaymentOrderCount;
    }

    public void setMemberPendingPaymentOrderCount(Long memberPendingPaymentOrderCount) {
        this.memberPendingPaymentOrderCount = memberPendingPaymentOrderCount;
    }

    public Long getMemberPendingShipmentOrderCount() {
        return memberPendingShipmentOrderCount;
    }

    public void setMemberPendingShipmentOrderCount(Long memberPendingShipmentOrderCount) {
        this.memberPendingShipmentOrderCount = memberPendingShipmentOrderCount;
    }

    public Long getMemberReceivedOrderCount() {
        return memberReceivedOrderCount;
    }

    public void setMemberReceivedOrderCount(Long memberReceivedOrderCount) {
        this.memberReceivedOrderCount = memberReceivedOrderCount;
    }

    public Long getPendingReviewCount() {
        return pendingReviewCount;
    }

    public void setPendingReviewCount(Long pendingReviewCount) {
        this.pendingReviewCount = pendingReviewCount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Certificates getCertificates() {
        return certificates;
    }

    public void setCertificates(Certificates certificates) {
        this.certificates = certificates;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Integer getGenter() {
        return genter;
    }

    public void setGenter(Integer genter) {
        this.genter = genter;
    }

    public Receiver getaDefault() {
        return aDefault;
    }

    public void setaDefault(Receiver aDefault) {
        this.aDefault = aDefault;
    }


    public List<InterestCategory> getInterestCategories() {
        return interestCategories;
    }

    public void setInterestCategories(List<InterestCategory> interestCategories) {
        this.interestCategories = interestCategories;
    }


    public List<SkinType> getSkinTypes() {
        return skinTypes;
    }

    public void setSkinTypes(List<SkinType> skinTypes) {
        this.skinTypes = skinTypes;
    }
}
