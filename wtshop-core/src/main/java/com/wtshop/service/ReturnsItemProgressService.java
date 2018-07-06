package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ReturnsItemProgressDao;
import com.wtshop.model.ReturnsItemProgress;

import java.util.List;

/**
 * Created by sq on 2017/7/5.
 */
public class ReturnsItemProgressService extends BaseService<ReturnsItemProgress>{

    /**
     * 构造方法
     */
    public ReturnsItemProgressService() {
        super(ReturnsItemProgress.class);
    }


    private ReturnsItemProgressDao returnsItemProgressDao = Enhancer.enhance(ReturnsItemProgressDao.class);


    /**
     *查询进度
     */
    public List<ReturnsItemProgress> findProgressById(Long  id ){

        return returnsItemProgressDao.findProgressById(id);

    }

    /**
     * 根据退货项的id 获取集合
     */
    public List<ReturnsItemProgress> findByReturnId(Long returnId){
        return returnsItemProgressDao.findByReturnId(returnId);
    }

}
