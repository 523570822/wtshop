package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.wtshop.model.StaffMember;
import com.wtshop.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 蔺哲 on 2017/9/6.
 */
public class StaffMemberDao extends BaseDao<StaffMember>{
    public StaffMemberDao(){super(StaffMember.class);}

    /**
     * 根据用户id字段查询
     * @param memberId
     * @return
     */
    public StaffMember queryByMemberId(Long memberId){
        String sql = "SELECT * FROM staff_member WHERE `status`=1 AND member_id="+memberId;
        return modelManager.findFirst(sql);
    }
    /**
     * 查找
     */
    public List<Record> queryList(Long staffId, String name){
        String sql = "SELECT s.*,m.nickname as name,m.avatar FROM staff_member s LEFT JOIN member m ON m.id=s.member_id WHERE s.`status`=1 AND staff_id="+staffId;
        if(!StringUtils.isEmpty(name)){
            sql +=" AND s.alias_name Like '%"+name+"%'";
        }
        return Db.find(sql);
    }

    public void updateByStaffId(Long staffId){
        if(staffId == null){
            return ;
        }else {
            String sql = "update staff_member set organ_id = null WHERE `status`=1 AND staff_id= "+ staffId;
            Db.update(sql);
        }
    }
    public List findAliasName(Long[] ids){
        StringBuffer stringBuffer = new StringBuffer();
        for(Long id : ids){
            stringBuffer.append(id+",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        String sql = "SELECT sm.alias_name,m.* FROM staff_member sm INNER JOIN member m ON m.id=sm.member_id WHERE sm.member_id"+" in (" + stringBuffer + ")";
        return Db.find(sql);
    }
}
