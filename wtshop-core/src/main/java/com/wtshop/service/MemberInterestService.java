package com.wtshop.service;

import com.jfinal.plugin.activerecord.Record;
import com.wtshop.dao.MemberInterestDao;
import com.wtshop.model.InterestCategory;
import com.wtshop.model.MemberInterestCategory;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by sq on 2017/8/21.
 */
public class MemberInterestService extends BaseService<MemberInterestCategory>{

    public MemberInterestService(){
        super(MemberInterestCategory.class);
    }

    private MemberInterestDao memberInterestDao = enhance(MemberInterestDao.class);

    /**
     * 查询当前是否有记录
     */
    public MemberInterestCategory findRecord(Long memberId, Long interestId){
        return memberInterestDao.findRecord(memberId, interestId);
    }

    /**
     * 删除会员记录
     */
    public void deleteRecord(Long memberId){
         memberInterestDao.deleteRecord(memberId);
    }


}
