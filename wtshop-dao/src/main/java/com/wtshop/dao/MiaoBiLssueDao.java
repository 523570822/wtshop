package com.wtshop.dao;


import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.GroupBuy;
import com.wtshop.model.MiaobiLssue;

import java.util.List;

/**
 * Created by sq on 2017/7/11.
 */
public class MiaoBiLssueDao extends BaseDao<MiaobiLssue>{

    public MiaoBiLssueDao() {
        super( MiaobiLssue.class);
    }

    /**
     * 获取当前正在使用的福袋
     */
    public Page<MiaobiLssue> findPages(Pageable pageable ,int status,long id ){

        String select = " select * ";
        String  sqlExceptSelect="";
        if(status==2){
              sqlExceptSelect=" FROM miaobi_lssue ml WHERE (ml.id) NOT IN ( SELECT mg.miaobil_id FROM miaobi_lssuelog mg WHERE mg.member_id = "+id+" ) AND unix_timestamp(now()) < unix_timestamp(ml.end_date) AND unix_timestamp(now()) > unix_timestamp(ml.begin_date)";
        }else  if(status==1){
             select ="select mg.*";
                    sqlExceptSelect="from miaobi_lssuelog mg where  mg.member_id='"+id+"'";
        }else {
            sqlExceptSelect="FROM miaobi_lssue ml WHERE (ml.id) NOT IN ( SELECT mg.miaobil_id FROM miaobi_lssuelog mg WHERE mg.member_id = '"+id+"' ) AND unix_timestamp(now()) < unix_timestamp(ml.begin_date)";
        }


           return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }

    /**
     * 获取当前正在使用的福袋
     */
    public Page<MiaobiLssue> findPages(Pageable pageable ,boolean status){

        String select = " SELECT f.*,g.name,g.image,g.market_price ";
        String  sqlExceptSelect="";
        if(!status){
            sqlExceptSelect="FROM group_buy f LEFT JOIN goods g ON f.product_id = g.id WHERE 1 = 1 AND unix_timestamp(now()) < unix_timestamp(f.end_date) AND unix_timestamp(now()) > unix_timestamp(f.begin_date) AND STATUS = 0 ORDER BY price DESC";

        }else{
            sqlExceptSelect="FROM group_buy f LEFT JOIN goods g ON f.product_id = g.id WHERE 1 = 1 AND unix_timestamp(now())<unix_timestamp( f.begin_date) AND STATUS = 0 ORDER BY price DESC";
        }


        return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
    }    /**
     * 获取当前正在使用的福袋
     */
    public List<MiaobiLssue> findListRe(){

        String sql = " SELECT  f.*,g.name,g.image,g.market_price FROM group_buy f LEFT JOIN goods g on f.product_id=g.id   where 1 = 1 AND status = 0 order by f.sales desc  limit 10";

        return modelManager.find(sql);
    }


}
