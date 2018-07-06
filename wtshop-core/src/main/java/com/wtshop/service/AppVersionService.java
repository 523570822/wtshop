package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.AppVersionDao;
import com.wtshop.dao.VersionDao;
import com.wtshop.model.AppVersion;
import com.wtshop.model.Version;

/**
 * Created by sq on 2017/9/8.
 */
public class AppVersionService extends BaseService<AppVersion>{

    public AppVersionService(){
        super(AppVersion.class);
    }

    private AppVersionDao versionDao = Enhancer.enhance(AppVersionDao.class);


    public AppVersion checkVersion(String type){
        return versionDao.checkVersion(type);
    }

}
