package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.VipGoodsHistoryDao;
import com.wtshop.model.Member;
import com.wtshop.model.VipgoodsHistory;

/**
 * Created by sq on 2017/9/1.
 */
public class VipGoodsHistoryService extends BaseService<VipgoodsHistory>{

    public VipGoodsHistoryService() {
        super(VipgoodsHistory.class);
    }

    private VipGoodsHistoryDao vipGoodsHistoryDao = Enhancer.enhance(VipGoodsHistoryDao.class);



    /**
     * vip商品置换记录
     */
     public Page<VipgoodsHistory> findpage(Member member , Pageable pageable){
         return vipGoodsHistoryDao.findPage(member ,pageable);
     }


    /**
     * 某个商品的消费信息
     */
    public Page<Record> findMemList(Pageable pageable,Long goodsId){
        return vipGoodsHistoryDao.findMemList(pageable,goodsId);
    }



}
