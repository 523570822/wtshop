package com.wtshop.api.common.result;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.Json;
import com.wtshop.entity.*;
import com.wtshop.entity.AreaResult;
import com.wtshop.model.Area;
import com.wtshop.model.Brand;
import com.wtshop.model.Effect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/9/4.
 */
public class SearchResult implements Serializable{

    private JSONObject zonghe;

    private JSONObject price;

    private List<Brand> brandList;

    private List<Effect> effectList;

    private List<Area> areaList;

    private Map<String, List> brandSort;

    private Map<String, List> effectSort;


    public SearchResult(JSONObject zonghe, JSONObject price, List<Brand> brandList, List<Effect> effectList, List<Area> areaList, Map<String, List> brandSort, Map<String, List> effectSort) {
        this.zonghe = zonghe;
        this.price = price;
        this.brandList = brandList;
        this.effectList = effectList;
        this.areaList = areaList;
        this.brandSort = brandSort;
        this.effectSort = effectSort;
    }

    public Map<String, List> getBrandSort() {
        return brandSort;
    }

    public void setBrandSort(Map<String, List> brandSort) {
        this.brandSort = brandSort;
    }

    public Map<String, List> getEffectSort() {
        return effectSort;
    }

    public void setEffectSort(Map<String, List> effectSort) {
        this.effectSort = effectSort;
    }

    public JSONObject getZonghe() {
        return zonghe;
    }

    public void setZonghe(JSONObject zonghe) {
        this.zonghe = zonghe;
    }

    public JSONObject getPrice() {
        return price;
    }

    public void setPrice(JSONObject price) {
        this.price = price;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public List<Effect> getEffectList() {
        return effectList;
    }

    public void setEffectList(List<Effect> effectList) {
        this.effectList = effectList;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }
}