package com.wtshop.dao;

import com.wtshop.model.ReturnsItemProgress;

import java.util.List;

/**
 * Created by sq on 2017/7/5.
 */
public class ReturnsItemProgressDao extends BaseDao<ReturnsItemProgress>{

    /**
     * 构造方法
     */
    public ReturnsItemProgressDao() {
        super(ReturnsItemProgress.class);
    }

    /**
     * 查询进度
     */
    public List<ReturnsItemProgress> findProgressById(Long id){

        String sql = "select create_date , `desc` from returns_item_progress where 1 = 1 ";
        if( id == null) {
            return null;
        }else {
            sql += " AND returns_id = " + id;
        }
        sql += " order by create_date ";

        return modelManager.find(sql);

    }
    /**
     * 查询进度
     */
    public List<ReturnsItemProgress> findByReturnId(Long id){
        String sql = "select * from returns_item_progress where 1 = 1 ";
        if( id == null) {
            return null;
        }else {
            sql += " AND returns_itemId = " + id;
        }
        sql += " order by create_date ";
        return modelManager.find(sql);
    }


}