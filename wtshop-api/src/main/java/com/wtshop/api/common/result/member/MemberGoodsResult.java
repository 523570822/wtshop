package com.wtshop.api.common.result.member;

import com.wtshop.model.Goods;
import com.wtshop.model.Information;
import java.io.Serializable;

/**
 * Created by sq on 2017/9/12.
 */
public class MemberGoodsResult implements Serializable{


    private Information information ;

    private Goods goods;

    public MemberGoodsResult(Information information, Goods goods) {
        this.information = information;
        this.goods = goods;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
