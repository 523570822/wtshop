package com.wtshop.entity;

/**
 * Created by Administrator on 2017/7/28.
 */
public class DaoPaiCfg {

    private   int  daopaiMaxPayTime;

    public int getDaopaiMaxPayTime() {
        return daopaiMaxPayTime;
    }

    public void setDaopaiMaxPayTime(int daopaiMaxPayTime) {
        this.daopaiMaxPayTime = daopaiMaxPayTime;
    }

    public int getDownPriceSpace() {
        return downPriceSpace;
    }

    public void setDownPriceSpace(int downPriceSpace) {
        this.downPriceSpace = downPriceSpace;
    }

    public int getDownPriceNum() {
        return downPriceNum;
    }

    public void setDownPriceNum(int downPriceNum) {
        this.downPriceNum = downPriceNum;
    }

    private int  downPriceSpace;
    private int downPriceNum;
}
