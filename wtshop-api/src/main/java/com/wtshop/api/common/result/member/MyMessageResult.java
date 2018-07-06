package com.wtshop.api.common.result.member;

import java.io.Serializable;

/**
 * Created by sq on 2017/8/23.
 */
public class MyMessageResult implements Serializable{

    private Boolean isSystemMessage;

    private Boolean isOrderMessage;

    private Boolean isStaffMessage;


    public MyMessageResult(Boolean isSystemMessage, Boolean isOrderMessage, Boolean isStaffMessage) {
        this.isSystemMessage = isSystemMessage;
        this.isOrderMessage = isOrderMessage;
        this.isStaffMessage = isStaffMessage;
    }

    public Boolean getSystemMessage() {
        return isSystemMessage;
    }

    public void setSystemMessage(Boolean systemMessage) {
        isSystemMessage = systemMessage;
    }

    public Boolean getOrderMessage() {
        return isOrderMessage;
    }

    public void setOrderMessage(Boolean orderMessage) {
        isOrderMessage = orderMessage;
    }

    public Boolean getStaffMessage() {
        return isStaffMessage;
    }

    public void setStaffMessage(Boolean staffMessage) {
        isStaffMessage = staffMessage;
    }
}
