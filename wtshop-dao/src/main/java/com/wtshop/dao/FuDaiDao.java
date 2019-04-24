package com.wtshop.dao;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.model.FuDai;

import java.util.List;

/**
 * Created by sq on 2017/7/11.
 */
public class FuDaiDao extends BaseDao<FuDai>{

    public FuDaiDao() {
        super(FuDai.class);
    }

    /**
     * 获取当前正在使用的福袋
     */
    public List<FuDai> findLists(){

        String sql = " SELECT f.* FROM fu_dai f  \n" +
                " where 1 = 1 AND status = 0 order by price desc";
        return modelManager.find(sql);

    }   public List<FuDai> findLists(String price){
        String sql = " SELECT f.* FROM fu_dai f   where 1 = 1";
if(price!=null){
    sql=sql+" and price="+price;
}
        sql=sql+" AND status = 0 order by price desc";
        return modelManager.find(sql);

    }


}
