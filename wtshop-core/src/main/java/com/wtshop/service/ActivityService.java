package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.ehcache.CacheKit;
import com.wtshop.dao.*;
import com.wtshop.model.Activity;
import com.wtshop.model.ActivityProduct;
import com.wtshop.model.FudaiProduct;
import com.wtshop.util.StringUtils;
import net.sf.ehcache.CacheManager;

import java.util.ArrayList;
import java.util.List;

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
    private ActivityProductDao activityProducDao = Enhancer.enhance(ActivityProductDao.class);

    @Override
    public Activity find(Long id) {
        return super.find(id);
    }


    public List<ActivityProduct> queryByActivityId(Long id) {
        return activityProducDao.findByActivityId(id);
    }

    public void saveOrUpdate(List<ActivityProduct> newList, Long fuDaiId) {
        List<ActivityProduct> oldList = activityProducDao.findByActivityId(fuDaiId);
        List<Long> updateIdList = new ArrayList<Long>();

        if (oldList.size() > 0) {//修改保存删除
            for (ActivityProduct detail : newList) {//保存新增产品
                if (!StringUtils.isEmpty(detail.getId())) {
                    updateIdList.add(detail.getId());
                } else {
                //    detail.setRepeatTime(detail.getRepeatTime() * 60);
                    activityProducDao.save(detail);
                }
            }

            for (ActivityProduct detail : oldList) {//删除前端删除产品
                if (!updateIdList.contains(detail.getId())) {
                    activityProducDao.remove(detail);
                }
            }

            for (ActivityProduct updateDetail : oldList) {//修改前段修改产品
                for (ActivityProduct detail : newList) {
                    if (detail.getId().equals(updateDetail.getId())) {
                 //       detail.setRepeatTime(detail.getRepeatTime() * 60);
                        activityProducDao.update(detail);
                    }
                }
            }
        } else {//保存
            for (ActivityProduct detail : newList) {
                detail.setRepeatTime(detail.getRepeatTime() * 60);
                activityProducDao.save(detail);
            }
        }
    }
}
