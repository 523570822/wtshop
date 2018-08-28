package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.ehcache.CacheKit;
import com.wtshop.Filter;
import com.wtshop.dao.ActivityDao;
import com.wtshop.dao.ActivityProductDao;
import com.wtshop.dao.RaffleDao;
import com.wtshop.model.Activity;
import com.wtshop.model.ActivityProduct;
import com.wtshop.model.Raffle;
import com.wtshop.util.StringUtils;
import net.sf.ehcache.CacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Service - 货品
 */
public class RaffleService extends BaseService<Raffle> {

    /**
     * 构造方法
     */
    public RaffleService() {
        super(Raffle.class);
    }

    private CacheManager cacheManager = CacheKit.getCacheManager();
    private RaffleDao raffleDao = Enhancer.enhance(RaffleDao.class);


    @Override
    public Raffle find(Long id) {
        return super.find(id);
    }


    public int findByActivityId(Long id) {

        Filter filter=new Filter();
        filter.setProperty("activity_id");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue(id);
        long sm = raffleDao.count(filter);
                return (int)sm;

    }
    public List<Raffle> findByActivityIdList(Long id) {


        List<Raffle> raffle = raffleDao.findListBySql("select * from raffle where activity_id= "+4 +" ORDER BY activityProductId desc");
        return raffle;

    }
    public int findByActivityIdAndMem(Long activityId,Long memberId) {

        Filter filter=new Filter();
        filter.setProperty("activity_id");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue(activityId);


        Filter filter1=new Filter();
        filter1.setProperty("member_id");
        filter1.setOperator(Filter.Operator.eq);
        filter1.setValue(memberId);
        long sm = raffleDao.count(filter,filter1);
        return (int)sm;

    }
}
