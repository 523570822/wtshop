package com.wtshop.dao;

import com.wtshop.model.Flashsale;
import com.wtshop.model.FlashsaleDetail;

import java.util.List;

/**
 * Created by 蔺哲 on 2017/5/17.
 */
public class FlashsaleDao extends BaseDao<Flashsale> {
    public FlashsaleDao(){super(Flashsale.class);}
    public List<FlashsaleDetail> queryDetailById(Long id){
        String sql = "select * from flashsale_detail where flashsale_id = " +id;
        return FlashsaleDetail.dao.find(sql);
    }


    /**
     * 获取当前限时抢购的商品信息
     */
    public Flashsale findGoodsByType(){
        String sql = " SELECT g.id,g.name,g.price,g.market_price,g.image from flashsale f LEFT JOIN flashsale_detail d on f.id=d.flashsale_id LEFT JOIN product p on p.id = d.product_id LEFT JOIN goods g on g.id = p.goods_id WHERE f.type = 1";
        return modelManager.findFirst(sql);
    }



}
