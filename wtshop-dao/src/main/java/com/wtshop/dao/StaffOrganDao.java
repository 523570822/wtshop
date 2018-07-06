package com.wtshop.dao;

import com.wtshop.model.StaffOrgan;

/**
 * Created by 蔺哲 on 2017/9/14.
 */
public class StaffOrganDao extends BaseDao<StaffOrgan> {
    public StaffOrganDao(){super(StaffOrgan.class);}

    public StaffOrgan findStaffOrgan(String organId,Long staffId){
        String sql = "SELECT * FROM staff_organ sg WHERE sg.is_leave=1 AND sg.member_id="+staffId+" AND sg.organ_id="+organId;
        return super.findBySql(sql);
    }
}
