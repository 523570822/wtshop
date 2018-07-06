package com.wtshop.api.common.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/8/14.
 */
public class CouponListResult implements Serializable{

    private List<TicketCouponResult> ticketCouponResults ;

    private List<CouponCodeResult> couponLists;

    public CouponListResult(List<TicketCouponResult> ticketCouponResults, List<CouponCodeResult> couponLists) {
        this.ticketCouponResults = ticketCouponResults;
        this.couponLists = couponLists;
    }

    public List<TicketCouponResult> getTicketCouponResults() {
        return ticketCouponResults;
    }

    public void setTicketCouponResults(List<TicketCouponResult> ticketCouponResults) {
        this.ticketCouponResults = ticketCouponResults;
    }

    public List<CouponCodeResult> getCouponLists() {
        return couponLists;
    }

    public void setCouponLists(List<CouponCodeResult> couponLists) {
        this.couponLists = couponLists;
    }
}
