package com.wtshop.service;


import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ReverseAuctionHistroyDao;
import com.wtshop.dao.ReverseAuctionOrderDao;
import com.wtshop.model.ReverseAuctionHistroy;
import com.wtshop.model.ReverseAuctionOrder;
import com.wtshop.util.ReadProper;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/6.
 */
public class ReverseAuctionOrderService extends BaseService<ReverseAuctionOrder> {

    private ReverseAuctionOrderDao reverseAuctionOrderDao = Enhancer.enhance(ReverseAuctionOrderDao.class);

    public ReverseAuctionOrderService() {
        super(ReverseAuctionOrder.class);
    }

}
