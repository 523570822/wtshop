package com.wtshop.dao;

import com.wtshop.model.Account;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by sq on 2017/9/12.
 */
public class AccountDao extends BaseDao<Account>{

    public AccountDao(){
        super(Account.class);
    }


    /**
     * 获取用户信息
     */
    public Account findByAccount(String openId,String unionid, Integer type){

        String sql = " select * from account where 1 = 1 ";
        if(StringUtils.isNotBlank(unionid)){
            sql += " AND ( account = '"+ openId + "' or  unionid = '"+ unionid + "') ";
        }else{
            sql += " AND account = '"+ openId + "'";
        }

        if(type != null){

            sql += " AND type = "+ type;

        }
        sql += " ORDER BY create_date desc ";
        return modelManager.findFirst(sql);

    }


    /**
     * 获取用户信息
     */
    public Account getUserInfo(Long memberId, Integer type){

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
    public List<Account> getUserInfoList(Long memberId){

        String sql = " select * from account where 1 = 1 ";
        if(memberId != null){

            sql += " AND member_id = "+ memberId;

        }
        return modelManager.find(sql);

    }

    public Account findByUnionid(String unionid, Integer i) {
        String sql = " select * from account where 1 = 1 ";
        if(unionid != null){

            sql += " AND unionid = '"+ unionid+"'";

        }if(i != null){

            sql += " AND type = "+ i;

        }
        return modelManager.findFirst(sql);
    }

    public Account findByOpenid(String openid) {
        String sql = " select * from account where 1 = 1 ";
        if (openid != null) {

            sql += " AND account = '" + openid +"' or unionid ='"+openid +"' or openId_xcx ='"+openid+"'";



        }
        return modelManager.findFirst(sql);
    }

    public Account findByMemberId(String memberId,String type) {
        String sql = " select * from account where 1 = 1 ";
        if (memberId != null) {
            sql += " AND member_id = '" + memberId+"'";
        }
        if (type != null) {
            sql += " AND type = '" + type+"'";
        }
        return modelManager.findFirst(sql);
    }
}
