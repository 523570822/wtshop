package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.NodifyGoodsSend;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by sq on 2017/10/19.
 */
public class NodifyGoodsSendDao extends BaseDao<NodifyGoodsSend> {


    public NodifyGoodsSendDao(){
        super(NodifyGoodsSend.class);
    }


    public Page<Record> findPageList(Pageable pageable, String sn, String nickname, String phone){

        String sql = " FROM nodify_goods_send n LEFT JOIN `order` o ON n.order_id = o.id LEFT JOIN member m ON n.member_id = m.id WHERE 1 = 1 and n.status = 0 and o.status = 2 ";
        String select = " SELECT n.id, n.create_date, o.sn, o.modify_date, m.nickname, m.phone ,m.id member_id, o.id order_id";
        if( sn != null ){
            sql += "AND o.sn LIKE '%" + sn +"%' " ;
        }
        if( nickname != null ){
            sql += "AND m.nickname LIKE '%" + nickname +"%' " ;
        }
        if( phone != null ){
            sql += "AND m.phone LIKE '%" + phone +"%' " ;
        }

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
            ordersSQL = "ORDER BY n.create_date DESC";
        }
        sql += ordersSQL;

        return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);


    }

    /**
     * 根据orderId 获取对象
     */
    public NodifyGoodsSend findByOrderId(Long orderId){
        if(orderId == null){
            return null;
        }else {
            String sql = " select * from nodify_goods_send where order_id = "+orderId;
            return modelManager.findFirst(sql);
        }
    }


    /**
     * 转换为Order
     *
     *
     *            Root
     * @param orders
     *            排序
     * @return Order
     */
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
