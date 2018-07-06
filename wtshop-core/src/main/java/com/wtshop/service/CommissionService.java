package com.wtshop.service;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.CommissionDao;
import com.wtshop.dao.MrmfShopDao;
import com.wtshop.model.CommissionHistory;
import com.wtshop.model.Member;
import com.wtshop.model.MrmfShop;

import java.util.Date;
import java.util.List;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/9/6.
 */
public class CommissionService extends BaseService<CommissionHistory>{

    public CommissionService() {
        super(CommissionHistory.class);
    }

    private CommissionDao commissionDao = enhance(CommissionDao.class);
    private MrmfShopDao mrmfShopDao = enhance(MrmfShopDao.class);
    /**
     * 查询佣金记录
     */
    public Page<CommissionHistory> findHistoryList(Member staff,Date startDate, Date endDate, Pageable pageable, Integer type){
        MrmfShop mrmfShop = mrmfShopDao.findByMemberId(staff.getId());
        if(mrmfShop != null){
            return commissionDao.findHistoryLists(mrmfShop, startDate, endDate, pageable, 1);
        }
        return commissionDao.findHistoryList(staff, startDate, endDate, pageable, 0);

    }

    /**
     * 获取店铺技师
     */

    public Page<CommissionHistory> findStaff(Long organId, Date startDate, Date endDate, Pageable pageable, String staffName){
        return commissionDao.findStaff(organId ,startDate, endDate, pageable, staffName);
    }


    /**
     * 技师明细
     */

    public Page<CommissionHistory> findHistoryList( Date startDate, Date endDate, Pageable pageable, String staffName){

        return commissionDao.findHistoryList( startDate, endDate, pageable, staffName);

    }

    public List<CommissionHistory> findStaffList(Long staffId, Date startDate, Date endDate){
        return commissionDao.findStaffList(staffId, startDate,endDate);
    }
}
