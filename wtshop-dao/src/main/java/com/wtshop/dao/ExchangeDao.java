package com.wtshop.dao;

import com.wtshop.model.ExchangeProgress;

/**
 * Created by sq on 2017/9/15.
 */
public class ExchangeDao extends BaseDao<ExchangeProgress>{

    public ExchangeDao(){
        super(ExchangeProgress.class);

    }

    /**
     * 获取提现进度
     */
    public ExchangeProgress findProgress(Long progerssId){

        String sql = " select create_date from exchange_progress where deposit_id = "+ progerssId;

        return modelManager.findFirst(sql);

    }

}
