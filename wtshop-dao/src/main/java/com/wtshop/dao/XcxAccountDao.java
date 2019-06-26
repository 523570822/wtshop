package com.wtshop.dao;

import com.wtshop.model.Account;
import com.wtshop.model.XcxAccount;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by sq on 2017/9/12.
 */
public class XcxAccountDao extends BaseDao<XcxAccount>{

    public XcxAccountDao(){
        super(XcxAccount.class);
    }


    /**
     * 获取用户信息
     */
    public XcxAccount findByAccount(String openId, Integer type){

        String sql = " select * from account where 1 = 1 ";
        if(StringUtils.isNotBlank(openId)){

            sql += " AND account = '"+ openId + "'";

        }if(type != null){

            sql += " AND type = "+ type;

        }
        sql += " ORDER BY create_date desc ";
        return modelManager.findFirst(sql);

    }


    /**
     * 获取用户信息
     */
    public XcxAccount getUserInfo(Long memberId, Integer type){

        String sql = " select * from account where 1 = 1 ";
        if(memberId != null){

            sql += " AND member_id = "+ memberId;

        }if(type != null){

            sql += " AND type = "+ type;

        }
        return modelManager.findFirst(sql);

    }

    /**
     *  获取用户信息集合
     */
    public List<XcxAccount> getUserInfoList(Long memberId){

        String sql = " select * from account where 1 = 1 ";
        if(memberId != null){

            sql += " AND member_id = "+ memberId;

        }
        return modelManager.find(sql);

    }

}
