package com.wtshop.dao;

import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.wtshop.model.Prestore;

import java.util.List;

/**
 * Created by sq on 2017/9/15.
 */
public class PrestoreDao extends BaseDao<Prestore>{

    public PrestoreDao(){
        super(Prestore.class);
    }

    /**
     * 查询店铺的预存款记录
     */
    public List<Prestore> findList(Long organId){

        if( organId != null){
            String sql = " select * from prestore p where 1 = 1 and p.organ_id = " + organId+" ORDER BY p.create_date DESC";
            return modelManager.find(sql);
        }else {
            return null;
        }

    }

}
