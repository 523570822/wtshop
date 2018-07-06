package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.model.MemberInterestCategory;
import com.wtshop.model.MemberSkinType;

/**
 * Created by sq on 2017/8/21.
 */
public class MemberSkinDao extends BaseDao<MemberSkinType>{

    public MemberSkinDao(){
        super(MemberSkinType.class);
    }

    /**
     * 查询当前id是否有记录
     */

    public MemberSkinType findRecord(Long memberId ,Long interestId){

        String sql = " select * from member_skin_type where 1 = 1  ";

        if(memberId != null){
            sql += " AND members = " + memberId;
        }
        if(interestId != null){
            sql += " AND skin_type = " + interestId;
        }
        return modelManager.findFirst(sql);

    }


    /**
     * 删除会员记录
     */
    public void deleteRecord(Long memberId){
        Db.update("delete from  member_skin_type where members = " + memberId);
    }

}
