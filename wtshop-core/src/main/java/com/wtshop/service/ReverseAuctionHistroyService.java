package com.wtshop.service;


import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ReverseAuctionHistroyDao;
import com.wtshop.model.ReverseAuctionHistroy;

/**
 * Created by Administrator on 2017/6/6.
 */
public class ReverseAuctionHistroyService extends BaseService<ReverseAuctionHistroy> {

    private ReverseAuctionHistroyDao reverseauctionHistroyDao = Enhancer.enhance(ReverseAuctionHistroyDao.class);

    public ReverseAuctionHistroyService() {
        super(ReverseAuctionHistroy.class);
    }

}
