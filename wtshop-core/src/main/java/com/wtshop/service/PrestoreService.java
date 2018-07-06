package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.PrestoreDao;
import com.wtshop.model.Prestore;

import java.util.List;

/**
 * Created by sq on 2017/9/15.
 */
public class PrestoreService extends BaseService<Prestore>{


    public PrestoreService(){
        super(Prestore.class);
    }

    private PrestoreDao prestoreDao = Enhancer.enhance(PrestoreDao.class);

    /**
     * 查询店铺的预存款记录
     */

    public List<Prestore> findList(Long organId){
        return prestoreDao.findList(organId);
    }

}
