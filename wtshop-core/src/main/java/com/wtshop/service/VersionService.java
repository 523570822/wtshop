package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.VersionDao;
import com.wtshop.model.Version;

/**
 * Created by sq on 2017/9/8.
 */
public class VersionService extends BaseService<Version>{

    public VersionService(){
        super(Version.class);
    }

    private VersionDao versionDao = Enhancer.enhance(VersionDao.class);


    public Version findNewVersion(){
        return versionDao.findNewVersion();
    }

}
