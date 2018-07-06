package com.wtshop.dao;

import com.wtshop.model.Version;

/**
 * Created by sq on 2017/9/8.
 */
public class VersionDao extends BaseDao<Version>{

    public VersionDao(){
        super(Version.class);
    }


    /**
     * 获取最新的版本号
     */
    public Version findNewVersion(){
        String sql = " select * from version order by create_date desc limit 1 ";
        return modelManager.findFirst(sql);

    }
}
