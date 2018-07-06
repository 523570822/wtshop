package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.GoodsPromotionDao;
import com.wtshop.model.GoodsPromotion;

/**
 * Created by sq on 2017/10/10.
 */
public class GoodsPromotionService extends BaseService<GoodsPromotion>{

    public GoodsPromotionService(){
        super(GoodsPromotion.class);
    }

    private GoodsPromotionDao goodsPromotionDao = Enhancer.enhance(GoodsPromotionDao.class);


    /**
     * 查找商品是否满减
     */
    public GoodsPromotion findByGoodsId(Long goodsId){
        return goodsPromotionDao.findByGoodsId(goodsId);
    }

    /**
     * 查找商品是否促销
     */
    public GoodsPromotion findPromitByGoodsId(Long goodsId){
        return goodsPromotionDao.findPromitByGoodsId(goodsId);
    }

    /**
     * 判断最后一件商品是否是满减
     */
    public  GoodsPromotion findByCartId(Long cartId){
        return goodsPromotionDao.findByCartId(cartId);
    }

}
