package com.wtshop.dao;

import com.wtshop.model.ReferrerConfig;

/**
 * Created by 蔺哲 on 2017/9/8.
 */
public class ReferrerConfigDao extends BaseDao<ReferrerConfig> {
    public ReferrerConfigDao (){super(ReferrerConfig.class);}

    public ReferrerConfig getConfig(){
        String sql = "select * from referrer_config";
        return modelManager.findFirst(sql);
    }
}
