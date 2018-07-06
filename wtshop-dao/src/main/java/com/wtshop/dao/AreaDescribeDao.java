package com.wtshop.dao;

import com.wtshop.model.AreaDescribe;

/**
 * Created by 蔺哲 on 2017/8/16.
 */
public class AreaDescribeDao extends BaseDao<AreaDescribe> {
    public AreaDescribeDao(){super(AreaDescribe.class);}
    public AreaDescribe findByAreaId(Long areaId){
        String sql = "SELECT * from area_describe where areaId=" + areaId;
        return modelManager.findFirst(sql);
    }

}
