package com.wtshop.dao;

import com.wtshop.model.ActIntroduce;

/**
 * Created by Administrator on 2017/8/21.
 */
public class ActIntroduceDao extends  BaseDao<ActIntroduce> {
   public   ActIntroduceDao(){
        super(ActIntroduce.class);
    }

    public  ActIntroduce getDetailsByType(int type){
        String sql="SELECT * from act_introduce  WHERE type= "+type +"  LIMIT 1";
        ActIntroduce actIntroduce=findBySql(sql);
        return  actIntroduce;
    }

}
