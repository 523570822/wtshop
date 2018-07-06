package com.wtshop.api.common.result;

import com.wtshop.model.Ad;
import com.wtshop.model.AdPosition;
import com.wtshop.model.Goods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weitong on 17/5/15.
 */
@ApiModel(value = "首页信息", description = "IndexResult")
public class IndexResult implements Serializable{

    private List<Ad> adList;

    private List<Ad> fudaiList;

    private List<Ad> vipList;

    private List<Goods> goodsList;

    private List<Goods> newGoodsList;

    private List<Goods> findCharactersList;

    private List<Ad> newGoodsAdList;

    private List<Ad> charactersAdList;


    public IndexResult(List<Ad> adList, List<Ad> fudaiList, List<Ad> vipList, List<Goods> goodsList, List<Goods> newGoodsList, List<Goods> findCharactersList, List<Ad> newGoodsAdList, List<Ad> charactersAdList) {
        this.adList = adList;
        this.fudaiList = fudaiList;
        this.vipList = vipList;
        this.goodsList = goodsList;
        this.newGoodsList = newGoodsList;
        this.findCharactersList = findCharactersList;
        this.newGoodsAdList = newGoodsAdList;
        this.charactersAdList = charactersAdList;
    }


    public List<Ad> getNewGoodsAdList() {
        return newGoodsAdList;
    }

    public void setNewGoodsAdList(List<Ad> newGoodsAdList) {
        this.newGoodsAdList = newGoodsAdList;
    }

    public List<Ad> getCharactersAdList() {
        return charactersAdList;
    }

    public void setCharactersAdList(List<Ad> charactersAdList) {
        this.charactersAdList = charactersAdList;
    }

    public List<Ad> getFudaiList() {
        return fudaiList;
    }

    public void setFudaiList(List<Ad> fudaiList) {
        this.fudaiList = fudaiList;
    }

    public List<Ad> getVipList() {
        return vipList;
    }

    public void setVipList(List<Ad> vipList) {
        this.vipList = vipList;
    }

    public List<Goods> getNewGoodsList() {
        return newGoodsList;
    }

    public void setNewGoodsList(List<Goods> newGoodsList) {
        this.newGoodsList = newGoodsList;
    }

    public List<Goods> getFindCharactersList() {
        return findCharactersList;
    }

    public void setFindCharactersList(List<Goods> findCharactersList) {
        this.findCharactersList = findCharactersList;
    }

    public List<Ad> getAdList() {
        return adList;
    }

    public void setAdList(List<Ad> adList) {
        this.adList = adList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

}
