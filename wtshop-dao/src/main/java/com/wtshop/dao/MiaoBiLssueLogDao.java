package com.wtshop.dao;


import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.MiaobiLssue;
import com.wtshop.model.MiaobiLssuelog;

import java.util.List;

/**
 * Created by sq on 2017/7/11.
 */
public class MiaoBiLssueLogDao extends BaseDao<MiaobiLssuelog>{

    public MiaoBiLssueLogDao() {
        super( MiaobiLssuelog.class);
    }

    /**
     * 获取当前正在使用的帮抢
     */
    public Page<MiaobiLssuelog> findPages(Pageable pageable ,boolean status,long id ){

        String select = " SELECT f.*,g.name,g.image,g.market_price,case when  gr.`status` is null then 0 else gr.`status` end  rem_status ";
        String  sqlExceptSelect="";
        if(status){
              sqlExceptSelect="FROM group_buy f LEFT JOIN goods g ON f.product_id = g.id LEFT JOIN (select  * from group_remind ss where ss.member_id="+id+") gr on f.id=gr.group_id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND f.STATUS = 1 ORDER BY price DESC";

        }else{
              sqlExceptSelect="FROM group_buy f LEFT JOIN goods g ON f.product_id = g.id LEFT JOIN (select  * from group_remind ss where ss.member_id="+id+") gr on f.id=gr.group_id WHERE 1 = 1 AND unix_timestamp(now())<unix_timestamp( f.begin_date) AND f.STATUS = 1 ORDER BY price DESC";
        }


           return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }

    /**
     * 获取当前正在使用的帮抢
     */
    public Page<MiaobiLssuelog> findPages(Pageable pageable ,boolean status){

        String select = " SELECT f.*,g.name,g.image,g.market_price ";
        String  sqlExceptSelect="";
        if(!status){
            sqlExceptSelect="FROM group_buy f LEFT JOIN goods g ON f.product_id = g.id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND STATUS = 0 ORDER BY price DESC";

        }else{
            sqlExceptSelect="FROM group_buy f LEFT JOIN goods g ON f.product_id = g.id WHERE 1 = 1 AND unix_timestamp(now())<unix_timestamp( f.begin_date) AND STATUS = 0 ORDER BY price DESC";
        }


        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }    /**
     * 获取当前正在使用的帮抢
     */
    public List<MiaobiLssuelog> findListRe(){

        String sql = " SELECT  f.*,g.name,g.image,g.market_price FROM group_buy f LEFT JOIN goods g on f.product_id=g.id   where 1 = 1 AND status = 0 order by f.sales desc  limit 10";

        return modelManager.find(sql);
    }


    public List<MiaobiLssuelog> findbylssueidMem(Long lssueId, Long id) {

        String sql = " select * from miaobi_lssuelog m where  m.member_id="+id+" and m.miaobil_id="+lssueId+"";

        return modelManager.find(sql);
    }
}
