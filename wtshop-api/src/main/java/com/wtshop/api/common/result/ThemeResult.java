package com.wtshop.api.common.result;

import com.wtshop.model.Goods;
import com.wtshop.model.GoodsTheme;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sq on 2017/7/12.
 */
public class ThemeResult implements Serializable {

    private GoodsTheme goodsTheme;

    private List<Goods> goodss;

    public ThemeResult(GoodsTheme goodsTheme, List<Goods> goodss) {
        this.goodsTheme = goodsTheme;
        this.goodss = goodss;
    }

    public GoodsTheme getGoodsTheme() {
        return goodsTheme;
    }

    public void setGoodsTheme(GoodsTheme goodsTheme) {
        this.goodsTheme = goodsTheme;
    }

    public List<Goods> getGoodss() {
        return goodss;
    }

    public void setGoodss(List<Goods> goodss) {
        this.goodss = goodss;
    }
}
