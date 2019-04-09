package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.model.MemberInterestCategory;

import java.util.List;

/**
 * Created by sq on 2017/8/21.
 */
public class MemberInterestDao extends BaseDao<MemberInterestCategory>{

    public MemberInterestDao(){
        super(MemberInterestCategory.class);
    }

    /**
     * 查询当前id是否有记录
     */

    public MemberInterestCategory findRecord(Long memberId ,Long interestId){

        String sql = " select * from member_interest_category where 1 = 1  ";

        if(memberId != null){
            sql += " AND members = " + memberId;
        }
        if(interestId != null){
            sql += " AND interest_category = " + interestId;
        }
        return modelManager.findFirst(sql);

    }  /**
     * 查询当前id是否有记录
     */

    public List<MemberInterestCategory> findRecord(Long memberId){

        String sql = " select * from member_interest_category where 1 = 1  ";

        if(memberId != null){
            sql += " AND members = " + memberId;
        }

        return modelManager.find(sql);

    }

    /**
     * 删除会员记录
     */
    public void deleteRecord(Long memberId){
        Db.update("delete from  member_interest_category where members = " + memberId);
    }   /**   /**
     * 删除会员记录
     */
    public void deleteRecordList(String ins,Long memberId){
        Db.update("delete from member_interest_category where interest_category not in ("+ins+") and members=" + memberId);
    }   /**
     * 删除会员记录
     */
    public void updateWeigth(MemberInterestCategory memberInterestCategory){


        Db.update("UPDATE member_interest_category  m set m.weights="+memberInterestCategory.getWeights()+" where m.interest_category="+memberInterestCategory.getInterestCategory()+" and m.members="+memberInterestCategory.getMembers()+"");
    }
}
