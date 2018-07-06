package com.wtshop.dao;

import com.wtshop.model.ReverseAuctionHistroy;

/**
 * Created by Administrator on 2017/6/5.
 */
public class ReverseAuctionHistroyDao extends BaseDao<ReverseAuctionHistroy> {
    /**
     * 构造方法
     *
     * @param entityClass
     */
    public ReverseAuctionHistroyDao(Class<ReverseAuctionHistroy> entityClass) {
        super(entityClass);
    }
    public ReverseAuctionHistroyDao() {
        super(ReverseAuctionHistroy.class);
    }


}
