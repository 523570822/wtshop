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
     * 获取当前正在使用的帮抢
     */
    public Page<GroupBuy> findPages(Pageable pageable ,boolean status,long id ){

        /*String select = " SELECT f.*,g.name,g.image,g.market_price,case when  gr.`status` is null then 0 else gr.`status` end  rem_status ";
        String  sqlExceptSelect="";
        if(status){
              sqlExceptSelect="FROM group_buy f LEFT JOIN product  p ON f.product_id = p.id LEFT JOIN goods g on g.id=p.goods_id LEFT JOIN (select  * from group_remind ss where ss.member_id="+id+") gr on f.id=gr.group_id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND f.STATUS = 1 ORDER BY orders DESC";

        }else{
              sqlExceptSelect="FROM group_buy f LEFT JOIN product  p ON f.product_id = p.id LEFT JOIN goods g on g.id=p.goods_id LEFT JOIN (select  * from group_remind ss where ss.member_id="+id+") gr on f.id=gr.group_id WHERE 1 = 1 AND unix_timestamp(now())<unix_timestamp( f.begin_date) AND f.STATUS = 1 ORDER BY orders DESC";
        }


           return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);*/
        String select = " SELECT f.*, g. NAME, g.image, g.market_price, CASE WHEN g.id IS NULL THEN 0 ELSE 1 END rem_status";
        String  sqlExceptSelect="";
        if(status){
            sqlExceptSelect="FROM group_buy f LEFT JOIN product p ON f.product_id = p.id LEFT JOIN goods g ON g.id = p.goods_id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND f. STATUS = 1 ORDER BY orders DESC";
        }else{
            sqlExceptSelect="FROM group_buy f LEFT JOIN product  p ON f.product_id = p.id LEFT JOIN goods g on g.id=p.goods_id  WHERE 1 = 1 AND unix_timestamp(now())<unix_timestamp( f.begin_date) AND f.STATUS = 1 ORDER BY orders DESC" ;
        }


        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }

    /**
     * 获取当前正在使用的帮抢
     */
    public Page<GroupBuy> findPages(Pageable pageable ,boolean status){

        String select = " SELECT f.*,g.name,g.image,g.market_price ";
        String  sqlExceptSelect="";
        if(!status){
            sqlExceptSelect="FROM group_buy f LEFT JOIN product  p ON f.product_id = p.id LEFT JOIN goods g on g.id=p.goods_id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND STATUS = 0 ORDER BY price DESC";

        }else{
            sqlExceptSelect="FROM group_buy f LEFT JOIN product  p ON f.product_id = p.id LEFT JOIN goods g on g.id=p.goods_id WHERE 1 = 1 AND unix_timestamp(now())<unix_timestamp( f.begin_date) AND STATUS = 0 ORDER BY price DESC";
        }


        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }    /**
     * 获取当前正在使用的帮抢
     */
    public List<GroupBuy> findListRe(long id){

        String sql = "SELECT f.*,g.name,g.image,g.market_price,case when  gr.`status` is null then 0 else gr.`status` end  rem_status FROM group_buy f LEFT JOIN product  p ON f.product_id = p.id LEFT JOIN goods g on g.id=p.goods_id LEFT JOIN (select  * from group_remind ss where ss.member_id="+id+") gr on f.id=gr.group_id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND f.STATUS = 1 ORDER BY price DESC";
        return modelManager.find(sql);
    }


}
