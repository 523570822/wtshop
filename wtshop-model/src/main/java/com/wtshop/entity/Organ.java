package com.wtshop.entity;

/**
 * Created by sq on 2017/8/31.
 */
public class Organ {

    public String name;

    public String id;

    public String image;

    public Organ(String name, String id, String image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public Organ() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
