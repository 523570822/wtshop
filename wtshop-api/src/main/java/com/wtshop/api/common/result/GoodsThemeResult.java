package com.wtshop.api.common.result;

import com.wtshop.model.Ad;
import com.wtshop.model.GoodsTheme;
import com.wtshop.model.ThemeProduct;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sq on 2017/5/25.
 */
public class GoodsThemeResult implements Serializable{

    private GoodsTheme goodsTheme;

    private List<ThemeProduct> themeProductList;

    private List<Ad> adList;

    private HashMap<String, Object> hashMap ;


    public GoodsThemeResult(GoodsTheme goodsTheme, List<ThemeProduct> themeProductList) {
        this.goodsTheme = goodsTheme;
        this.themeProductList = themeProductList;
    }

    public GoodsThemeResult(List<Ad> adList, HashMap<String, Object> hashMap) {
        this.adList = adList;
        this.hashMap = hashMap;
    }

    public List<Ad> getAdList() {
        return adList;
    }

    public void setAdList(List<Ad> adList) {
        this.adList = adList;
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

    public GoodsTheme getGoodsTheme() {
        return goodsTheme;
    }

    public void setGoodsTheme(GoodsTheme goodsTheme) {
        this.goodsTheme = goodsTheme;
    }

    public List<ThemeProduct> getThemeProductList() {
        return themeProductList;
    }

    public void setThemeProductList(List<ThemeProduct> themeProductList) {
        this.themeProductList = themeProductList;
    }
}
