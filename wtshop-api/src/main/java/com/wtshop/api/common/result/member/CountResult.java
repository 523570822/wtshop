package com.wtshop.api.common.result.member;

import java.io.Serializable;

/**
 * Created by sq on 2017/9/22.
 */
public class CountResult implements Serializable{

    private Long memberPendingPaymentOrderCount;
    private Long memberPendingShipmentOrderCount;
    private Long memberReceivedOrderCount;
    private Long pendingReviewCount;

    public CountResult(Long memberPendingPaymentOrderCount, Long memberPendingShipmentOrderCount, Long memberReceivedOrderCount, Long pendingReviewCount) {
        this.memberPendingPaymentOrderCount = memberPendingPaymentOrderCount;
        this.memberPendingShipmentOrderCount = memberPendingShipmentOrderCount;
        this.memberReceivedOrderCount = memberReceivedOrderCount;
        this.pendingReviewCount = pendingReviewCount;
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
}
