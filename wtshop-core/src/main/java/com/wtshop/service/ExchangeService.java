package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ExchangeDao;
import com.wtshop.model.ExchangeProgress;

/**
 * Created by sq on 2017/9/15.
 */
public class ExchangeService extends BaseService<ExchangeProgress> {

    public ExchangeService(){
        super(ExchangeProgress.class);
    }

    private ExchangeDao exchangeDao = Enhancer.enhance(ExchangeDao.class);

    /**
     * 获取提现进度
     */
    public ExchangeProgress findProgress(Long progressId){
        return exchangeDao.findProgress(progressId);
    }

}
