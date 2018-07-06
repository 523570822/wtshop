package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.TargetPathDao;
import com.wtshop.model.TargetPath;

import java.util.List;

/**
 * Created by admin on 2017/6/20.
 */
public class TargetPathService extends BaseService<TargetPath>{

    public TargetPathService(Class<TargetPath> modelClass) {
        super(modelClass);
    }
    public TargetPathService() {
        super(TargetPath.class);
    }
    private TargetPathDao targetPathDao = Enhancer.enhance(TargetPathDao.class);
    public List<TargetPath> findByUrltype(int urltype) {
        return targetPathDao.findByUrltype(urltype);
    }

    public Page<TargetPath> findById(Integer tarId, Pageable pageable) {
        return targetPathDao.findById(tarId,pageable);
    }
    public Page<TargetPath> findLevel(Pageable pageable) {
        return targetPathDao.findLevel(pageable);
    }
    public TargetPath findMaxUrlType(){
        return targetPathDao.findMaxUrlType();
    }
}
