package com.wtshop.dao;

import com.wtshop.model.AppVersion;
import com.wtshop.model.Version;

/**
 * Created by sq on 2017/9/8.
 */
public class AppVersionDao extends BaseDao<AppVersion>{

    public AppVersionDao(){
        super(AppVersion.class);
    }


    /**
     * 获取最新的版本号
     */
    public AppVersion checkVersion(String type){
        String sql = " select * from app_version where app_flat = '" +type + "'  order by app_version desc limit 1 ";
        return modelManager.findFirst(sql);

    }
}
