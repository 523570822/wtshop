package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Coupon;
import com.wtshop.model.Member;
import com.wtshop.model.Ticketreceive;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */
public class TicketReceiveDao extends BaseDao<Ticketreceive> {
    public TicketReceiveDao() {
        super(Ticketreceive.class);
    }


    //检测重复领取
    public  long  chcekRepeat(long mid,String sn){
        String sql="SELECT COUNT(*) from ticketsnrecord t WHERE    t.ticketSnId=(SELECT tsn.id from ticketsn tsn WHERE sn="+sn+")   and t.memId="+mid;
        return Db.queryLong(sql);
    }

    /**
     * 查找我的分享优惠码分页
     *
     * @param member
     *            会员
     * @param pageable
     *            分页信息
     * @return 优惠码分页
     */
    public Page<Ticketreceive> findSharePage(Member member, Pageable pageable, Boolean isUsed , List<Long> productCategory , Boolean hasExpired, Boolean isEnabled) {
        String sqlExceptSelect = "FROM coupon_code c LEFT JOIN ticketreceive p ON c.receiveId = p.id  WHERE 1 = 1 AND coupon_id = 0";
        String select = "select p.realETime,p.`name`, c.* , p.money, p.condition,  p.image ,p.modulus ,p.type";
        if (member != null) {
            sqlExceptSelect += " AND c.member_id = " + member.getId();
        }
        if (isUsed != null) {
            sqlExceptSelect += " AND is_used = " + isUsed + " ";
        }
        if( productCategory != null){
            sqlExceptSelect += " AND p.product_category_id IN " + SqlUtils.getSQLIn(productCategory) ;
        }
        if (hasExpired != null) {
            if (hasExpired) {
                sqlExceptSelect += " AND (realETime IS NOT NULL AND realETime <= '" + DateUtils.getDateTime()+ "' )";
            } else {
                sqlExceptSelect += " AND (realETime IS NULL OR realETime > '" + DateUtils.getDateTime()+ "' )";
            }
        }
        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }

    /**
     * 查找优惠券分页
     *
     * @param isEnabled
     *            是否启用
     * @param isExchange
     *            是否允许积分兑换
     * @param hasExpired
     *            是否已过期
     * @param pageable
     *            分页信息
     * @return 优惠券分页
     */
    public Page<Ticketreceive> findSharePage( Boolean hasExpired, Pageable pageable ,List<Long> productCategory) {
        String sqlExceptSelect = "FROM ticketreceive WHERE 1 = 1 ";
        if (productCategory != null && productCategory.size()>0) {
            sqlExceptSelect += " AND product_category_id IN " + SqlUtils.getSQLIn(productCategory);
        }
        if (hasExpired != null) {
            if (hasExpired) {
                sqlExceptSelect += " AND (realETime IS NOT NULL AND realETime <= '" + DateUtils.getDateTime()+ "' )";
            } else {
                sqlExceptSelect += " AND (realETime IS NULL OR realETime > '" + DateUtils.getDateTime()+ "' )";
            }
        }
        return super.findPage(sqlExceptSelect, pageable);
    }


}
