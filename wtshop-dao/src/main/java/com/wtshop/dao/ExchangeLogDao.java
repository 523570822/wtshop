package com.wtshop.dao;

import com.wtshop.model.ExchangeLog;

/**
 * Created by sq on 2017/11/2.
 */
public class ExchangeLogDao extends BaseDao<ExchangeLog>{

    public ExchangeLogDao(){
        super(ExchangeLog.class);
    }

    /**
     * 根据支付单号查询
     */
    public ExchangeLog findByOrderNo(String orderNo){

        if(orderNo == null){
            return null;
        }else {
            String sql = "SELECT * FROM exchange_log WHERE orderNo = " + orderNo;
            return modelManager.findFirst(sql);
        }


    }

}
