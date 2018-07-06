package com.wtshop.api.common.result;

import com.wtshop.model.Goods;
import com.wtshop.model.GoodsTheme;

import java.util.List;

/**
 * Created by jobfo on 2017/6/4.
 */
public class Theme {
    private GoodsTheme goodsTheme;

    private List<Goods> goodsList;

    public GoodsTheme getGoodsTheme() {
        return goodsTheme;
    }

    public void setGoodsTheme(GoodsTheme goodsTheme) {
        this.goodsTheme = goodsTheme;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public Theme(GoodsTheme goodsTheme, List<Goods> goodsList) {
        this.goodsTheme = goodsTheme;
        this.goodsList = goodsList;
    }
}
