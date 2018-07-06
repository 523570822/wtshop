package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ActIntroduceDao;
import com.wtshop.model.ActIntroduce;

/**
 * Created by Administrator on 2017/8/21.
 */
public class ActIntroduceService extends  BaseService<ActIntroduce> {
    private ActIntroduceDao actIntroduceDao= Enhancer.enhance(ActIntroduceDao.class);
    public ActIntroduceService(){
        super(ActIntroduce.class);
    }

    public ActIntroduce getDetailsByType(int type){
                return  actIntroduceDao.getDetailsByType(type);
    }
}
