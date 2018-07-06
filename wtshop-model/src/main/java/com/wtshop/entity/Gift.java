package com.wtshop.entity;

/**
 * Created by Administrator on 2017/7/10.
 */


public class Gift {

    private int index;
    private Long gitfId;
    private String giftName;
    private double probability;

    public Gift(int index, Long gitfId, String giftName, double probability) {
        this.index = index;
        this.gitfId = gitfId;
        this.giftName = giftName;
        this.probability = probability;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getGitfId() {
        return gitfId;
    }

    public void setGitfId(Long gitfId) {
        this.gitfId = gitfId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}