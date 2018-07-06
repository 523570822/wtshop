package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * Created by sq on 2017/7/14.
 * 快递公司id + name + 运费
 */
public class Delivery implements Serializable{

    private Long id ;

    private String name ;

    private Double price ;

    public Delivery(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
