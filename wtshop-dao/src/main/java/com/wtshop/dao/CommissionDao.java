package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.sun.tools.corba.se.idl.InterfaceGen;
import com.wtshop.Pageable;
import com.wtshop.model.CommissionHistory;
import com.wtshop.model.InterestCategory;
import com.wtshop.model.Member;
import com.wtshop.model.MrmfShop;
import com.wtshop.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by sq on 2017/9/6.
 */
public class CommissionDao extends BaseDao<CommissionHistory>{


    public CommissionDao(){
        super(CommissionHistory.class);
    }

    /**
     * 技师 查询记录
     */

    public Page<CommissionHistory> findHistoryList(Member staff, Date startDate, Date endDate, Pageable pageable, Integer type){

        String sql = " from commission_history c left join member m on c.member_id = m.id left join goods g on c.goods_id = g.id where 1 = 1 ";

        String select = " select c.*,m.nickname memberName,g.name goodsName,m.avatar";

        if(startDate != null){
            sql += " and c.create_date >= '"+ DateUtils.formatDateTime(startDate) + "' ";
        }

        if(endDate != null){
            sql += " and c.create_date <= '"+ DateUtils.formatDateTime(endDate) + "' ";
        }
        if(staff != null){
            sql += " and staff_id = " +staff.getId();
        }
        if(type != null){
            sql += " and tyep = " +type;
        }
        sql += " order by c.create_date desc ";

        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
    }

    /**
     * 店铺 查询记录
     */

    public Page<CommissionHistory> findHistoryLists(MrmfShop mrmfShop, Date startDate, Date endDate, Pageable pageable, Integer type){

        String sql = " from commission_history c left join member m on c.member_id = m.id left join goods g on c.goods_id = g.id where 1 = 1 ";

        String select = " select c.*,m.nickname memberName,g.name goodsName,m.avatar ";

        if(startDate != null){
            sql += " and c.create_date >= '"+ DateUtils.formatDateTime(startDate) + "' ";
        }

        if(endDate != null){
            sql += " and c.create_date <= '"+ DateUtils.formatDateTime(endDate) + "' ";
        }
        if(mrmfShop != null){
            sql += " and organ_id = '" + mrmfShop.getOrganId() + "'";
        }
        if(type != null){
            sql += " and tyep = " +type;
        }
        sql += " order by c.create_date desc ";

        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
    }


    /**
     * 获取技师
     */

    public Page<CommissionHistory> findStaff(Long organId, Date startDate, Date endDate, Pageable pageable, String staffName){
        String sql = " from ( SELECT staff_id , money ,m.nickname staffName , m.avatar FROM commission_history  c left join member m on c.staff_id = m.id  where 1 = 1 and c.tyep = 0 ";

        String select = "select * ,SUM(money) money";

        if(organId != null){
            sql += " and organ_id = " + organId;
        }
        if(startDate != null){
            sql += " and c.create_date >= '"+ DateUtils.formatDateTime(startDate) + "' ";
        }

        if(endDate != null){
            sql += " and c.create_date <= '"+ DateUtils.formatDateTime(endDate) + "' ";
        }

        if(staffName != null){
            sql += " and m.nickname like '%"+ staffName + "%' ";
        }
        sql += " ) m GROUP BY m.staff_id ";

        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
    }

    /**
     * 技师明细
     */

    public Page<CommissionHistory> findHistoryList(Date startDate, Date endDate, Pageable pageable, String staffName){

        String sql = " from commission_history c left join member m on c.member_id = m.id left join goods g on c.goods_id = g.id where 1 = 1 and c.tyep = 0  ";

        String select = " select c.*,m.nickname memberName,g.name goodsName,m.avatar";

        if(startDate != null){
            sql += " and c.create_date >= '"+ DateUtils.formatDateTime(startDate) + "' ";
        }

        if(endDate != null){
            sql += " and c.create_date <= '"+ DateUtils.formatDateTime(endDate) + "' ";
        }
        if(staffName != null){
            sql += " and m.nickname like '%"+ staffName + "%' ";
        }

        sql += " order by c.create_date desc ";

        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
    }

    /**
     * 查询技师
     */
    public List<CommissionHistory> findStaffList(Long staffId ,Date startDate, Date endDate){

        if(staffId != null){
            String sql = " select c.*,m.nickname memberName,g.name goodsName,m.avatar from commission_history c left join member m on c.member_id = m.id left join goods g on c.goods_id = g.id where 1 = 1 and c.tyep = 0  ";
            sql += " and staff_id = " + staffId;
            if(startDate != null){
                sql += " and c.create_date >= '"+ DateUtils.formatDateTime(startDate) + "' ";
            }

            if(endDate != null){
                sql += " and c.create_date <= '"+ DateUtils.formatDateTime(endDate) + "' ";
            }
            sql += " order by c.create_date desc";
            return modelManager.find(sql);

        }else {
            return null;
        }

    }

}
