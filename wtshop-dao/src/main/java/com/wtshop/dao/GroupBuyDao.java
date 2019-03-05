package com.wtshop.dao;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.model.FuDai;
import com.wtshop.model.GroupBuy;

import java.util.List;

/**
 * Created by sq on 2017/7/11.
 */
public class GroupBuyDao extends BaseDao<GroupBuy>{

    public GroupBuyDao() {
        super( GroupBuy.class);
    }

    /**
     * 获取当前正在使用的福袋
     */
    public Page<GroupBuy> findPages(Pageable pageable){

        String select = " SELECT f.*,g.name,g.image,g.market_price ";
        String  sqlExceptSelect="FROM group_buy f LEFT JOIN goods g on f.product_id=g.id   where 1 = 1 AND status = 0 order by price desc";
        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }   /**
     * 获取当前正在使用的福袋
     */
    public List<GroupBuy> findListRe(){

        String sql = " SELECT  f.*,g.name,g.image,g.market_price FROM group_buy f LEFT JOIN goods g on f.product_id=g.id   where 1 = 1 AND status = 0 order by f.sales desc  limit 10";

        return modelManager.find(sql);
    }


}
