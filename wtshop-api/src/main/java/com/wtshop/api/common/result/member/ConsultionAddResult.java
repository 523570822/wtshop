package com.wtshop.api.common.result.member;

import com.wtshop.model.Goods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sq on 2017/5/16.
 */
@ApiModel(value = "咨询增加", description = "ConsultionAddResult")
public class ConsultionAddResult implements Serializable {

    @ApiModelProperty(value = "商品", dataType = "Goods")
    private Goods goods;

    public ConsultionAddResult(Goods goods) {
        this.goods = goods;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
