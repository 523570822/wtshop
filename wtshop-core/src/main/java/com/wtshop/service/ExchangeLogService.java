package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ExchangeLogDao;
import com.wtshop.model.ExchangeLog;

/**
 * Created by sq on 2017/11/2.
 */
public class ExchangeLogService extends BaseService<ExchangeLog> {

    private ExchangeLogDao exchangeLogDao = Enhancer.enhance(ExchangeLogDao.class);

    public ExchangeLogService(){
        super(ExchangeLog.class);
    }

    /**
     * 根据支付单号查询
     */
    public ExchangeLog findByOrderNo(String orderNo){
       return exchangeLogDao.findByOrderNo(orderNo);
    }


}
