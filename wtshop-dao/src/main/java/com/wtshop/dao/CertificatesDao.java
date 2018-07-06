package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Certificates;
import com.wtshop.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by 蔺哲 on 2017/5/25.
 */
public class CertificatesDao extends BaseDao<Certificates> {
    public CertificatesDao(){super(Certificates.class);}

    public Certificates queryByMemberId(Long memberId){
        String sql = "select * from certificates where member_id = " + memberId;
        return modelManager.findFirst(sql);
    }

    /**
     * 根据状态分页查询
     */
    public Page<Certificates> findShenHePage(String username, String create_date, Pageable pageable, Integer type){

            String sql = "From certificates where 1 = 1";
            if(type != null && 4 != type){

                sql +=" AND state = " + type;
            }
            if(!StringUtils.isEmpty(username)){
                sql +=" AND name LIKE " + "'%" +username+"%'";
            }
            if(!StringUtils.isEmpty(create_date)){
                sql +=" AND create_date  >= '" + create_date + "'  ";
            }
            sql += " order by create_date desc ";
            String select = "select * ";
            return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);



    }
}
