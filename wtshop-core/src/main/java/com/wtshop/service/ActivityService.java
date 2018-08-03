package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.ehcache.CacheKit;
import com.wtshop.dao.ActivityDao;
import com.wtshop.dao.GoodsDao;
import com.wtshop.dao.ProductDao;
import com.wtshop.model.Activity;
import net.sf.ehcache.CacheManager;

/**
 * Service - 货品
 */
public class ActivityService extends BaseService<Activity> {

    /**
     * 构造方法
     */
    public ActivityService() {
        super(Activity.class);
    }

    private CacheManager cacheManager = CacheKit.getCacheManager();
    private ActivityDao activityDao = Enhancer.enhance(ActivityDao.class);

    @Override
    public Activity find(Long id) {
        return super.find(id);
    }
}
