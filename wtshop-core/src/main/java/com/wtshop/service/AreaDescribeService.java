package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.AreaDescribeDao;
import com.wtshop.model.AreaDescribe;


/**
 * Created by 蔺哲 on 2017/8/16.
 */
public class AreaDescribeService extends BaseService<AreaDescribe> {
    public AreaDescribeService(){super(AreaDescribe.class);}
    private AreaDescribeDao areaDescribeDao = Enhancer.enhance(AreaDescribeDao.class);
    public AreaDescribe findByAreaId(Long areaId){
        return areaDescribeDao.findByAreaId(areaId);
    }
}
