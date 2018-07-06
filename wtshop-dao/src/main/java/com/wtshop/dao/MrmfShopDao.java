package com.wtshop.dao;

import com.wtshop.model.MrmfShop;

/**
 * Created by 蔺哲 on 2017/9/27.
 */
public class MrmfShopDao extends BaseDao<MrmfShop> {
    public MrmfShopDao (){super(MrmfShop.class);}

    public MrmfShop findByOrganId(String organId){
        String sql = "select * from mrmf_shop where organ_id="+organId;
        return modelManager.findFirst(sql);
    }
    public MrmfShop findByMemberId(Long memberId){
        String sql = "select * from mrmf_shop where member_id="+memberId;
        return modelManager.findFirst(sql);
    }
}
