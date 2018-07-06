package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.VipgoodsHistory;

/**
 * Created by sq on 2017/9/1.
 */
public class VipGoodsHistoryDao extends BaseDao<VipgoodsHistory>{

    public VipGoodsHistoryDao(){
        super(VipgoodsHistory.class);
    }

    /**
     * 查询置换记录
     */
    public Page<VipgoodsHistory> findPage(Member member , Pageable pageable){

        String sql = " From vipGoods_history where 1 = 1 ";
        if(member != null ){
            sql += " and member_id = "+ member.getId();
        }
        return super.findPage(sql, pageable);

    }


    /**
     * 某个商品的消费信息
     */
    public Page<Record> findMemList(Pageable pageable,Long goodsId){
        String select="SELECT v.*,m.nickname ";
        String sql = " FROM vipGoods_history v LEFT JOIN member m on v.member_id = m.id where 1 = 1 and v.status = 2 ";
        if(goodsId != null){
            sql += " AND goods_id = "+goodsId;
        }
        sql +=" order by create_date desc";
          return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

    }

}
