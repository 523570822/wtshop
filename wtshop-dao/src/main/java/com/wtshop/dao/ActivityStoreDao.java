package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.ActivityStore;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 蔺哲 on 2017/5/25.
 */
public class ActivityStoreDao extends BaseDao<ActivityStore> {
    public ActivityStoreDao(){super(ActivityStore.class);}

    public ActivityStore queryByMemberId(Long memberId){
        String sql = "select * from activity_store where member_id = " + memberId;
        return modelManager.findFirst(sql);
    }

    /**
     * 根据状态分页查询
     */
    public Page<ActivityStore> findShenHePage(String username, String create_date, Pageable pageable, Integer type){

            String sql = "From activity_store where 1 = 1";
            if(type != null && 4 != type){

                sql +=" AND state = " + type;
            }
            if(!StringUtils.isEmpty(username)){
                sql +=" AND name LIKE " + "'%" +username+"%'";
            }
            if(!StringUtils.isEmpty(create_date)){
                sql +=" AND create_date  >= '" + create_date + "'  ";
            }
            sql += " order by create_date desc ";
            String select = "select * ";
            return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);



    }
}
