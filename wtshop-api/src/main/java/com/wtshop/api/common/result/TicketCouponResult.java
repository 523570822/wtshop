package com.wtshop.api.common.result;

import com.wtshop.model.Ticketreceive;

import java.io.Serializable;

/**
 * Created by sq on 2017/8/14.
 */
public class TicketCouponResult implements Serializable{

    private Ticketreceive ticketreceive;

    private Long count;

    public TicketCouponResult(Ticketreceive ticketreceive, Long count) {
        this.ticketreceive = ticketreceive;
        this.count = count;
    }

    public Ticketreceive getTicketreceive() {
        return ticketreceive;
    }

    public void setTicketreceive(Ticketreceive ticketreceive) {
        this.ticketreceive = ticketreceive;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
