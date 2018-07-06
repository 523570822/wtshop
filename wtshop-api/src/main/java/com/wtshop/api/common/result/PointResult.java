package com.wtshop.api.common.result;

import java.io.Serializable;

/**
 * Created by sq on 2017/9/19.
 */
public class PointResult implements Serializable {


    private String desc;

    private String image;

    private String price;


    public PointResult(String desc, String image, String price) {
        this.desc = desc;
        this.image = image;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
