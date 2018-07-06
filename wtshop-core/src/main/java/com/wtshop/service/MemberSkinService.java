package com.wtshop.service;

import com.wtshop.dao.MemberInterestDao;
import com.wtshop.dao.MemberSkinDao;
import com.wtshop.model.MemberInterestCategory;
import com.wtshop.model.MemberSkinType;
import com.wtshop.model.SkinType;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/21.
 */
public class MemberSkinService extends BaseService<MemberSkinType>{


    public MemberSkinService(){
        super(MemberSkinType.class);
    }

    private MemberSkinDao memberSkinDao = enhance(MemberSkinDao.class);

    /**
     * 查询当前是否有记录
     */
    public MemberSkinType findRecord(Long membreId, Long interestId){
        return memberSkinDao.findRecord(membreId, interestId);
    }

    /**
     * 删除会员记录
     */
    public void deleteRecord(Long memberId){
        memberSkinDao.deleteRecord(memberId);
    }
}
