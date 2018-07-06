package com.wtshop.dao;

import com.wtshop.model.GoodsPromotion;

/**
 * Created by Administrator on 2017/8/30.
 */
public class GoodsPromotionDao extends  BaseDao<GoodsPromotion> {

    public  GoodsPromotionDao(){
        super(GoodsPromotion.class);
    }

    /**
     * 查找商品是否存在
     */
    public GoodsPromotion findByGoodsId(Long goodsId){
        if(goodsId == null){
            return null;
        }else {
            String sql = " SELECT * FROM goods_promotion where promotions = 5 and goods = " + goodsId;
            return modelManager.findFirst(sql);
        }
    }

    public GoodsPromotion findPromitByGoodsId(Long goodsId){
        if(goodsId == null){
            return null;
        }else {
            String sql = " SELECT * FROM goods_promotion where  goods = " + goodsId;
            return modelManager.findFirst(sql);
        }
    }


    public GoodsPromotion findByCartId(Long cartId){
        String sql = " SELECT * FROM product p  LEFT JOIN cart_item c ON p.id = c.product_id WHERE c.cart_id = " + cartId;
        sql += " ORDER BY c.modify_date DESC LIMIT 1 ";

        return modelManager.findFirst(sql);

    }


}
