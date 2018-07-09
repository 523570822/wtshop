package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.Information;
import com.wtshop.model.Member;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by sq on 2017/8/1.
 */
public class InformationDao extends  BaseDao<Information>{

    /**
     * 构造方法

     */
    public InformationDao() {
        super(Information.class);
    }


    /**
     * 删除信息
     */
    public int updateMessage(Member member){
        if(member == null){
            return 0;
        }

        String sql = " update information set is_delete = 1 where member_id =" + member.getId();

        return Db.update(sql);
    }


    /**
     * 技师信息
     */
    public Information findMember(Long id ){

        String sql = " select i.create_date, m.nickname, m.avatar from information i left join member m on i.staff_id = m.id where 1 = 1 ";

        if(id != null ){
            sql += " AND i.id = "+ id;
        }
        return modelManager.findFirst(sql);

    }




    /**
     * 查找消息分页
     *
     * @param member
     *            会员，null表示管理员
     * @param pageable
     *            分页信息
     * @return 消息分页
     */
    public Page<Information> findPage(Member member, Pageable pageable, Integer type) {
        String sqlExceptSelect = "FROM information i LEFT JOIN member m ON i.staff_id = m.id left join message_link l on i.link = l.uuid LEFT JOIN `order` o ON i.link = o.sn LEFT JOIN `order_item` ord ON ord.order_id=o.id WHERE 1 = 1 AND i.is_delete = 0";

        String select = " select i.id, i.content, i.create_date, i.link, i.type, m.avatar, m.nickname ,l.information ,i.status,ord.thumbnail";
        if (member != null) {
            sqlExceptSelect += " AND i.member_id = " + member.getId() ;
        }
        if (type != null) {
            if( 0 == type){
                sqlExceptSelect += " AND i.type in (0, 2, 5) ";
            }else if( 1 == type) {

                sqlExceptSelect += " AND i.type = 4 ";
            }else if( 2 == type){
                sqlExceptSelect += " AND i.type in (1, 3) ";
            }
        }
        sqlExceptSelect += " GROUP BY i.id order by  create_date desc";
        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }

    /**
     * 全部未读的消息
     * @param member
     * @param read
     * @return
     */
    public List<Information> findMessageNoRead(Member member, Boolean read) {
        String sqlExceptSelect = "select *  FROM information WHERE 1 = 1 AND is_delete = 0";
        if (member != null) {
            sqlExceptSelect += " AND member_id = " + member.getId() ;
        }
        if (read != null) {
            sqlExceptSelect += " AND status = " + read;
        }
        return modelManager.find(sqlExceptSelect);
    }

    /**
     * 查询我的未读消息个数
     * @param member
     * @param read
     * @return
     */
    public Long findMessageNoReadCount(Member member, Boolean read) {
        String sqlExceptSelect = "FROM information WHERE 1 = 1 AND is_delete = 0";
        if (member != null) {
            sqlExceptSelect += " AND member_id = " + member.getId() ;
        }
        if (read != null) {
            sqlExceptSelect += " AND status = " + read;
        }
        return super.count(sqlExceptSelect);
    }

    /**
     * 消息分页记录
     */
    public Page<Record> findPageRecord(Pageable pageable ,Integer type ,String title, String name){

        String sql = " from information i LEFT JOIN member m on i.member_id = m.id where 1 = 1 AND i.is_delete = 0 ";

        if(type != null){
            sql += " AND i.type = "+ type;
        }
        if(title != null){
            sql += " AND i.title LIKE '%" + title +"%' " ;
        }
        if(name != null){
            sql += " AND m.username LIKE '%" + name +"%' " ;
        }

        String select = "select i.id,i.type,i.title,i.create_date,m.username,m.id memberId ";

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
            ordersSQL = " ORDER BY i.create_date DESC";
        }
        sql += ordersSQL;
        return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

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
