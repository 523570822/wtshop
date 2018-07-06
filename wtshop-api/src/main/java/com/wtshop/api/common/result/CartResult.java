package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * Created by sq on 2017/9/18.
 */
public class CartResult implements Serializable{


    private String message;

    private Double subtract;

    public CartResult(String message, Double subtract) {
        this.message = message;
        this.subtract = subtract;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getSubtract() {
        return subtract;
    }

    public void setSubtract(Double subtract) {
        this.subtract = subtract;
    }
}
