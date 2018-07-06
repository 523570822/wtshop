package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.MiaoBiGoodsDao;
import com.wtshop.model.MiaobiGoods;

/**
 * Created by 蔺哲 on 2017/9/12.
 */
public class MiaoBiGoodsService extends BaseService<MiaobiGoods> {
    public MiaoBiGoodsService(){super(MiaobiGoods.class);}
    private MiaoBiGoodsDao miaoBiGoodsDao = Enhancer.enhance(MiaoBiGoodsDao.class);

    /**
     * 我的 喵币商城商品
     * @param pageNumber
     * @param pageSizes
     * @return
     */
    public Page findByPage(int pageNumber,int pageSizes){
        return miaoBiGoodsDao.findByPage(pageNumber,pageSizes);
    }

    /**
     * cms喵币商品列表
     * @param pageable
     * @return
     */
    public Page findByPage(Pageable pageable){
        return miaoBiGoodsDao.findList(pageable);
    }
}
