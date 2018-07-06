package com.wtshop.entity;

/**
 * Created by sq on 2017/9/1.
 */
public class InterestResult {

    private Integer id;

    private String name;

    public InterestResult(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
