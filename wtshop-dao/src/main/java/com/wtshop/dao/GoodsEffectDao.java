package com.wtshop.dao;
import com.jfinal.plugin.activerecord.Db;
import com.wtshop.model.GoodsEffct;

/**
 * Created by sq on 2017/9/5.
 */
public class GoodsEffectDao extends BaseDao<GoodsEffct>{

    public GoodsEffectDao(){
        super(GoodsEffct.class);
    }

}
