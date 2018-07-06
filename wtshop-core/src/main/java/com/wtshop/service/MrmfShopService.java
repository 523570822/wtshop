package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.MrmfShopDao;
import com.wtshop.model.Member;
import com.wtshop.model.MrmfShop;

/**
 * Created by 蔺哲 on 2017/9/27.
 */
public class MrmfShopService extends BaseService<MrmfShop> {
    public MrmfShopService(){super(MrmfShop.class);}

    private MrmfShopDao mrmfShopDao = Enhancer.enhance(MrmfShopDao.class);

    /**
     * 根据用户获取organId
     */
    public MrmfShop findMrmfShop(Member member){
        return mrmfShopDao.findByMemberId(member.getId());
    }


}
