package com.wtshop.api.common.result.member;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.model.Goods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/18.
 */

@ApiModel(value = "预存款充值", description = "FavoriteListResult")
public class FavoriteListResult implements Serializable{

    @ApiModelProperty(value = "商品", dataType = "Goods")
    private Page<Goods> goods;

    @ApiModelProperty(value = "时间", dataType = "Object")
    private Object period;

    public FavoriteListResult(Page<Goods> goods, Object period) {
        this.goods = goods;
        this.period = period;
    }

    public Page<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Page<Goods> goods) {
        this.goods = goods;
    }

    public Object getPeriod() {
        return period;
    }

    public void setPeriod(Object period) {
        this.period = period;
    }
}
