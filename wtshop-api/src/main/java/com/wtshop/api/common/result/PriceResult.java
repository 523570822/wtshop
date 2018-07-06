package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * Created by sq on 2017/8/31.
 */
public class PriceResult implements Serializable{

    private String name ;

    private String price ;

    public PriceResult(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
