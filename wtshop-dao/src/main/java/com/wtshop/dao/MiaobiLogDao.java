package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.MiaobiLog;
import com.wtshop.model.PointLog;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by sq on 2017/9/30.
 */
public class MiaobiLogDao extends BaseDao<MiaobiLog>{

    public MiaobiLogDao(){
        super(MiaobiLog.class);
    }

    /**
     * 根据会员id 获取首次赠送记录
     */
    public MiaobiLog findLogByMemberId(Long memberId){
        if (memberId == null) {
            return null;
        }
        String sql = " select * from miaobi_log where  member_id = " + memberId;
        return modelManager.findFirst(sql);
    }
    /**
     * 根据会员id 获取首次赠送记录
     */
    public List<MiaobiLog> findLogByMemberId(Long memberId,int type){
        if (memberId == null) {
            return null;
        }
        String sql = " select * from miaobi_log m where  type="+type+" and  member_id = " + memberId +" and to_days(m.create_date) = to_days(now())";
        return modelManager.find(sql);
    }

    /**
     * 喵币分页
     * @param pageable
     * @param type
     * @return
     */
    public Page<Record> findPages(Pageable pageable, String name ,Integer type){

        String sql = " from  miaobi_log g LEFT JOIN member m ON g.member_id = m.id WHERE 1 =1  ";
        if( name != null ){
            sql += "AND m.nickname LIKE '%" + name +"%' " ;
        }

        if( type != null && 4 != type ){
            sql += "AND g.type = " + type ;
        }

        String select = "SELECT g.*,m.nickname   ";


        // 排序属性、方向
        String ordersSQL = getOrders(pageable.getOrders());
        String orderProperty = pageable.getOrderProperty();
        Order.Direction orderDirection = pageable.getOrderDirection();
        if (StringUtils.isNotEmpty(orderProperty) && orderDirection != null) {
            switch (orderDirection) {
                case asc:
                    sql += " ORDER BY " + orderProperty + " ASC ";
                    break;
                case desc:
                    sql += " ORDER BY " + orderProperty + " DESC ";
                    break;
            }
        } else if (StrKit.isBlank(ordersSQL)) {
            ordersSQL = " ORDER BY g.create_date DESC";
        }
        sql += ordersSQL;

        return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }

    private String getOrders(List<Order> orders) {
        String orderSql = "";
        if (CollectionUtils.isNotEmpty(orders)) {
            orderSql = " ORDER BY ";
            for (Order order : orders) {
                String property = order.getProperty();
                Order.Direction direction = order.getDirection();
                switch (direction) {
                    case asc:
                        orderSql += property + " ASC, ";
                        break;
                    case desc:
                        orderSql += property + " DESC,";
                        break;
                }
            }
            orderSql = StringUtils.substring(orderSql, 0, orderSql.length() - 1);
        }
        return orderSql;
    }
}
