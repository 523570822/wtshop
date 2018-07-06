package com.wtshop.api.common.result.member;

import com.wtshop.model.Certificates;
import com.wtshop.model.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/17.
 */
@ApiModel(value = "会员首页", description = "MemberIndexResult")
public class MemberIndexResult implements Serializable{

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

    private String birth;

    public MemberIndexResult(Long memberPendingPaymentOrderCount, Long memberPendingShipmentOrderCount, Long memberReceivedOrderCount, Long pendingReviewCount, Member member, Certificates certificates, String birth) {
        this.memberPendingPaymentOrderCount = memberPendingPaymentOrderCount;
        this.memberPendingShipmentOrderCount = memberPendingShipmentOrderCount;
        this.memberReceivedOrderCount = memberReceivedOrderCount;
        this.pendingReviewCount = pendingReviewCount;
        this.member = member;
        this.certificates = certificates;
        this.birth = birth;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
