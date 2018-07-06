package com.wtshop.api.common.result;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Consultation;
import com.wtshop.model.Goods;
import com.wtshop.model.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "货物搜索", description = "GoodsSearchResult")
public class GoodsSearchResult  implements Serializable {

    @ApiModelProperty(value = "排序类型", dataType = "Array")
    private Object[] orderTypes;

    @ApiModelProperty(value = "排序", dataType = "Object")
    private Object orderType;

    @ApiModelProperty(value = "关键字", dataType = "String")
    private String goodsKeyword;

    @ApiModelProperty(value = "最低价格", dataType = "BigDecimal")
    private BigDecimal startPrice;

    @ApiModelProperty(value = "最高价格", dataType = "BigDecimal")
    private BigDecimal endPrice;

    @ApiModelProperty(value = "商品分页", dataType = "Goods")
    private List<Goods> goods;

    public GoodsSearchResult(Object[] orderTypes, Object orderType, String goodsKeyword, BigDecimal startPrice, BigDecimal endPrice, List<Goods> goods) {
        this.orderTypes = orderTypes;
        this.orderType = orderType;
        this.goodsKeyword = goodsKeyword;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.goods = goods;
    }

    public Object[] getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(Object[] orderTypes) {
        this.orderTypes = orderTypes;
    }

    public Object getOrderType() {
        return orderType;
    }

    public void setOrderType(Object orderType) {
        this.orderType = orderType;
    }

    public String getGoodsKeyword() {
        return goodsKeyword;
    }

    public void setGoodsKeyword(String goodsKeyword) {
        this.goodsKeyword = goodsKeyword;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}
