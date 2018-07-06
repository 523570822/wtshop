package com.wtshop.api.common.result;

import com.wtshop.model.Goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/10/10.
 */
public class CartGoodsResult implements Serializable{

    private String message;

    private Integer activityId;

    private String clickMesssage;


    public CartGoodsResult(String message, Integer activityId, String clickMesssage) {
        this.message = message;
        this.activityId = activityId;
        this.clickMesssage = clickMesssage;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClickMesssage() {
        return clickMesssage;
    }

    public void setClickMesssage(String clickMesssage) {
        this.clickMesssage = clickMesssage;
    }
}
